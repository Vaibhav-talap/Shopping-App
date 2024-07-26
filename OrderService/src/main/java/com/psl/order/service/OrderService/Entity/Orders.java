package com.psl.order.service.OrderService.Entity;

import jakarta.persistence.*;
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
@Builder
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int orderId;
    @NotEmpty(message = "Order Name should not be empty")
    private String orderName;
    private double totalAmount;
    private boolean status;
    private List<Integer> products = new ArrayList<>();
    private int userID;
}

