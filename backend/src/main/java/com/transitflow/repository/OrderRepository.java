package com.transitflow.repository;

import com.transitflow.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    
    List<Order> findByOrderStatus(String status);
    
    List<Order> findByOrderPurchaseTimestampBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT o.orderStatus, COUNT(o) FROM Order o GROUP BY o.orderStatus")
    List<Object[]> countByStatus();
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.customerId = :customerId")
    Long countByCustomerId(String customerId);
    
    @Query("SELECT o.customerId, COUNT(o) FROM Order o GROUP BY o.customerId")
    List<Object[]> countOrdersByCustomer();
}
