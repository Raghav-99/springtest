import dto.EmailRequest;
import dto.EmailResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

public class EmailClient {
    private final Set<Integer> options = Set.of(1, 2);
    private final Map<Integer, File> fileMap = new HashMap<>();
    private int attachmentCount = 0;
    private static final HttpClient httpClient = HttpClient.newBuilder().build();
    public EmailClient() {
        File directory = new File("../resources/attachments");
        if(directory.isDirectory()) {
            File[] attachments = directory.listFiles();
            int id = 1;
            if(attachments != null) {
                for (File attachment : attachments) {
                    fileMap.put(id++, attachment);
                }
                attachmentCount = id;
            }
        }
    }
    public void start() throws IOException, InterruptedException {
        System.out.println("Client started...");
        while(true) {
            System.out.println("1: Read my mails\r\n2: Send a mail\r\n0: Exit");
            Scanner sc = new Scanner(System.in);
            String inp = sc.nextLine();
            int op = isInputValid(inp);
            
            switch(op) {
                case 1 -> {
                    System.out.println("Enter the recipient email: ");
                    String rId = sc.nextLine().trim();
                    if(rId.isBlank())
                    {
                        System.out.println("Recipient cannot be blank! Aborting operation...");
                        break;
                    }
                    HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(String.format("http://localhost:8080/api/email/receive?email={%s}", rId))).GET().build();
                    HttpResponse<String> emailResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                    System.out.println(emailResponse.body().toString());
                    break;
                }
                case 2 -> {
                    System.out.println("Enter the following details:");
                    System.out.println("1. Sender email: ");
                    String sender = sc.nextLine().trim();

                    System.out.println("2. Subject:");
                    String subject = sc.nextLine().trim();

                    System.out.println("3. Body:");
                    String body = sc.nextLine().trim();

                    System.out.println("4. Select attachment(optional and relative to resources folder):");
                    File attachment = getAttachment(sc);
                    if (attachment == null) {
                        System.out.println("No attachment selected!");
                    } else {
                        System.out.println("File " + attachment.getName() + " selected!");
                    }


                    System.out.println("5. Recipients(in csv):");
                    String recipients = sc.nextLine().trim();

                    EmailRequest email = buildEmail(sender, subject, body, attachment, recipients);
                    if(email != null) {
                        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/api/email/send")).POST(HttpRequest.BodyPublishers.ofString(email.toString())).build();
                        HttpResponse<String> emailResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                        System.out.println(emailResponse.body());
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

    private File getAttachment(Scanner sc) {
        if(attachmentCount < 1) return null;

        for (int i = 0; i < attachmentCount; i++) {
            String f = String.format("%d: %s", i+1, fileMap.get(i + 1).getName());
            System.out.print(f);
        }
        int op = isInputValid(sc.nextLine().trim());
        if(op != 0 && fileMap.containsKey(op)) {
            return fileMap.get(op);
        }
        return null;
    }

    private EmailRequest buildEmail(String sender, String subject, String body, File attachment, String recipients) {
        if(sender.isBlank() || recipients.isBlank()) {
            System.out.println("Not allowed: Sender or recipients cannot be blank!");
            return null;
        }
        String content = null;
        if(attachment != null) {
            if(!attachment.exists()) {
                System.out.println("Aborting: File at path "+attachment.getPath()+" not found!");
                return null;
            }
            byte[] data = streamFile(attachment);
            if(data != null && data.length > 0) content = Base64.getEncoder().encodeToString(data);
        }
        List<String> r = Arrays.asList(recipients.split(","));
        return new EmailRequest(sender, r, subject, body, content);
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