package com.transitflow.strategy.impl;

import com.transitflow.model.Customer;
import com.transitflow.model.Order;
import com.transitflow.model.Payment;
import com.transitflow.repository.CustomerRepository;
import com.transitflow.repository.OrderRepository;
import com.transitflow.repository.PaymentRepository;
import com.transitflow.strategy.AnalyticsStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CustomerSegmentationStrategy implements AnalyticsStrategy {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Override
    public Map<String, Object> execute() {
        log.info("Executing Customer Segmentation Analysis");
        
        Map<String, Object> result = new HashMap<>();
        
        // Geographic distribution - already optimized
        List<Object[]> stateDistribution = customerRepository.countByState();
        Map<String, Long> stateMap = stateDistribution.stream()
            .limit(20)
            .collect(Collectors.toMap(
                arr -> (String) arr[0],
                arr -> (Long) arr[1],
                (a, b) -> a,
                LinkedHashMap::new
            ));
        
        // Optimized: Get order counts in a single query
        log.info("Calculating customer frequency segments...");
        List<Object[]> orderCounts = orderRepository.countOrdersByCustomer();
        
        int oneTimeCustomers = 0;
        int repeatCustomers = 0;
        int frequentCustomers = 0;
        
        for (Object[] row : orderCounts) {
            Long count = (Long) row[1];
            if (count == 1) oneTimeCustomers++;
            else if (count <= 3) repeatCustomers++;
            else frequentCustomers++;
        }
        
        Map<String, Object> frequencySegments = new HashMap<>();
        frequencySegments.put("one_time", oneTimeCustomers);
        frequencySegments.put("repeat", repeatCustomers);
        frequencySegments.put("frequent", frequentCustomers);
        
        result.put("state_distribution", stateMap);
        result.put("frequency_segments", frequencySegments);
        result.put("total_customers", orderCounts.size());
        
        log.info("Customer Segmentation completed: {} customers analyzed", orderCounts.size());
        
        return result;
    }
    
    @Override
    public String getStrategyName() {
        return "CustomerSegmentation";
    }
}
