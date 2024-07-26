package com.psl.order.service.OrderService.Service;

import com.psl.order.service.OrderService.DTO.OrderDTO;
import com.psl.order.service.OrderService.Entity.Orders;
import com.psl.order.service.OrderService.Payload.ApiResponse;

public interface OrderService {
    OrderDTO addtoCart(Orders order);
    OrderDTO getOrderDetails(int orderId);
    OrderDTO placeOrder(int orderID);
    ApiResponse emptyCart(int orderId);
    ApiResponse deleteItemFromCart(int orderId, int productId);
}