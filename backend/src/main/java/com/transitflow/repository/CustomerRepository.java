package com.transitflow.repository;

import com.transitflow.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    
    List<Customer> findByCustomerState(String state);
    
    @Query("SELECT c.customerState, COUNT(c) FROM Customer c GROUP BY c.customerState ORDER BY COUNT(c) DESC")
    List<Object[]> countByState();
    
    @Query("SELECT c.customerCity, COUNT(c) FROM Customer c GROUP BY c.customerCity ORDER BY COUNT(c) DESC")
    List<Object[]> countByCity();
}
