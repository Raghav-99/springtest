package apiapp;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(value = "message")
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
		entity.messageStatus = MessageStatus.PENDING.name();
		repository.save(entity);
	}

	@Override
	@Cacheable
	public MessageEntity retrieveMessage(int id) throws Exception {
		Optional<MessageEntity> message = id <= 0 ? repository.findFirstByOrderByIdDesc() : repository.findById(id);
		if(message.isEmpty()) throw new Exception("No message found!");
		id = message.get().id;
		System.out.printf("WARN: cache miss for key: %d\n", id);
		return message.get();
	}

}
