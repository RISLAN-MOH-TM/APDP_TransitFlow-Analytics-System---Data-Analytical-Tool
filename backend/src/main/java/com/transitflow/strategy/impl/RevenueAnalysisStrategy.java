package com.transitflow.strategy.impl;

import com.transitflow.model.Order;
import com.transitflow.model.Payment;
import com.transitflow.repository.OrderRepository;
import com.transitflow.repository.PaymentRepository;
import com.transitflow.strategy.AnalyticsStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RevenueAnalysisStrategy implements AnalyticsStrategy {
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Override
    public Map<String, Object> execute() {
        log.info("Executing Revenue Analysis");
        
        Map<String, Object> result = new HashMap<>();
        
        // Total revenue - optimized with single query
        Double totalRevenue = paymentRepository.sumAllPayments();
        if (totalRevenue == null) totalRevenue = 0.0;
        
        // Payment type distribution - already optimized
        List<Object[]> paymentTypeData = paymentRepository.aggregateByPaymentType();
        Map<String, Object> paymentTypeAnalysis = new HashMap<>();
        
        for (Object[] data : paymentTypeData) {
            String type = (String) data[0];
            Long count = (Long) data[1];
            Double sum = (Double) data[2];
            
            paymentTypeAnalysis.put(type, Map.of(
                "count", count,
                "total_value", sum != null ? sum : 0.0
            ));
        }
        
        // Monthly revenue trends - optimized with single query
        log.info("Calculating monthly revenue trends...");
        List<Object[]> monthlyData = paymentRepository.sumRevenueByMonth();
        Map<String, Double> monthlyRevenue = new TreeMap<>();
        
        for (Object[] data : monthlyData) {
            String month = (String) data[0];
            Double revenue = (Double) data[1];
            if (month != null && revenue != null) {
                monthlyRevenue.put(month, revenue);
            }
        }
        
        long totalPayments = paymentRepository.count();
        
        result.put("total_revenue", totalRevenue);
        result.put("payment_type_analysis", paymentTypeAnalysis);
        result.put("monthly_revenue", monthlyRevenue);
        result.put("average_order_value", totalPayments > 0 ? totalRevenue / totalPayments : 0);
        
        log.info("Revenue Analysis completed: Total revenue R$ {}", totalRevenue);
        
        return result;
    }
    
    @Override
    public String getStrategyName() {
        return "RevenueAnalysis";
    }
}
