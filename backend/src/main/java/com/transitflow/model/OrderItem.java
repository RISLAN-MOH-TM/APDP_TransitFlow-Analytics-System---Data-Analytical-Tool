package com.transitflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "order_id")
    private String orderId;
    
    @Column(name = "order_item_id")
    private Integer orderItemId;
    
    @Column(name = "product_id")
    private String productId;
    
    @Column(name = "seller_id")
    private String sellerId;
    
    @Column(name = "shipping_limit_date")
    private LocalDateTime shippingLimitDate;
    
    @Column(name = "price")
    private Double price;
    
    @Column(name = "freight_value")
    private Double freightValue;
}
