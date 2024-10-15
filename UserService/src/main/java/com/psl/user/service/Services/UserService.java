package com.psl.user.service.Services;

import com.psl.user.service.Entity.Role;
import com.psl.user.service.Entity.User;

public interface UserService {
    User registerUser(User user);

    User registerAdmin(User user);

    User updateUser(User user, int userId);
    User findUserById(int userId);
    String generateToken(String email);

    Role registerRole(Role role);
}