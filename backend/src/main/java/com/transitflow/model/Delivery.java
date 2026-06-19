package com.transitflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "deliveries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {
    
    @Id
    @Column(name = "order_id", nullable = false, unique = true)
    private String orderId;
    
    @Column(name = "customer_id")
    private String customerId;
    
    @Column(name = "order_status")
    private String orderStatus;
    
    @Column(name = "purchase_timestamp")
    private LocalDateTime purchaseTimestamp;
    
    @Column(name = "delivered_timestamp")
    private LocalDateTime deliveredTimestamp;
    
    @Column(name = "estimated_delivery")
    private LocalDateTime estimatedDelivery;
    
    @Column(name = "transit_time_hours")
    private Long transitTimeHours;
    
    @Column(name = "delay_hours")
    private Long delayHours;
    
    @Column(name = "is_delayed")
    private Boolean isDelayed;
}
