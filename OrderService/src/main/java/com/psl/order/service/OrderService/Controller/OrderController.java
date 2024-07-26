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

@RestController
@RequestMapping("/Orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> addtoCart(@Valid @RequestBody Orders order) {
        return new ResponseEntity<>(orderService.addtoCart(order), HttpStatus.CREATED);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDTO> placeOrder(@PathVariable int orderId){
        return new ResponseEntity<>(orderService.placeOrder(orderId), HttpStatus.CREATED);
    }
    @GetMapping("/{orderId}")

    public ResponseEntity<OrderDTO> getOrderDetails(@PathVariable int orderId){
        return new ResponseEntity<>(orderService.getOrderDetails(orderId), HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse> emptyCart(@PathVariable int orderId){
        return new ResponseEntity<>(orderService.emptyCart(orderId),HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}/Product/{productId}")
    public ResponseEntity<ApiResponse> deleteItemFromCart(@PathVariable int orderId, @PathVariable int productId){
        return new ResponseEntity<>(orderService.deleteItemFromCart(orderId,productId),HttpStatus.OK);
    }

}
