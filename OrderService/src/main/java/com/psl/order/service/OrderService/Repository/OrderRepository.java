package com.psl.order.service.OrderService.Repository;

import com.psl.order.service.OrderService.Entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository  extends JpaRepository<Orders,Integer> {

    Optional<List<Orders>> findByUserID(int userID);
}
