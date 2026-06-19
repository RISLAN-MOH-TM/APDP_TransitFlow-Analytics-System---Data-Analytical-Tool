package com.transitflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "order_id")
    private String orderId;
    
    @Column(name = "payment_sequential")
    private Integer paymentSequential;
    
    @Column(name = "payment_type")
    private String paymentType;
    
    @Column(name = "payment_installments")
    private Integer paymentInstallments;
    
    @Column(name = "payment_value")
    private Double paymentValue;
}
