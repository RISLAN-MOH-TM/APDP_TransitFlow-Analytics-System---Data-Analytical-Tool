package com.transitflow.repository;

import com.transitflow.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SellerRepository extends JpaRepository<Seller, String> {
    
    List<Seller> findBySellerState(String state);
    
    @Query("SELECT s.sellerState, COUNT(s) FROM Seller s GROUP BY s.sellerState ORDER BY COUNT(s) DESC")
    List<Object[]> countByState();
}
