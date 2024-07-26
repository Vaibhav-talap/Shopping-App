package com.psl.order.service.OrderService.Repository;

import com.psl.order.service.OrderService.Entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository  extends JpaRepository<Orders,Integer> {
}
