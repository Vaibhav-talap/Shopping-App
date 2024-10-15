package com.psl.product.service.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int productId;

    @NotEmpty(message = "Product Name should not be empty")
    private String productName;
    @NotEmpty(message = "Product Category should not be empty")
    private String productCategory;
    private double price;
    private String imageUrl;


}

