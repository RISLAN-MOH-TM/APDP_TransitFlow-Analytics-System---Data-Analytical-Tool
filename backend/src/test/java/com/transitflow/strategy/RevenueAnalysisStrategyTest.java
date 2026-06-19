package com.transitflow.strategy;

import com.transitflow.model.Payment;
import com.transitflow.repository.OrderRepository;
import com.transitflow.repository.PaymentRepository;
import com.transitflow.strategy.impl.RevenueAnalysisStrategy;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * JUnit 5 Test Suite for RevenueAnalysisStrategy
 * Testing revenue calculations and boundary values
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("RevenueAnalysisStrategy Unit Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RevenueAnalysisStrategyTest {
    
    @Mock
    private PaymentRepository paymentRepository;
    
    @Mock
    private OrderRepository orderRepository;
    
    @InjectMocks
    private RevenueAnalysisStrategy revenueAnalysisStrategy;
    
    @Test
    @Order(1)
    @DisplayName("Should calculate total revenue correctly")
    void testCalculateTotalRevenue() {
        // Arrange
        double expectedRevenue = 15000.00;
        when(paymentRepository.sumAllPayments()).thenReturn(expectedRevenue);
        when(paymentRepository.aggregateByPaymentType()).thenReturn(createMockPaymentTypeData());
        when(paymentRepository.sumRevenueByMonth()).thenReturn(new ArrayList<>());
        when(paymentRepository.count()).thenReturn(100L);
        
        // Act
        Map<String, Object> result = revenueAnalysisStrategy.execute();
        
        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("total_revenue"));
        assertEquals(expectedRevenue, result.get("total_revenue"));
        
        verify(paymentRepository).sumAllPayments();
    }
    
    @Test
    @Order(2)
    @DisplayName("Should handle zero revenue scenario")
    void testZeroRevenue() {
        // Arrange
        when(paymentRepository.sumAllPayments()).thenReturn(0.0);
        when(paymentRepository.aggregateByPaymentType()).thenReturn(new ArrayList<>());
        when(paymentRepository.sumRevenueByMonth()).thenReturn(new ArrayList<>());
        when(paymentRepository.count()).thenReturn(0L);
        
        // Act
        Map<String, Object> result = revenueAnalysisStrategy.execute();
        
        // Assert
        assertNotNull(result);
        assertEquals(0.0, result.get("total_revenue"));
    }
    
    @Test
    @Order(3)
    @DisplayName("Should handle null revenue from database")
    void testNullRevenue() {
        // Arrange
        when(paymentRepository.sumAllPayments()).thenReturn(null);
        when(paymentRepository.aggregateByPaymentType()).thenReturn(new ArrayList<>());
        when(paymentRepository.sumRevenueByMonth()).thenReturn(new ArrayList<>());
        when(paymentRepository.count()).thenReturn(0L);
        
        // Act
        Map<String, Object> result = revenueAnalysisStrategy.execute();
        
        // Assert
        assertNotNull(result);
        assertEquals(0.0, result.get("total_revenue"));
    }
    
    private List<Object[]> createMockPaymentTypeData() {
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{"credit_card", 50L, 7500.00});
        data.add(new Object[]{"boleto", 30L, 4500.00});
        return data;
    }
}
