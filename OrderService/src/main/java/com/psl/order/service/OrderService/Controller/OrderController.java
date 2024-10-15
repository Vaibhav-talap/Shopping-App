package com.psl.order.service.OrderService.Controller;

import com.psl.order.service.OrderService.DTO.OrderDTO;
import com.psl.order.service.OrderService.Entity.Orders;
import com.psl.order.service.OrderService.Payload.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.psl.order.service.OrderService.Service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/Orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> addToCart(@Valid @RequestBody Orders order) {
        return new ResponseEntity<>(orderService.addtoCart(order), HttpStatus.CREATED);
    }

    @PutMapping("/{userID}")
    public ResponseEntity<OrderDTO> placeOrder(@PathVariable int userID){
        return new ResponseEntity<>(orderService.placeOrder(userID), HttpStatus.CREATED);
    }

    @GetMapping("/users/{userID}")
    public ResponseEntity<?> getAllOrdersOfUser(@PathVariable int userID){
        return new ResponseEntity<>(orderService.getAllOrdersOfUser(userID), HttpStatus.OK);
    }
    @GetMapping("/{orderID}")
    public ResponseEntity<OrderDTO> getOrderDetails(@PathVariable int orderID){
        return new ResponseEntity<>(orderService.getOrderDetails(orderID), HttpStatus.OK);
    }

    @DeleteMapping("/{userID}")
    public ResponseEntity<ApiResponse> emptyCart(@PathVariable int userID){
        return new ResponseEntity<>(orderService.emptyCart(userID),HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity<OrderDTO> deleteItemFromCart(@RequestBody Orders order){
        return new ResponseEntity<>(orderService.deleteItemFromCart(order),HttpStatus.OK);
    }

}
