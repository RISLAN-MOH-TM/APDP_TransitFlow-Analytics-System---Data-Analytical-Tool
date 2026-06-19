package com.transitflow.facade;

import com.transitflow.service.DatasetService;
import com.transitflow.strategy.impl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TransitFlow Facade Tests")
class TransitFlowFacadeTest {

    @Mock private DatasetService datasetService;
    @Mock private PeakDeliveryStrategy peakDeliveryStrategy;
    @Mock private CustomerSegmentationStrategy customerSegmentationStrategy;
    @Mock private DeliveryEfficiencyStrategy deliveryEfficiencyStrategy;
    @Mock private RevenueAnalysisStrategy revenueAnalysisStrategy;
    @Mock private AnomalyDetectionStrategy anomalyDetectionStrategy;
    @Mock private SeasonalDemandStrategy seasonalDemandStrategy;
    @Mock private RegionalPerformanceStrategy regionalPerformanceStrategy;

    private TransitFlowFacade facade;

    @BeforeEach
    void setUp() {
        facade = new TransitFlowFacade();
        injectField(facade, "datasetService", datasetService);
        injectField(facade, "peakDeliveryStrategy", peakDeliveryStrategy);
        injectField(facade, "customerSegmentationStrategy", customerSegmentationStrategy);
        injectField(facade, "deliveryEfficiencyStrategy", deliveryEfficiencyStrategy);
        injectField(facade, "revenueAnalysisStrategy", revenueAnalysisStrategy);
        injectField(facade, "anomalyDetectionStrategy", anomalyDetectionStrategy);
        injectField(facade, "seasonalDemandStrategy", seasonalDemandStrategy);
        injectField(facade, "regionalPerformanceStrategy", regionalPerformanceStrategy);
    }

    private void injectField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject " + fieldName, e);
        }
    }

    @Test
    @DisplayName("executeAnalysis routes to PeakDeliveryStrategy")
    void testExecutePeakDelivery() {
        when(peakDeliveryStrategy.execute()).thenReturn(Map.of("total_orders", 5000));
        assertEquals(5000, facade.executeAnalysis("peak_delivery").get("total_orders"));
    }

    @Test
    @DisplayName("executeAnalysis routes to CustomerSegmentationStrategy")
    void testExecuteCustomerSegmentation() {
        when(customerSegmentationStrategy.execute()).thenReturn(Map.of("total_customers", 1000));
        assertEquals(1000, facade.executeAnalysis("customer_segmentation").get("total_customers"));
    }

    @Test
    @DisplayName("executeAnalysis routes to DeliveryEfficiencyStrategy")
    void testExecuteDeliveryEfficiency() {
        when(deliveryEfficiencyStrategy.execute()).thenReturn(Map.of("on_time_rate", 92.5));
        assertEquals(92.5, facade.executeAnalysis("delivery_efficiency").get("on_time_rate"));
    }

    @Test
    @DisplayName("executeAnalysis routes to RevenueAnalysisStrategy")
    void testExecuteRevenueAnalysis() {
        when(revenueAnalysisStrategy.execute()).thenReturn(Map.of("total_revenue", 1250000.0));
        assertEquals(1250000.0, facade.executeAnalysis("revenue_analysis").get("total_revenue"));
    }

    @Test
    @DisplayName("executeAnalysis routes to AnomalyDetectionStrategy")
    void testExecuteAnomalyDetection() {
        when(anomalyDetectionStrategy.execute()).thenReturn(Map.of("total_anomalies", 25));
        assertEquals(25, facade.executeAnalysis("anomaly_detection").get("total_anomalies"));
    }

    @Test
    @DisplayName("executeAnalysis routes to SeasonalDemandStrategy")
    void testExecuteSeasonalDemand() {
        when(seasonalDemandStrategy.execute()).thenReturn(Map.of("total_orders", 5000));
        assertEquals(5000, facade.executeAnalysis("seasonal_demand").get("total_orders"));
    }

    @Test
    @DisplayName("executeAnalysis routes to RegionalPerformanceStrategy")
    void testExecuteRegionalPerformance() {
        when(regionalPerformanceStrategy.execute()).thenReturn(Map.of("total_deliveries", 4800));
        assertEquals(4800, facade.executeAnalysis("regional_performance").get("total_deliveries"));
    }

    @Test
    @DisplayName("executeAnalysis throws for unknown strategy")
    void testExecuteUnknownStrategy() {
        assertThrows(IllegalArgumentException.class, () -> facade.executeAnalysis("unknown"));
    }

    @Test
    @DisplayName("ingestDatasets delegates to DatasetService and returns results")
    void testIngestDatasets() {
        Map<String, Object> expected = Map.of("customers_total", 1000, "orders_total", 5000);
        when(datasetService.ingestFromDirectory("/data/csv")).thenReturn(expected);
        Map<String, Object> result = facade.ingestDatasets("/data/csv");
        assertEquals(expected, result);
        verify(datasetService).ingestFromDirectory("/data/csv");
    }
}
