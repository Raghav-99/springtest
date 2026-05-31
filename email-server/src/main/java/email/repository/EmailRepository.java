package email.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import email.entity.Email;

public interface EmailRepository extends CrudRepository<Email, String> {
    @Query(value = "select e from Email e where e.sender.email=:email")
    Email findBySenderByEmail(@Param("email") String id);
}
