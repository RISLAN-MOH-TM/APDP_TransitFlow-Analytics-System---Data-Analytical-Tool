package com.transitflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    
    @Id
    @Column(name = "customer_id", nullable = false, unique = true)
    private String customerId;
    
    @Column(name = "customer_unique_id")
    private String customerUniqueId;
    
    @Column(name = "customer_zip_code_prefix")
    private String customerZipCodePrefix;
    
    @Column(name = "customer_city")
    private String customerCity;
    
    @Column(name = "customer_state")
    private String customerState;
}
