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

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    RestTemplate restTemplate;


    @Override
    public OrderDTO deleteItemFromCart(Orders order) {
        OrderDTO orderDTO = new OrderDTO();
        HashMap<Product, Integer> productWithQuantityMap = new HashMap<>();
        double totalAmount = 0;
        Optional<List<Orders>> userOrderList = orderRepository.findByUserID(order.getUserID());
        if(userOrderList.isPresent()){
            Optional<Orders> userCart =  userOrderList.get()
                    .stream()
                    .filter(savedOrder -> savedOrder.getOrderState() == Orders.OrderState.CART).findAny();
            if(userCart.isPresent()){
                Orders cart = userCart.get();
                cart.setProductQuantityAndId(order.getProductQuantityAndId());
                for(Map.Entry<Integer,Integer> product : cart.getProductQuantityAndId().entrySet()){
                    Product p = restTemplate.getForObject("http://PRODUCTSERVICE/Products/"+product.getKey(),Product.class);
                    assert p != null;
                    totalAmount += p.getPrice()*product.getValue();
                    productWithQuantityMap.put(p,product.getValue());
                }
                Orders updatedCart = orderRepository.save(cart);
                orderDTO.setCartItemWithQuantity(productWithQuantityMap);
                orderDTO.setTotalAmount(updatedCart.getTotalAmount());
                orderDTO.setOrderState(updatedCart.getOrderState());
                orderDTO.setOrderId(updatedCart.getOrderId());
                orderDTO.setUserID(updatedCart.getUserID());
                return orderDTO;
            }
            else{
                throw new ResourceNotFoundException("Please add items in cart");
            }
        }
        else{
            throw new ResourceNotFoundException("Please add items in cart");
        }
    }
    @Override
    public Optional<List<OrderDTO>> getAllOrdersOfUser(int userID) {
        Optional<List<OrderDTO>> optionalOrderDTOList;
        Optional<List<Orders>> userOrderList = orderRepository.findByUserID(userID);
        if(userOrderList.isPresent()) {
            Optional<Orders> userPlacedOrder = userOrderList.get()
                    .stream()
                    .filter(savedOrder -> savedOrder.getOrderState() == Orders.OrderState.PLACED).findAny();
            if (userPlacedOrder.isPresent()) {
                optionalOrderDTOList = Optional.of(userOrderList.get()
                        .stream()
                        .filter(userOrder -> userOrder.getOrderState() == Orders.OrderState.PLACED)
                        .map(this::convertOrderToOrderDTO)
                        .toList());
                return optionalOrderDTOList;
            }
            else{
                optionalOrderDTOList = Optional.empty();

            }
        }
        else{
            optionalOrderDTOList = Optional.empty();
        }
        return optionalOrderDTOList;
    }

    public OrderDTO convertOrderToOrderDTO(Orders order){
        HashMap<Product, Integer> productWithQuantityMap = new HashMap<>();
        OrderDTO orderDTO = new OrderDTO();
        for(Map.Entry<Integer,Integer> product : order.getProductQuantityAndId().entrySet()){
            Product p = restTemplate.getForObject("http://PRODUCTSERVICE/Products/"+product.getKey(),Product.class);
            productWithQuantityMap.put(p,product.getValue());
        }

        orderDTO.setCartItemWithQuantity(productWithQuantityMap);
        orderDTO.setTotalAmount(order.getTotalAmount());
        orderDTO.setOrderState(order.getOrderState());
        orderDTO.setOrderId(order.getOrderId());
        orderDTO.setUserID(order.getUserID());
        return orderDTO;
    }
    @Override
    public OrderDTO addtoCart(Orders order) {
        OrderDTO orderDTO = new OrderDTO();
        Optional<List<Orders>> userOrderList = orderRepository.findByUserID(order.getUserID());
        HashMap<Product, Integer> productWithQuantityMap = new HashMap<>();
        double totalAmount = 0;

        if(userOrderList.isPresent()){
           Optional<Orders> userCart =  userOrderList.get()
                            .stream()
                            .filter(savedOrder -> savedOrder.getOrderState() == Orders.OrderState.CART).findAny();
           if(userCart.isPresent()){
               Orders cart = userCart.get();
               for(Map.Entry<Integer,Integer> product : order.getProductQuantityAndId().entrySet()){
                       Product p = restTemplate.getForObject("http://PRODUCTSERVICE/Products/"+product.getKey(),Product.class);
                       assert p != null;
                       totalAmount += p.getPrice()*product.getValue();
                       productWithQuantityMap.put(p,product.getValue());
               }
               orderDTO.setCartItemWithQuantity(productWithQuantityMap);
               cart.setTotalAmount(totalAmount);
               Orders updatedCart = orderRepository.save(cart);
               orderDTO.setTotalAmount(totalAmount);
               orderDTO.setOrderState(Orders.OrderState.CART);
               orderDTO.setOrderId(updatedCart.getOrderId());
               orderDTO.setUserID(updatedCart.getUserID());
               return orderDTO;
           }
           }
        order.setOrderState(Orders.OrderState.CART);
        for(Map.Entry<Integer,Integer> product : order.getProductQuantityAndId().entrySet()){
            Product p = restTemplate.getForObject("http://PRODUCTSERVICE/Products/"+product.getKey(),Product.class);
            assert p != null;
            totalAmount += p.getPrice()*product.getValue();
            productWithQuantityMap.put(p,product.getValue());
        }
        orderDTO.setCartItemWithQuantity(productWithQuantityMap);
        order.setTotalAmount(totalAmount);
        Orders newCart = orderRepository.save(order);
        orderDTO.setTotalAmount(totalAmount);
        orderDTO.setOrderState(Orders.OrderState.CART);
        orderDTO.setOrderId(newCart.getOrderId());
        orderDTO.setUserID(newCart.getUserID());
        return orderDTO;
        }

    @Override
    public OrderDTO getOrderDetails(int orderId) {
       Orders order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("The Order with given id not exists"));
       HashMap<Product, Integer> productWithQuantityMap = new HashMap<>();


        OrderDTO orderDTO = new OrderDTO();
        for(Map.Entry<Integer,Integer> product : order.getProductQuantityAndId().entrySet()){
            Product p = restTemplate.getForObject("http://PRODUCTSERVICE/Products/"+product.getKey(),Product.class);
            productWithQuantityMap.put(p,product.getValue());
        }

        orderDTO.setCartItemWithQuantity(productWithQuantityMap);
        orderDTO.setTotalAmount(order.getTotalAmount());
        orderDTO.setOrderState(order.getOrderState());
        orderDTO.setOrderId(order.getOrderId());
        orderDTO.setUserID(order.getUserID());
       return orderDTO;
    }
    @Override
    public OrderDTO placeOrder(int userID) {
        Optional<Orders> orders;
        Optional<List<Orders>> userOrderList = orderRepository.findByUserID(userID);
        if(userOrderList.isPresent()){
            orders = userOrderList.get().stream()
                    .filter(order -> order.getOrderState() == Orders.OrderState.CART).findAny();

            if(orders.isPresent()){
                Orders cart = orders.get();
                cart.setOrderState(Orders.OrderState.PLACED);
                Orders placedOrder = orderRepository.save(cart);
                HashMap<Product, Integer> productWithQuantityMap = new HashMap<>();
                OrderDTO orderDTO = new OrderDTO();
                for(Map.Entry<Integer,Integer> product : placedOrder.getProductQuantityAndId().entrySet()){
                    Product p = restTemplate.getForObject("http://PRODUCTSERVICE/Products/"+product.getKey(),Product.class);
                    productWithQuantityMap.put(p,product.getValue());
                }

                orderDTO.setCartItemWithQuantity(productWithQuantityMap);
                orderDTO.setTotalAmount(placedOrder.getTotalAmount());
                orderDTO.setOrderState(placedOrder.getOrderState());
                orderDTO.setOrderId(placedOrder.getOrderId());
                orderDTO.setUserID(placedOrder.getUserID());
                return orderDTO;
            }
            else{
                throw new ResourceNotFoundException("Please add items in cart");
            }
        }
        else{
            throw new ResourceNotFoundException("Please add items in cart");
        }
    }

    @Override
    public ApiResponse emptyCart(int userID) {
        Optional<Orders> orders;
        Optional<List<Orders>> userOrderList = orderRepository.findByUserID(userID);
        if(userOrderList.isPresent()){
            orders = userOrderList.get().stream()
                    .filter(order -> order.getOrderState() == Orders.OrderState.CART).findAny();

            if(orders.isPresent()){
            orderRepository.delete(orders.get());
            return ApiResponse.builder().message("The Cart deleted Successfully").status(HttpStatus.OK).build();
        }
            else{
                throw new ResourceNotFoundException("Cart is already empty");
            }
        }
        else{
            return ApiResponse.builder().message("Cart is already empty").status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
