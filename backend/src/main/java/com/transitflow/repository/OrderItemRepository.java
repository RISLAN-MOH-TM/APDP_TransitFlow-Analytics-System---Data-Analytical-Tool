package com.transitflow.repository;

import com.transitflow.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    
    List<OrderItem> findByOrderId(String orderId);
    
    @Query("SELECT oi.productId, COUNT(oi), SUM(oi.price) FROM OrderItem oi GROUP BY oi.productId ORDER BY COUNT(oi) DESC")
    List<Object[]> getTopProducts();
}
