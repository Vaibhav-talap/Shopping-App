package com.psl.user.service.Services.ServiceImpl;

import com.psl.user.service.Entity.Role;
import com.psl.user.service.Entity.User;
import com.psl.user.service.Exceptions.ResourceNotFoundException;
import com.psl.user.service.Repository.RoleRepository;
import com.psl.user.service.Repository.UserRepository;
import com.psl.user.service.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtService;

    @Autowired
    RestTemplate restTemplate;
    @Override
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        List<Role> roleList =  new ArrayList<>();
        roleList.add(roleRepository.findByRoleName("ROLE_USER").orElseThrow(() -> new ResourceNotFoundException("The Role with given name not exists")));
        user.setRoles(roleList);
        return userRepository.save(user);
    }

    @Override
    public User registerAdmin(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        List<Role> roleList =  new ArrayList<>();
        roleList.add(roleRepository.findByRoleName("ROLE_ADMIN").orElseThrow(() -> new ResourceNotFoundException("The Role with given name not exists")));
        user.setRoles(roleList);
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

    @Override
    public Role registerRole(Role role) {
        return roleRepository.save(role);
    }
}
