package com.psl.order.service.OrderService.DTO;

import com.psl.order.service.OrderService.Entity.Product;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.ArrayList;
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
    @NotEmpty(message = "Order Name should not be empty")
    private String orderName;
    private double totalAmount;
    private boolean status;
    private List<Product> products = new ArrayList<>();
    private int userID;
}
