package com.psl.user.service.Services.ServiceImpl;

import com.psl.user.service.Entity.User;
import com.psl.user.service.Exceptions.ResourceNotFoundException;
import com.psl.user.service.Repository.UserRepository;
import com.psl.user.service.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtService;

    @Autowired
    RestTemplate restTemplate;
    @Override
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user, int userId) {
        User tobeupdatedUser = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("The User with given id not exists"));
        tobeupdatedUser.setUserName(user.getUserName());
        tobeupdatedUser.setEmail(user.getEmail());
        return userRepository.save(user);
    }

    @Override
    public User findUserById(int userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("The User with given id not exists"));
    }

    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }
}
