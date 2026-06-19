package com.transitflow.strategy.impl;

import com.transitflow.model.Delivery;
import com.transitflow.repository.DeliveryRepository;
import com.transitflow.strategy.AnalyticsStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;

@Slf4j
@Component
public class DeliveryEfficiencyStrategy implements AnalyticsStrategy {
    
    @Autowired
    private DeliveryRepository deliveryRepository;
    
    @Override
    public Map<String, Object> execute() {
        log.info("Executing Delivery Efficiency Analysis");
        
        Map<String, Object> result = new HashMap<>();
        
        // Average transit time
        Double avgTransitTime = deliveryRepository.getAverageTransitTime();
        
        // Delayed deliveries
        List<Delivery> delayedDeliveries = deliveryRepository.findDelayedDeliveries();
        
        // Total deliveries
        long totalDeliveries = deliveryRepository.count();
        long delayedCount = delayedDeliveries.size();
        double delayRate = totalDeliveries > 0 ? (delayedCount * 100.0 / totalDeliveries) : 0;
        
        // Delay distribution
        Map<String, Long> delayDistribution = new HashMap<>();
        delayDistribution.put("on_time", totalDeliveries - delayedCount);
        delayDistribution.put("delayed", delayedCount);
        
        result.put("average_transit_time_hours", avgTransitTime != null ? avgTransitTime : 0);
        result.put("total_deliveries", totalDeliveries);
        result.put("delayed_deliveries", delayedCount);
        result.put("delay_rate_percent", delayRate);
        result.put("delay_distribution", delayDistribution);
        result.put("top_delays", delayedDeliveries.stream()
            .limit(10)
            .map(d -> Map.of(
                "order_id", d.getOrderId(),
                "delay_hours", d.getDelayHours()
            ))
            .toList()
        );
        
        return result;
    }
    
    @Override
    public String getStrategyName() {
        return "DeliveryEfficiency";
    }
}
