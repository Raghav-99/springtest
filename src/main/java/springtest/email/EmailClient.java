package springtest.email;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import springtest.email.IEmailController.EmailRequest;
import springtest.email.IEmailController.EmailResponse;
import springtest.email.service.EmailService;

@Component
@ConditionalOnBean(value = EmailService.class)
public class EmailClient implements Runnable {
	private final Set<Integer> options = Set.of(1, 2);
	@Override
	public void run() {
		while(true) {
			System.out.println("1: Read my mails\r\n2: Send a mail\r\n");
			Scanner sc = new Scanner(System.in);
			String inp = sc.nextLine();
			int op = isInputValid(inp);
			
			RestClient rc = RestClient.builder().baseUrl("http://localhost:8080/api/email").build();
			switch(op) {
				case 1 -> {
					System.out.println("Enter the recipient id: ");
					inp = sc.nextLine();
					int rId = isInputValid(inp);
					EmailResponse emailResponse = rc.get().uri((b) -> b.path("/receive/{id}").build(Map.of("id", rId)))
					.exchange((req, resp) -> resp.bodyTo(EmailResponse.class));
					System.out.println(emailResponse.toString());
					break;
				}
				case 2 -> {
					System.out.println("Enter the following details:");
					System.out.println("1. Sender email: ");
					String sender = sc.nextLine();
					
					System.out.println("2. Subject:");
					String subject = sc.nextLine();
					
					System.out.println("3. Body:");
					String body = sc.nextLine();
					
					System.out.println("4. Path to attachment(optional and relative to resources folder):");
					String path = sc.nextLine();
					
					System.out.println("5. Recipients(in csv):");
					String recipients = sc.nextLine();
					
					EmailRequest email = buildEmail(sender, subject, body, path, recipients);
					if(email != null) {
						System.out.println(rc.post().uri(b -> b.path("/send").build())
						.exchange((req, resp) -> resp.bodyTo(EmailResponse.class)));
					}
					break;
				}
				default -> {
					break;
				}
			}
			if(!options.contains(op)) break;
		}
	}
	
	private EmailRequest buildEmail(String sender, String subject, String body, String path, String recipients) {
		if(sender.isBlank() || recipients.isBlank()) {
			System.out.println("Not allowed: Sender or recipients cannot be blank!");
			return null;
		}
		String attachment = null;
		if(!path.isBlank()) {
			File file = new File(path);
			if(!file.exists()) {
				System.out.println("Aborting: File at path "+path+" not found!");
				return null;
			}
			byte[] data = streamFile(file);
			if(data.length > 0) attachment = Base64.getEncoder().encodeToString(data);
		}
		List<String> r = null;
		if(recipients.length() > 0) {
			r = Arrays.asList(recipients.split(","));
		}
		else {
			System.out.println("Aborting: Recipients cannot be empty!");
			return null;
		}
		return new EmailRequest(sender, r, subject, body, attachment);
	}

	private byte[] streamFile(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			return fis.readAllBytes();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private int isInputValid(String inp) {
		int op = 0;
		if(inp != null && !inp.isBlank()) {
			try
			{
				op = Integer.parseInt(inp);
			}catch(NumberFormatException nfx) {
				System.out.println(nfx.getMessage());
			}
		}
		return op;
	}

}
