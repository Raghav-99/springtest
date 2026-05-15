package springtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class MessageController {
	private final IMessageService messageService;
	public MessageController(@Autowired IMessageService messageService) {
		this.messageService = messageService;
	}
	
   	@PostMapping("message")
	public ResponseEntity<MessageResponse> sendMessage(@RequestBody String message) throws Exception {
		messageService.sendMessage(message);
		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}
	
	@GetMapping("message")
	public ResponseEntity<MessageResponse> retrieveMessage(@PathVariable(required = false) String id) throws Exception {
		int m_id = id == null || id.isBlank() ? 0 : Integer.parseInt(id);
		MessageEntity message = messageService.retrieveMessage(m_id);
		return ResponseEntity.ok(new MessageResponse(message, null));
		
	}
	
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<MessageResponse> MessageExceptionHandler(Exception ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(null, ex.getMessage()));
	}
}
