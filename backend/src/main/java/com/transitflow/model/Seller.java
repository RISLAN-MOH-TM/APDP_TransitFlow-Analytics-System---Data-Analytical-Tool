package com.transitflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sellers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seller {
    
    @Id
    @Column(name = "seller_id", nullable = false, unique = true)
    private String sellerId;
    
    @Column(name = "seller_zip_code_prefix")
    private String sellerZipCodePrefix;
    
    @Column(name = "seller_city")
    private String sellerCity;
    
    @Column(name = "seller_state")
    private String sellerState;
}
