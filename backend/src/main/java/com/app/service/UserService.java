package com.app.service;

import com.app.persistence.entity.User;

public interface UserService {
    User getUserByEmail(String email);

}
