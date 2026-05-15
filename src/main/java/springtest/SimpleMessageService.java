package springtest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimpleMessageService implements IMessageService {
	private final SimpleMessageRepository repository;
	public SimpleMessageService(@Autowired SimpleMessageRepository repository) {
		this.repository = repository;
	}
	@Override
	public void sendMessage(String message) throws Exception {
		if(message.isBlank()) throw new Exception("Not allowed: Cannot send a blank message!");
		MessageEntity entity = new MessageEntity();
		entity.message = message;
		repository.save(entity);
	}

	@Override
	public MessageEntity retrieveMessage(int id) throws Exception {
		Optional<MessageEntity> message = id <= 0 ? repository.findFirstByOrderByIdDesc() : repository.findById(id);
		if(message.isEmpty()) throw new Exception("No message found!");
		return message.get();
	}

}
