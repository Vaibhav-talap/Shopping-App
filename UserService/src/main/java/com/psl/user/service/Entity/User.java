package com.psl.user.service.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "user_table")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotEmpty(message = "User Name should not be empty")
    private String userName;
    @Email
    private String email;
    @NotEmpty(message = "Password should not be empty")
    private String password;

    @Transient
    private List<Orders> orders = new ArrayList<>();
}
