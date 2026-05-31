package email.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import email.entity.User;
import email.repository.UserRepository;
import email.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public User getOrCreateUser(User user) {
        try {
            return userRepository.save(user);
        }catch (Exception exception) {
            return userRepository.findByEmail(user.getEmail()).get();
        }
    }
}
