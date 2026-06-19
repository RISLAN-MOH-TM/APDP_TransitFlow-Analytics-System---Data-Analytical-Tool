package com.transitflow.strategy.impl;

import com.transitflow.model.Customer;
import com.transitflow.model.Delivery;
import com.transitflow.model.Order;
import com.transitflow.model.Payment;
import com.transitflow.repository.*;
import com.transitflow.strategy.AnalyticsStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RegionalPerformanceStrategy implements AnalyticsStrategy {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private DeliveryRepository deliveryRepository;
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private SellerRepository sellerRepository;
    
    @Override
    public Map<String, Object> execute() {
        log.info("Executing Regional Performance Analysis");
        
        Map<String, Object> result = new HashMap<>();
        
        // Get all data
        List<Customer> customers = customerRepository.findAll();
        List<Order> orders = orderRepository.findAll();
        List<Delivery> deliveries = deliveryRepository.findAll();
        List<Payment> payments = paymentRepository.findAll();
        
        // Regional customer distribution
        Map<String, Long> customersByRegion = calculateCustomersByRegion(customers);
        
        // Regional order volume
        Map<String, Long> ordersByRegion = calculateOrdersByRegion(orders, customers);
        
        // Regional revenue
        Map<String, Double> revenueByRegion = calculateRevenueByRegion(orders, payments, customers);
        
        // Regional delivery success rate
        Map<String, Object> deliveryPerformanceByRegion = calculateDeliveryPerformance(deliveries, customers);
        
        // Regional efficiency metrics
        Map<String, Object> efficiencyByRegion = calculateRegionalEfficiency(deliveries, customers);
        
        // Regional rankings
        List<Map<String, Object>> regionalRankings = generateRegionalRankings(
            customersByRegion, ordersByRegion, revenueByRegion, deliveryPerformanceByRegion
        );
        
        // Top performing regions
        List<String> topRegions = regionalRankings.stream()
            .limit(10)
            .map(r -> (String) r.get("state"))
            .collect(Collectors.toList());
        
        // Bottom performing regions
        List<String> bottomRegions = regionalRankings.stream()
            .skip(Math.max(0, regionalRankings.size() - 5))
            .map(r -> (String) r.get("state"))
            .collect(Collectors.toList());
        
        result.put("customers_by_region", customersByRegion);
        result.put("orders_by_region", ordersByRegion);
        result.put("revenue_by_region", revenueByRegion);
        result.put("delivery_performance_by_region", deliveryPerformanceByRegion);
        result.put("efficiency_by_region", efficiencyByRegion);
        result.put("regional_rankings", regionalRankings);
        result.put("top_performing_regions", topRegions);
        result.put("bottom_performing_regions", bottomRegions);
        result.put("total_regions", customersByRegion.size());
        
        return result;
    }
    
    private Map<String, Long> calculateCustomersByRegion(List<Customer> customers) {
        return customers.stream()
            .filter(c -> c.getCustomerState() != null)
            .collect(Collectors.groupingBy(
                Customer::getCustomerState,
                Collectors.counting()
            ));
    }
    
    private Map<String, Long> calculateOrdersByRegion(List<Order> orders, List<Customer> customers) {
        Map<String, String> customerStateMap = customers.stream()
            .filter(c -> c.getCustomerState() != null)
            .collect(Collectors.toMap(
                Customer::getCustomerId,
                Customer::getCustomerState,
                (a, b) -> a
            ));
        
        return orders.stream()
            .filter(o -> o.getCustomerId() != null)
            .filter(o -> customerStateMap.containsKey(o.getCustomerId()))
            .collect(Collectors.groupingBy(
                o -> customerStateMap.get(o.getCustomerId()),
                Collectors.counting()
            ));
    }
    
    private Map<String, Double> calculateRevenueByRegion(
            List<Order> orders, 
            List<Payment> payments, 
            List<Customer> customers) {
        
        Map<String, String> customerStateMap = customers.stream()
            .filter(c -> c.getCustomerState() != null)
            .collect(Collectors.toMap(
                Customer::getCustomerId,
                Customer::getCustomerState,
                (a, b) -> a
            ));
        
        Map<String, Double> orderPaymentMap = payments.stream()
            .filter(p -> p.getOrderId() != null && p.getPaymentValue() != null)
            .collect(Collectors.groupingBy(
                Payment::getOrderId,
                Collectors.summingDouble(Payment::getPaymentValue)
            ));
        
        Map<String, Double> revenueByState = new HashMap<>();
        
        for (Order order : orders) {
            if (order.getCustomerId() != null && customerStateMap.containsKey(order.getCustomerId())) {
                String state = customerStateMap.get(order.getCustomerId());
                Double revenue = orderPaymentMap.getOrDefault(order.getOrderId(), 0.0);
                revenueByState.merge(state, revenue, Double::sum);
            }
        }
        
        return revenueByState;
    }
    
    private Map<String, Object> calculateDeliveryPerformance(
            List<Delivery> deliveries, 
            List<Customer> customers) {
        
        Map<String, String> customerStateMap = customers.stream()
            .filter(c -> c.getCustomerState() != null)
            .collect(Collectors.toMap(
                Customer::getCustomerId,
                Customer::getCustomerState,
                (a, b) -> a
            ));
        
        Map<String, List<Delivery>> deliveriesByState = deliveries.stream()
            .filter(d -> d.getCustomerId() != null)
            .filter(d -> customerStateMap.containsKey(d.getCustomerId()))
            .collect(Collectors.groupingBy(
                d -> customerStateMap.get(d.getCustomerId())
            ));
        
        Map<String, Object> performance = new HashMap<>();
        
        for (Map.Entry<String, List<Delivery>> entry : deliveriesByState.entrySet()) {
            String state = entry.getKey();
            List<Delivery> stateDeliveries = entry.getValue();
            
            long totalDeliveries = stateDeliveries.size();
            long onTimeDeliveries = stateDeliveries.stream()
                .filter(d -> d.getIsDelayed() != null && !d.getIsDelayed())
                .count();
            
            double successRate = totalDeliveries > 0 
                ? (onTimeDeliveries * 100.0 / totalDeliveries) 
                : 0.0;
            
            performance.put(state, Map.of(
                "total_deliveries", totalDeliveries,
                "on_time_deliveries", onTimeDeliveries,
                "success_rate_percent", Math.round(successRate * 100.0) / 100.0
            ));
        }
        
        return performance;
    }
    
    private Map<String, Object> calculateRegionalEfficiency(
            List<Delivery> deliveries, 
            List<Customer> customers) {
        
        Map<String, String> customerStateMap = customers.stream()
            .filter(c -> c.getCustomerState() != null)
            .collect(Collectors.toMap(
                Customer::getCustomerId,
                Customer::getCustomerState,
                (a, b) -> a
            ));
        
        Map<String, List<Delivery>> deliveriesByState = deliveries.stream()
            .filter(d -> d.getCustomerId() != null)
            .filter(d -> customerStateMap.containsKey(d.getCustomerId()))
            .filter(d -> d.getTransitTimeHours() != null)
            .collect(Collectors.groupingBy(
                d -> customerStateMap.get(d.getCustomerId())
            ));
        
        Map<String, Object> efficiency = new HashMap<>();
        
        for (Map.Entry<String, List<Delivery>> entry : deliveriesByState.entrySet()) {
            String state = entry.getKey();
            List<Delivery> stateDeliveries = entry.getValue();
            
            double avgTransitTime = stateDeliveries.stream()
                .mapToLong(Delivery::getTransitTimeHours)
                .average()
                .orElse(0.0);
            
            efficiency.put(state, Map.of(
                "average_transit_hours", Math.round(avgTransitTime * 100.0) / 100.0,
                "delivery_count", stateDeliveries.size()
            ));
        }
        
        return efficiency;
    }
    
    private List<Map<String, Object>> generateRegionalRankings(
            Map<String, Long> customersByRegion,
            Map<String, Long> ordersByRegion,
            Map<String, Double> revenueByRegion,
            Map<String, Object> deliveryPerformance) {
        
        List<Map<String, Object>> rankings = new ArrayList<>();
        
        for (String state : customersByRegion.keySet()) {
            Map<String, Object> regionData = new HashMap<>();
            regionData.put("state", state);
            regionData.put("customers", customersByRegion.getOrDefault(state, 0L));
            regionData.put("orders", ordersByRegion.getOrDefault(state, 0L));
            regionData.put("revenue", Math.round(revenueByRegion.getOrDefault(state, 0.0) * 100.0) / 100.0);
            
            if (deliveryPerformance.containsKey(state)) {
                @SuppressWarnings("unchecked")
                Map<String, Object> perfData = (Map<String, Object>) deliveryPerformance.get(state);
                regionData.put("success_rate", perfData.get("success_rate_percent"));
            } else {
                regionData.put("success_rate", 0.0);
            }
            
            // Calculate composite score
            double score = calculateCompositeScore(regionData);
            regionData.put("performance_score", Math.round(score * 100.0) / 100.0);
            
            rankings.add(regionData);
        }
        
        // Sort by performance score
        rankings.sort((a, b) -> Double.compare(
            (Double) b.get("performance_score"), 
            (Double) a.get("performance_score")
        ));
        
        return rankings;
    }
    
    private double calculateCompositeScore(Map<String, Object> regionData) {
        // Weighted composite score
        long customers = (Long) regionData.get("customers");
        long orders = (Long) regionData.get("orders");
        double revenue = (Double) regionData.get("revenue");
        double successRate = (Double) regionData.get("success_rate");
        
        // Normalize and weight
        double customerScore = Math.log(customers + 1) * 0.2;
        double orderScore = Math.log(orders + 1) * 0.3;
        double revenueScore = Math.log(revenue + 1) * 0.3;
        double successScore = successRate * 0.2;
        
        return customerScore + orderScore + revenueScore + successScore;
    }
    
    @Override
    public String getStrategyName() {
        return "RegionalPerformanceAnalysis";
    }
}
