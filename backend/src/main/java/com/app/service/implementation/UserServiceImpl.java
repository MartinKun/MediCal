package com.app.service.implementation;

import com.app.exception.UserDoesNotExistException;
import com.app.exception.UserNotEnabledException;
import com.app.persistence.entity.User;
import com.app.persistence.repository.UserRepository;
import com.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(UserDoesNotExistException::new);
        if(!user.isEnabled()) throw new UserNotEnabledException();

        return user;
    }
}
