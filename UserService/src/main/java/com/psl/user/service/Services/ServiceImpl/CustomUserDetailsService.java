package com.psl.user.service.Services.ServiceImpl;

import com.psl.user.service.Entity.User;
import com.psl.user.service.Config.CustomUserDetails;
import com.psl.user.service.Exceptions.ResourceNotFoundException;
import com.psl.user.service.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = repository.findByEmail(username);
        return user.map(CustomUserDetails::new).orElseThrow(() -> new ResourceNotFoundException("user not found with name :" + username));
    }
}
