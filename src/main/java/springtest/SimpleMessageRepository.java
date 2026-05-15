package springtest;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SimpleMessageRepository extends JpaRepository<MessageEntity, Integer> {
	
	Optional<MessageEntity> findFirstByOrderByIdDesc();
}
