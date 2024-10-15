package com.psl.order.service.OrderService.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.HashMap;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Orders {

    public enum OrderState { CART, PLACED, DELIVERED}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int orderId;
    private double totalAmount;
    private HashMap<Integer, Integer> productQuantityAndId;
    private int userID;
    @Enumerated(EnumType.STRING)
    private OrderState orderState;

}

