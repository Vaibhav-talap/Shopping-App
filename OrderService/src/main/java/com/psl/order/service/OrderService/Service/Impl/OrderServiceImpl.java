package com.psl.order.service.OrderService.Service.Impl;

import com.psl.order.service.OrderService.DTO.OrderDTO;
import com.psl.order.service.OrderService.Entity.Orders;
import com.psl.order.service.OrderService.Entity.Product;
import com.psl.order.service.OrderService.Exceptions.ResourceNotFoundException;
import com.psl.order.service.OrderService.Payload.ApiResponse;
import com.psl.order.service.OrderService.Repository.OrderRepository;
import com.psl.order.service.OrderService.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    RestTemplate restTemplate;


    @Override
    public ApiResponse deleteItemFromCart(int orderId, int productId) {
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("The Order with given id not exists"));
        if(order.isStatus()){
            return ApiResponse.builder().message("The Order is placed already").status(HttpStatus.OK).build();
        }
        else{
            if(!order.getProducts().contains(productId)) {
                throw new ResourceNotFoundException("The Product with given id not exists");
            }
            else{
            order.getProducts().remove(productId);
            orderRepository.save(order);
                return ApiResponse.builder().message("The the product from cart successfully.").status(HttpStatus.OK).build();
        }

        }

    }

    @Override
    public OrderDTO addtoCart(Orders order) {
        OrderDTO orderDTO = new OrderDTO();
        List<Product> productList = new ArrayList<>();

        order.setStatus(false);
        order.setProducts(order.getProducts());
        double totalAmount = 0;
        for(int product: order.getProducts()){
            Product p = restTemplate.getForObject("http://PRODUCTSERVICE/Products/"+product,Product.class);
            assert p != null;
            totalAmount += p.getPrice();
            productList.add(p);
        }
        order.setTotalAmount(totalAmount);
        Orders addedOrder = orderRepository.save(order);

        orderDTO.setProducts(productList);
        orderDTO.setTotalAmount(totalAmount);
        orderDTO.setStatus(false);
        orderDTO.setOrderId(addedOrder.getOrderId());
        orderDTO.setOrderName(addedOrder.getOrderName());
        orderDTO.setUserID(addedOrder.getUserID());
        return orderDTO;
    }
    @Override
    public OrderDTO getOrderDetails(int orderId) {
       Orders orders = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("The Order with given id not exists"));

        OrderDTO orderDTO = new OrderDTO();
        List<Product> productList = new ArrayList<>();

        for(int product: orders.getProducts()){
            Product p = restTemplate.getForObject("http://PRODUCTSERVICE/Products/"+product,Product.class);
            productList.add(p);
        }

        orderDTO.setProducts(productList);
        orderDTO.setTotalAmount(orders.getTotalAmount());
        orderDTO.setStatus(false);
        orderDTO.setOrderId(orders.getOrderId());
        orderDTO.setOrderName(orders.getOrderName());
        orderDTO.setUserID(orders.getUserID());
       return orderDTO;
    }
    @Override
    public OrderDTO placeOrder(int orderId) {
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("The Order with given id not exists"));
        order.setStatus(true);
        Orders orders = orderRepository.save(order);
        OrderDTO orderDTO = new OrderDTO();
        List<Product> productList = new ArrayList<>();

        for(int product: orders.getProducts()){
            Product p = restTemplate.getForObject("http://PRODUCTSERVICE/Products/"+product,Product.class);
            productList.add(p);
        }

        orderDTO.setProducts(productList);
        orderDTO.setTotalAmount(orders.getTotalAmount());
        orderDTO.setStatus(false);
        orderDTO.setOrderId(orders.getOrderId());
        orderDTO.setOrderName(orders.getOrderName());
        orderDTO.setUserID(orders.getUserID());
        return orderDTO;
    }

    @Override
    public ApiResponse emptyCart(int orderId) {
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("The Order with given id not exists"));
        if(!order.isStatus()){
            orderRepository.delete(order);
            return ApiResponse.builder().message("The Cart deleted Successfully").status(HttpStatus.OK).build();
        }
        else{
            return ApiResponse.builder().message("The Order is placed already").status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
