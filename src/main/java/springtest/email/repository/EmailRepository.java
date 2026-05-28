package springtest.email.repository;

import org.springframework.data.repository.CrudRepository;
import springtest.email.entity.Email;

public interface EmailRepository extends CrudRepository<Email, String> {
    Email findByRecepientId(int id);
}
