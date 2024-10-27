package com.app.service.implementation;

import com.app.exception.UserDoesNotExistException;
import com.app.persistence.entity.User;
import com.app.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
       Optional<User> user = userRepository.findUserByEmail(username);
        if (!user.isPresent())
            throw new UserDoesNotExistException();
        return user.get();
    }
}
