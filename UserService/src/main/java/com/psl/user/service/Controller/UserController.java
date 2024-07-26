package com.psl.user.service.Controller;

import com.psl.user.service.Entity.User;
import com.psl.user.service.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Users")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user){
        return new ResponseEntity<>(userService.registerUser(user), HttpStatus.CREATED);
    }
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserDetails(@PathVariable int userId){
        return new ResponseEntity<>(userService.findUserById(userId), HttpStatus.CREATED);
    }
}

