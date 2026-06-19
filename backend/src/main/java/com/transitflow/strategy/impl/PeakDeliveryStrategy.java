package com.transitflow.strategy.impl;

import com.transitflow.model.Order;
import com.transitflow.repository.OrderRepository;
import com.transitflow.strategy.AnalyticsStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PeakDeliveryStrategy implements AnalyticsStrategy {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Override
    public Map<String, Object> execute() {
        log.info("Executing Peak Delivery Analysis");
        
        Map<String, Object> result = new HashMap<>();
        List<Order> orders = orderRepository.findAll();
        
        // Hourly trends
        Map<Integer, Long> hourlyDistribution = orders.stream()
            .filter(o -> o.getOrderPurchaseTimestamp() != null)
            .collect(Collectors.groupingBy(
                o -> o.getOrderPurchaseTimestamp().getHour(),
                Collectors.counting()
            ));
        
        // Daily trends
        Map<String, Long> dailyDistribution = orders.stream()
            .filter(o -> o.getOrderPurchaseTimestamp() != null)
            .collect(Collectors.groupingBy(
                o -> o.getOrderPurchaseTimestamp().toLocalDate().toString(),
                Collectors.counting()
            ));
        
        // Day of week trends
        Map<String, Long> dayOfWeekDistribution = orders.stream()
            .filter(o -> o.getOrderPurchaseTimestamp() != null)
            .collect(Collectors.groupingBy(
                o -> o.getOrderPurchaseTimestamp().getDayOfWeek().toString(),
                Collectors.counting()
            ));
        
        result.put("hourly_distribution", hourlyDistribution);
        result.put("daily_distribution", dailyDistribution);
        result.put("day_of_week_distribution", dayOfWeekDistribution);
        result.put("total_orders", orders.size());
        
        return result;
    }
    
    @Override
    public String getStrategyName() {
        return "PeakDeliveryAnalysis";
    }
}
