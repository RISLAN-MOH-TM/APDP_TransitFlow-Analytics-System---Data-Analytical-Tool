package com.transitflow.strategy.impl;

import com.transitflow.model.Payment;
import com.transitflow.model.Delivery;
import com.transitflow.repository.PaymentRepository;
import com.transitflow.repository.DeliveryRepository;
import com.transitflow.strategy.AnalyticsStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AnomalyDetectionStrategy implements AnalyticsStrategy {
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private DeliveryRepository deliveryRepository;
    
    @Override
    public Map<String, Object> execute() {
        log.info("Executing Anomaly Detection Analysis");
        
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> anomalies = new ArrayList<>();
        
        // Price anomalies
        List<Payment> payments = paymentRepository.findAll();
        if (!payments.isEmpty()) {
            double avgPayment = payments.stream()
                .mapToDouble(p -> p.getPaymentValue() != null ? p.getPaymentValue() : 0.0)
                .average()
                .orElse(0.0);
            
            double stdDev = calculateStdDev(payments, avgPayment);
            double threshold = avgPayment + (3 * stdDev);
            
            List<Payment> priceAnomalies = payments.stream()
                .filter(p -> p.getPaymentValue() != null && p.getPaymentValue() > threshold)
                .limit(20)
                .toList();
            
            for (Payment p : priceAnomalies) {
                Map<String, Object> anomaly = new HashMap<>();
                anomaly.put("type", "HIGH_PRICE");
                anomaly.put("order_id", p.getOrderId());
                anomaly.put("value", p.getPaymentValue());
                anomaly.put("threshold", threshold);
                anomalies.add(anomaly);
            }
        }
        
        // Delay anomalies
        List<Delivery> deliveries = deliveryRepository.findAll();
        List<Delivery> extremeDelays = deliveries.stream()
            .filter(d -> d.getDelayHours() != null && d.getDelayHours() > 168) // > 1 week
            .limit(20)
            .toList();
        
        for (Delivery d : extremeDelays) {
            Map<String, Object> anomaly = new HashMap<>();
            anomaly.put("type", "EXTREME_DELAY");
            anomaly.put("order_id", d.getOrderId());
            anomaly.put("delay_hours", d.getDelayHours());
            anomaly.put("delay_days", d.getDelayHours() / 24);
            anomalies.add(anomaly);
        }
        
        result.put("anomalies", anomalies);
        result.put("total_anomalies", anomalies.size());
        
        return result;
    }
    
    private double calculateStdDev(List<Payment> payments, double mean) {
        double variance = payments.stream()
            .mapToDouble(p -> {
                double val = p.getPaymentValue() != null ? p.getPaymentValue() : 0.0;
                return Math.pow(val - mean, 2);
            })
            .average()
            .orElse(0.0);
        
        return Math.sqrt(variance);
    }
    
    @Override
    public String getStrategyName() {
        return "AnomalyDetection";
    }
}
