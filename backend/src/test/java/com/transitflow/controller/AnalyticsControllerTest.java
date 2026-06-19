package com.transitflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transitflow.facade.TransitFlowFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Map;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Analytics Controller Tests")
class AnalyticsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransitFlowFacade facade;

    @InjectMocks
    private AnalyticsController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("GET /api/analytics/peak-delivery returns 200")
    void testPeakDelivery() throws Exception {
        when(facade.executeAnalysis("peak_delivery")).thenReturn(Map.of("total_orders", 5000));
        mockMvc.perform(get("/api/analytics/peak-delivery"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.total_orders").value(5000));
    }

    @Test
    @DisplayName("GET /api/analytics/customer-segmentation returns 200")
    void testCustomerSegmentation() throws Exception {
        when(facade.executeAnalysis("customer_segmentation")).thenReturn(Map.of("total_customers", 1000));
        mockMvc.perform(get("/api/analytics/customer-segmentation"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.total_customers").value(1000));
    }

    @Test
    @DisplayName("GET /api/analytics/delivery-efficiency returns 200")
    void testDeliveryEfficiency() throws Exception {
        when(facade.executeAnalysis("delivery_efficiency")).thenReturn(Map.of("on_time_rate", 92.5));
        mockMvc.perform(get("/api/analytics/delivery-efficiency"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.on_time_rate").value(92.5));
    }

    @Test
    @DisplayName("GET /api/analytics/revenue-analysis returns 200")
    void testRevenueAnalysis() throws Exception {
        when(facade.executeAnalysis("revenue_analysis")).thenReturn(Map.of("total_revenue", 1250000.50));
        mockMvc.perform(get("/api/analytics/revenue-analysis"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.total_revenue").value(1250000.50));
    }

    @Test
    @DisplayName("GET /api/analytics/anomaly-detection returns 200")
    void testAnomalyDetection() throws Exception {
        when(facade.executeAnalysis("anomaly_detection")).thenReturn(Map.of("total_anomalies", 25));
        mockMvc.perform(get("/api/analytics/anomaly-detection"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.total_anomalies").value(25));
    }

    @Test
    @DisplayName("GET /api/analytics/seasonal-demand returns 200")
    void testSeasonalDemand() throws Exception {
        when(facade.executeAnalysis("seasonal_demand")).thenReturn(Map.of("total_orders", 5000));
        mockMvc.perform(get("/api/analytics/seasonal-demand"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.total_orders").value(5000));
    }

    @Test
    @DisplayName("GET /api/analytics/regional-performance returns 200")
    void testRegionalPerformance() throws Exception {
        when(facade.executeAnalysis("regional_performance")).thenReturn(Map.of("total_deliveries", 4800));
        mockMvc.perform(get("/api/analytics/regional-performance"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.total_deliveries").value(4800));
    }

    @Test
    @DisplayName("GET /api/analytics/peak-delivery returns 500 on facade error")
    void testAnalyticsInternalError() throws Exception {
        when(facade.executeAnalysis("peak_delivery")).thenThrow(new RuntimeException("DB error"));
        mockMvc.perform(get("/api/analytics/peak-delivery"))
            .andExpect(status().isInternalServerError());
    }
}
