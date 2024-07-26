package com.psl.user.service.Entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
public class Orders {

    private int orderId;
    @NotEmpty(message = "Order Name should not be empty")
    private String orderName;
    private double totalAmount;
    private boolean status;
    private List<Integer> products = new ArrayList<>();
    private int userId;

}
