package com.psl.order.service.OrderService.Service;

import com.psl.order.service.OrderService.DTO.OrderDTO;
import com.psl.order.service.OrderService.Entity.Orders;
import com.psl.order.service.OrderService.Payload.ApiResponse;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    OrderDTO addtoCart(Orders order);
    OrderDTO getOrderDetails(int orderId);
    OrderDTO placeOrder(int userID);
    ApiResponse emptyCart(int userID);
    OrderDTO deleteItemFromCart(Orders order);
    Optional<List<OrderDTO>> getAllOrdersOfUser(int userID);
}