package com.psl.user.service.Services;

import com.psl.user.service.Entity.User;

public interface UserService {
    User registerUser(User user);
    User updateUser(User user, int userId);
    User findUserById(int userId);
}