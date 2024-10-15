package com.psl.order.service.OrderService.DTO;

import com.psl.order.service.OrderService.Entity.Orders;
import com.psl.order.service.OrderService.Entity.Product;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class OrderDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int orderId;
    private double totalAmount;
    private Orders.OrderState orderState;
    private HashMap<Product,Integer> cartItemWithQuantity;
    private int userID;
}
