package email.repository;

import org.springframework.data.repository.CrudRepository;
import email.entity.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
