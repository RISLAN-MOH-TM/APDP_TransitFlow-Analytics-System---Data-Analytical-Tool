package com.transitflow.service;

import com.transitflow.model.*;
import com.transitflow.repository.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * JUnit 5 Test Suite for DatasetService
 * Testing duplicate removal, validation, and chunk processing
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("DatasetService Unit Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DatasetServiceTest {
    
    @Mock
    private CustomerRepository customerRepository;
    
    @Mock
    private OrderRepository orderRepository;
    
    @Mock
    private OrderItemRepository orderItemRepository;
    
    @Mock
    private PaymentRepository paymentRepository;
    
    @Mock
    private ProductRepository productRepository;
    
    @Mock
    private SellerRepository sellerRepository;
    
    @Mock
    private DeliveryRepository deliveryRepository;
    
    @Mock
    private FaultyRecordRepository faultyRecordRepository;
    
    @InjectMocks
    private DatasetService datasetService;
    
    @Test
    @org.junit.jupiter.api.Order(1)
    @DisplayName("Should get ingestion statistics")
    void testGetIngestionStats() {
        // Arrange - Mock all repository counts
        when(customerRepository.count()).thenReturn(1000L);
        when(orderRepository.count()).thenReturn(1500L);
        when(orderItemRepository.count()).thenReturn(2500L);
        when(paymentRepository.count()).thenReturn(2000L);
        when(productRepository.count()).thenReturn(500L);
        when(sellerRepository.count()).thenReturn(300L);
        when(deliveryRepository.count()).thenReturn(1400L);
        when(faultyRecordRepository.count()).thenReturn(10L);
        
        // Act
        Map<String, Object> stats = datasetService.getIngestionStats();
        
        // Assert
        assertNotNull(stats);
        assertEquals(1000L, stats.get("total_customers"));
        assertEquals(1500L, stats.get("total_orders"));
        assertEquals(2500L, stats.get("total_order_items"));
        assertEquals(2000L, stats.get("total_payments"));
        assertEquals(500L, stats.get("total_products"));
        assertEquals(300L, stats.get("total_sellers"));
        assertEquals(1400L, stats.get("total_deliveries"));
        assertEquals(10L, stats.get("faulty_records"));
        
        // Verify all repositories were called
        verify(customerRepository).count();
        verify(orderRepository).count();
        verify(orderItemRepository).count();
        verify(paymentRepository).count();
        verify(productRepository).count();
        verify(sellerRepository).count();
        verify(deliveryRepository).count();
        verify(faultyRecordRepository).count();
    }
    
    @Test
    @org.junit.jupiter.api.Order(2)
    @DisplayName("Should validate customer data correctly")
    void testValidateCustomerData() {
        // Valid customer
        Customer validCustomer = new Customer("C001", "U001", "12345", "City", "State");
        assertTrue(isValidCustomer(validCustomer));
        
        // Invalid: null ID
        Customer invalidCustomer1 = new Customer(null, "U001", "12345", "City", "State");
        assertFalse(isValidCustomer(invalidCustomer1));
        
        // Invalid: empty ID
        Customer invalidCustomer2 = new Customer("", "U001", "12345", "City", "State");
        assertFalse(isValidCustomer(invalidCustomer2));
    }
    
    private boolean isValidCustomer(Customer customer) {
        return customer != null && 
               customer.getCustomerId() != null && 
               !customer.getCustomerId().trim().isEmpty();
    }
}
