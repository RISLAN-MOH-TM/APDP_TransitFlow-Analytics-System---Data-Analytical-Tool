package com.transitflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    
    @Id
    @Column(name = "order_id", nullable = false, unique = true)
    private String orderId;
    
    @Column(name = "customer_id")
    private String customerId;
    
    @Column(name = "order_status")
    private String orderStatus;
    
    @Column(name = "order_purchase_timestamp")
    private LocalDateTime orderPurchaseTimestamp;
    
    @Column(name = "order_approved_at")
    private LocalDateTime orderApprovedAt;
    
    @Column(name = "order_delivered_carrier_date")
    private LocalDateTime orderDeliveredCarrierDate;
    
    @Column(name = "order_delivered_customer_date")
    private LocalDateTime orderDeliveredCustomerDate;
    
    @Column(name = "order_estimated_delivery_date")
    private LocalDateTime orderEstimatedDeliveryDate;
}
