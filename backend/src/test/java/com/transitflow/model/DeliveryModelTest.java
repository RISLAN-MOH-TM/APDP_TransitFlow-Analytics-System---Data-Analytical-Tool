package com.transitflow.model;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 Test Suite for Delivery Entity Model
 * 
 * Tests delivery metrics and delay calculations
 */
@DisplayName("Delivery Model Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DeliveryModelTest {
    
    private Delivery delivery;
    
    @BeforeEach
    void setUp() {
        delivery = new Delivery();
    }
    
    @Test
    @Order(1)
    @DisplayName("Should create delivery with all fields")
    void testDeliveryCreation() {
        // Arrange
        LocalDateTime purchaseTime = LocalDateTime.now().minusDays(5);
        LocalDateTime deliveredTime = LocalDateTime.now();
        
        // Act
        delivery.setOrderId("O001");
        delivery.setCustomerId("C001");
        delivery.setPurchaseTimestamp(purchaseTime);
        delivery.setDeliveredTimestamp(deliveredTime);
        delivery.setTransitTimeHours(120L);
        delivery.setIsDelayed(false);
        
        // Assert
        assertEquals("O001", delivery.getOrderId());
        assertEquals("C001", delivery.getCustomerId());
        assertEquals(120L, delivery.getTransitTimeHours());
        assertFalse(delivery.getIsDelayed());
    }
    
    @Test
    @Order(2)
    @DisplayName("Should calculate transit time correctly")
    void testTransitTimeCalculation() {
        // Arrange
        LocalDateTime purchaseTime = LocalDateTime.of(2024, 1, 15, 10, 0);
        LocalDateTime deliveredTime = LocalDateTime.of(2024, 1, 20, 14, 0);
        
        delivery.setPurchaseTimestamp(purchaseTime);
        delivery.setDeliveredTimestamp(deliveredTime);
        
        // Act
        long calculatedHours = ChronoUnit.HOURS.between(purchaseTime, deliveredTime);
        delivery.setTransitTimeHours(calculatedHours);
        
        // Assert
        assertTrue(delivery.getTransitTimeHours() > 0, "Transit time should be positive");
        assertEquals(124L, delivery.getTransitTimeHours(), "Should be 5 days + 4 hours");
    }
    
    @Test
    @Order(3)
    @DisplayName("Should detect delayed deliveries")
    void testDelayDetection() {
        // Arrange
        LocalDateTime estimatedDelivery = LocalDateTime.of(2024, 1, 20, 23, 59);
        LocalDateTime actualDelivery = LocalDateTime.of(2024, 1, 22, 10, 0);
        
        delivery.setEstimatedDelivery(estimatedDelivery);
        delivery.setDeliveredTimestamp(actualDelivery);
        
        // Act
        long delayHours = ChronoUnit.HOURS.between(estimatedDelivery, actualDelivery);
        delivery.setDelayHours(delayHours);
        delivery.setIsDelayed(delayHours > 0);
        
        // Assert
        assertTrue(delivery.getIsDelayed(), "Should be marked as delayed");
        assertTrue(delivery.getDelayHours() > 0, "Delay hours should be positive");
    }
    
    @Test
    @Order(4)
    @DisplayName("Should mark on-time deliveries correctly")
    void testOnTimeDelivery() {
        // Arrange
        LocalDateTime estimatedDelivery = LocalDateTime.of(2024, 1, 25, 23, 59);
        LocalDateTime actualDelivery = LocalDateTime.of(2024, 1, 20, 14, 0);
        
        delivery.setEstimatedDelivery(estimatedDelivery);
        delivery.setDeliveredTimestamp(actualDelivery);
        
        // Act
        long delayHours = ChronoUnit.HOURS.between(estimatedDelivery, actualDelivery);
        delivery.setDelayHours(delayHours);
        delivery.setIsDelayed(delayHours > 0);
        
        // Assert
        assertFalse(delivery.getIsDelayed(), "Should not be marked as delayed");
        assertTrue(delivery.getDelayHours() < 0, "Delay hours should be negative (early delivery)");
    }
}
