package com.transitflow.repository;

import com.transitflow.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    List<Payment> findByOrderId(String orderId);
    
    @Query("SELECT p.paymentType, COUNT(p), SUM(p.paymentValue) FROM Payment p GROUP BY p.paymentType")
    List<Object[]> aggregateByPaymentType();
    
    @Query("SELECT SUM(p.paymentValue) FROM Payment p WHERE p.orderId = :orderId")
    Double sumByOrderId(String orderId);
    
    @Query("SELECT SUM(p.paymentValue) FROM Payment p")
    Double sumAllPayments();
    
    @Query("SELECT FUNCTION('TO_CHAR', o.orderPurchaseTimestamp, 'YYYY-MM'), SUM(p.paymentValue) " +
           "FROM Payment p JOIN Order o ON p.orderId = o.orderId " +
           "WHERE o.orderPurchaseTimestamp IS NOT NULL " +
           "GROUP BY FUNCTION('TO_CHAR', o.orderPurchaseTimestamp, 'YYYY-MM') " +
           "ORDER BY FUNCTION('TO_CHAR', o.orderPurchaseTimestamp, 'YYYY-MM')")
    List<Object[]> sumRevenueByMonth();
}
