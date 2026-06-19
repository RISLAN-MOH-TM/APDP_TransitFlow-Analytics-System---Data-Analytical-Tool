package com.transitflow.repository;

import com.transitflow.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    
    @Query("SELECT p.productCategoryName, COUNT(p) FROM Product p WHERE p.productCategoryName IS NOT NULL GROUP BY p.productCategoryName ORDER BY COUNT(p) DESC")
    List<Object[]> countByCategory();
}
