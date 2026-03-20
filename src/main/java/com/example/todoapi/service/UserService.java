package com.example.todoapi.service;

import com.example.todoapi.User;
import com.example.todoapi.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getOrCreateUser(Long telegramId, String username){

        Optional<User> optional = userRepository.findByTelegramId(telegramId);

        if (optional.isPresent()){
            return optional.get();
        }else {
            User optional1 = new User();
            optional1.setTelegramId(telegramId);
            optional1.setUsername(username);
            return userRepository.save(optional1);
        }
    }
}
