package com.transitflow.repository;

import com.transitflow.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, String> {
    
    List<Delivery> findByIsDelayed(Boolean isDelayed);
    
    @Query("SELECT AVG(d.transitTimeHours) FROM Delivery d WHERE d.transitTimeHours IS NOT NULL")
    Double getAverageTransitTime();
    
    @Query("SELECT d FROM Delivery d WHERE d.isDelayed = true ORDER BY d.delayHours DESC")
    List<Delivery> findDelayedDeliveries();
}
