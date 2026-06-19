package com.transitflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    
    @Id
    @Column(name = "product_id", nullable = false, unique = true)
    private String productId;
    
    @Column(name = "product_category_name")
    private String productCategoryName;
    
    @Column(name = "product_name_length")
    private Integer productNameLength;
    
    @Column(name = "product_description_length")
    private Integer productDescriptionLength;
    
    @Column(name = "product_photos_qty")
    private Integer productPhotosQty;
    
    @Column(name = "product_weight_g")
    private Integer productWeightG;
    
    @Column(name = "product_length_cm")
    private Integer productLengthCm;
    
    @Column(name = "product_height_cm")
    private Integer productHeightCm;
    
    @Column(name = "product_width_cm")
    private Integer productWidthCm;
}
