package com.transitflow.strategy.impl;

import com.transitflow.model.Order;
import com.transitflow.repository.OrderRepository;
import com.transitflow.strategy.AnalyticsStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SeasonalDemandStrategy implements AnalyticsStrategy {
    
    @Autowired
    private OrderRepository orderRepository;
    
    // Holiday periods in Brazil (adjust for your region)
    private static final Map<String, List<Integer>> HOLIDAY_PERIODS = Map.of(
        "Christmas", List.of(12, 24, 25),      // December 24-25
        "Black Friday", List.of(11, 20, 27),   // Late November
        "New Year", List.of(12, 31, 1),        // Dec 31 - Jan 1
        "Carnival", List.of(2, 1, 28),         // February
        "Mother's Day", List.of(5, 1, 15),     // Early May
        "Father's Day", List.of(8, 1, 15),     // Early August
        "Children's Day", List.of(10, 12, 12)  // October 12
    );
    
    @Override
    public Map<String, Object> execute() {
        log.info("Executing Seasonal Demand Analysis");
        
        Map<String, Object> result = new HashMap<>();
        List<Order> orders = orderRepository.findAll();
        
        if (orders.isEmpty()) {
            result.put("message", "No order data available");
            return result;
        }
        
        // Monthly order volume
        Map<String, Long> monthlyOrders = calculateMonthlyOrders(orders);
        
        // Month-over-month growth
        Map<String, Double> monthlyGrowth = calculateMonthlyGrowth(monthlyOrders);
        
        // Holiday period analysis
        Map<String, Object> holidayAnalysis = analyzeHolidayPeriods(orders);
        
        // Seasonal patterns
        Map<String, Long> seasonalPatterns = analyzeSeasonalPatterns(orders);
        
        // Identify peak seasons
        List<Map<String, Object>> peakSeasons = identifyPeakSeasons(monthlyOrders);
        
        // Cyclical trends (quarterly)
        Map<String, Long> quarterlyTrends = analyzeQuarterlyTrends(orders);
        
        result.put("monthly_orders", monthlyOrders);
        result.put("monthly_growth_percent", monthlyGrowth);
        result.put("holiday_analysis", holidayAnalysis);
        result.put("seasonal_patterns", seasonalPatterns);
        result.put("peak_seasons", peakSeasons);
        result.put("quarterly_trends", quarterlyTrends);
        result.put("total_orders", orders.size());
        
        return result;
    }
    
    private Map<String, Long> calculateMonthlyOrders(List<Order> orders) {
        return orders.stream()
            .filter(o -> o.getOrderPurchaseTimestamp() != null)
            .collect(Collectors.groupingBy(
                o -> o.getOrderPurchaseTimestamp().getYear() + "-" + 
                     String.format("%02d", o.getOrderPurchaseTimestamp().getMonthValue()),
                TreeMap::new,
                Collectors.counting()
            ));
    }
    
    private Map<String, Double> calculateMonthlyGrowth(Map<String, Long> monthlyOrders) {
        Map<String, Double> growth = new LinkedHashMap<>();
        List<Map.Entry<String, Long>> entries = new ArrayList<>(monthlyOrders.entrySet());
        
        for (int i = 1; i < entries.size(); i++) {
            String currentMonth = entries.get(i).getKey();
            long currentValue = entries.get(i).getValue();
            long previousValue = entries.get(i - 1).getValue();
            
            if (previousValue > 0) {
                double growthPercent = ((currentValue - previousValue) * 100.0) / previousValue;
                growth.put(currentMonth, Math.round(growthPercent * 100.0) / 100.0);
            }
        }
        
        return growth;
    }
    
    private Map<String, Object> analyzeHolidayPeriods(List<Order> orders) {
        Map<String, Object> holidayData = new HashMap<>();
        
        for (Map.Entry<String, List<Integer>> holiday : HOLIDAY_PERIODS.entrySet()) {
            String holidayName = holiday.getKey();
            int month = holiday.getValue().get(0);
            
            long holidayOrders = orders.stream()
                .filter(o -> o.getOrderPurchaseTimestamp() != null)
                .filter(o -> o.getOrderPurchaseTimestamp().getMonthValue() == month)
                .count();
            
            holidayData.put(holidayName, Map.of(
                "month", Month.of(month).name(),
                "order_count", holidayOrders
            ));
        }
        
        return holidayData;
    }
    
    private Map<String, Long> analyzeSeasonalPatterns(List<Order> orders) {
        Map<String, Long> seasons = new LinkedHashMap<>();
        
        Map<String, Long> monthCounts = orders.stream()
            .filter(o -> o.getOrderPurchaseTimestamp() != null)
            .collect(Collectors.groupingBy(
                o -> {
                    int month = o.getOrderPurchaseTimestamp().getMonthValue();
                    if (month >= 3 && month <= 5) return "Spring";
                    else if (month >= 6 && month <= 8) return "Summer";
                    else if (month >= 9 && month <= 11) return "Fall";
                    else return "Winter";
                },
                Collectors.counting()
            ));
        
        seasons.put("Spring", monthCounts.getOrDefault("Spring", 0L));
        seasons.put("Summer", monthCounts.getOrDefault("Summer", 0L));
        seasons.put("Fall", monthCounts.getOrDefault("Fall", 0L));
        seasons.put("Winter", monthCounts.getOrDefault("Winter", 0L));
        
        return seasons;
    }
    
    private List<Map<String, Object>> identifyPeakSeasons(Map<String, Long> monthlyOrders) {
        return monthlyOrders.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(5)
            .map(entry -> Map.<String, Object>of(
                "month", entry.getKey(),
                "order_count", entry.getValue()
            ))
            .collect(Collectors.toList());
    }
    
    private Map<String, Long> analyzeQuarterlyTrends(List<Order> orders) {
        return orders.stream()
            .filter(o -> o.getOrderPurchaseTimestamp() != null)
            .collect(Collectors.groupingBy(
                o -> {
                    int month = o.getOrderPurchaseTimestamp().getMonthValue();
                    int year = o.getOrderPurchaseTimestamp().getYear();
                    int quarter = (month - 1) / 3 + 1;
                    return year + "-Q" + quarter;
                },
                TreeMap::new,
                Collectors.counting()
            ));
    }
    
    @Override
    public String getStrategyName() {
        return "SeasonalDemandAnalysis";
    }
}
