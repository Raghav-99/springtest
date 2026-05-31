package email.service;

import email.entity.User;

public interface UserService {
    User getOrCreateUser(User user);
}
