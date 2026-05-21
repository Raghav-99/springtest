package springtest;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SimpleMessageRepository extends JpaRepository<MessageEntity, Integer> {
	
	Optional<MessageEntity> findFirstByOrderByIdDesc();
}
