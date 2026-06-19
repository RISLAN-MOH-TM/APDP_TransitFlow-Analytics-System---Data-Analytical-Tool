package com.transitflow.model;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.*;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 Test Suite for Order Entity Model
 * 
 * Tests order entity with timestamp handling and status management
 */
@DisplayName("Order Model Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderModelTest {
    
    private com.transitflow.model.Order order;
    
    @BeforeEach
    void setUp() {
        order = new com.transitflow.model.Order();
    }
    
    @Test
    @Order(1)
    @DisplayName("Should create order with all fields")
    void testOrderCreation() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        
        // Act
        order.setOrderId("O001");
        order.setCustomerId("C001");
        order.setOrderStatus("delivered");
        order.setOrderPurchaseTimestamp(now);
        
        // Assert
        assertEquals("O001", order.getOrderId());
        assertEquals("C001", order.getCustomerId());
        assertEquals("delivered", order.getOrderStatus());
        assertEquals(now, order.getOrderPurchaseTimestamp());
    }
    
    @Test
    @Order(2)
    @DisplayName("Should handle different order statuses")
    void testOrderStatuses() {
        // Arrange
        String[] validStatuses = {"delivered", "shipped", "processing", "canceled"};
        
        // Act & Assert
        for (String status : validStatuses) {
            order.setOrderStatus(status);
            assertEquals(status, order.getOrderStatus());
        }
    }
    
    @Test
    @Order(3)
    @DisplayName("Should set and get order timestamps")
    void testOrderTimestamps() {
        // Arrange
        LocalDateTime purchaseTime = LocalDateTime.of(2024, 1, 15, 10, 30);
        LocalDateTime approvedTime = LocalDateTime.of(2024, 1, 15, 11, 0);
        LocalDateTime deliveredTime = LocalDateTime.of(2024, 1, 20, 14, 30);
        
        // Act
        order.setOrderPurchaseTimestamp(purchaseTime);
        order.setOrderApprovedAt(approvedTime);
        order.setOrderDeliveredCustomerDate(deliveredTime);
        
        // Assert
        assertEquals(purchaseTime, order.getOrderPurchaseTimestamp());
        assertEquals(approvedTime, order.getOrderApprovedAt());
        assertEquals(deliveredTime, order.getOrderDeliveredCustomerDate());
    }
    
    @Test
    @Order(4)
    @DisplayName("Should validate delivery timeline")
    void testDeliveryTimeline() {
        // Arrange
        LocalDateTime purchaseTime = LocalDateTime.of(2024, 1, 15, 10, 0);
        LocalDateTime deliveredTime = LocalDateTime.of(2024, 1, 20, 14, 0);
        
        order.setOrderPurchaseTimestamp(purchaseTime);
        order.setOrderDeliveredCustomerDate(deliveredTime);
        
        // Act & Assert
        assertTrue(deliveredTime.isAfter(purchaseTime), 
            "Delivered time should be after purchase time");
    }
    
    @Test
    @Order(5)
    @DisplayName("Should handle null timestamps")
    void testNullTimestamps() {
        // Act
        order.setOrderPurchaseTimestamp(null);
        order.setOrderDeliveredCustomerDate(null);
        
        // Assert
        assertNull(order.getOrderPurchaseTimestamp());
        assertNull(order.getOrderDeliveredCustomerDate());
    }
}
