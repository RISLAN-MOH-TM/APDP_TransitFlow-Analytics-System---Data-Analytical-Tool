package com.transitflow.controller;

import com.transitflow.facade.TransitFlowFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    
    @Autowired
    private TransitFlowFacade facade;
    
    @GetMapping("/peak-delivery")
    public ResponseEntity<Map<String, Object>> getPeakDelivery() {
        try {
            Map<String, Object> result = facade.executeAnalysis("peak_delivery");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error in peak delivery analysis", e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/customer-segmentation")
    public ResponseEntity<Map<String, Object>> getCustomerSegmentation() {
        try {
            Map<String, Object> result = facade.executeAnalysis("customer_segmentation");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error in customer segmentation", e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/delivery-efficiency")
    public ResponseEntity<Map<String, Object>> getDeliveryEfficiency() {
        try {
            Map<String, Object> result = facade.executeAnalysis("delivery_efficiency");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error in delivery efficiency analysis", e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/revenue-analysis")
    public ResponseEntity<Map<String, Object>> getRevenueAnalysis() {
        try {
            Map<String, Object> result = facade.executeAnalysis("revenue_analysis");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error in revenue analysis", e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/anomaly-detection")
    public ResponseEntity<Map<String, Object>> getAnomalyDetection() {
        try {
            Map<String, Object> result = facade.executeAnalysis("anomaly_detection");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error in anomaly detection", e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/seasonal-demand")
    public ResponseEntity<Map<String, Object>> getSeasonalDemand() {
        try {
            Map<String, Object> result = facade.executeAnalysis("seasonal_demand");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error in seasonal demand analysis", e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/regional-performance")
    public ResponseEntity<Map<String, Object>> getRegionalPerformance() {
        try {
            Map<String, Object> result = facade.executeAnalysis("regional_performance");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error in regional performance analysis", e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", e.getMessage()));
        }
    }
}
