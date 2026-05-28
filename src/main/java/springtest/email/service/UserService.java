package springtest.email.service;

import org.springframework.stereotype.Service;
import springtest.email.entity.User;

public interface UserService {
    User getOrCreateUser(User user);
}
