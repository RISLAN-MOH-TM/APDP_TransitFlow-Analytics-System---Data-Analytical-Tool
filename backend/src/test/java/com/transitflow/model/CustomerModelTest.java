package com.transitflow.model;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 Test Suite for Customer Entity Model
 * 
 * Tests entity validation, getters/setters, and business logic
 * 
 * Testing Techniques:
 * - Unit Testing
 * - Object-Oriented Testing
 * - Validation Testing
 */
@DisplayName("Customer Model Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerModelTest {
    
    private Customer customer;
    
    @BeforeEach
    void setUp() {
        customer = new Customer();
    }
    
    @AfterEach
    void tearDown() {
        customer = null;
    }
    
    @Test
    @Order(1)
    @DisplayName("Should create customer with default constructor")
    void testDefaultConstructor() {
        // Act
        Customer newCustomer = new Customer();
        
        // Assert
        assertNotNull(newCustomer, "Customer should not be null");
    }
    
    @Test
    @Order(2)
    @DisplayName("Should create customer with all-args constructor")
    void testAllArgsConstructor() {
        // Act
        Customer newCustomer = new Customer(
            "C001",
            "U001",
            "12345",
            "São Paulo",
            "SP"
        );
        
        // Assert
        assertEquals("C001", newCustomer.getCustomerId());
        assertEquals("U001", newCustomer.getCustomerUniqueId());
        assertEquals("12345", newCustomer.getCustomerZipCodePrefix());
        assertEquals("São Paulo", newCustomer.getCustomerCity());
        assertEquals("SP", newCustomer.getCustomerState());
    }
    
    @Test
    @Order(3)
    @DisplayName("Should set and get customer ID")
    void testSetAndGetCustomerId() {
        // Arrange
        String customerId = "C123456";
        
        // Act
        customer.setCustomerId(customerId);
        
        // Assert
        assertEquals(customerId, customer.getCustomerId());
    }
    
    @Test
    @Order(4)
    @DisplayName("Should set and get customer state")
    void testSetAndGetCustomerState() {
        // Arrange
        String state = "RJ";
        
        // Act
        customer.setCustomerState(state);
        
        // Assert
        assertEquals(state, customer.getCustomerState());
    }
    
    @Test
    @Order(5)
    @DisplayName("Should set and get customer city")
    void testSetAndGetCustomerCity() {
        // Arrange
        String city = "Rio de Janeiro";
        
        // Act
        customer.setCustomerCity(city);
        
        // Assert
        assertEquals(city, customer.getCustomerCity());
    }
    
    @Test
    @Order(6)
    @DisplayName("Should test equals and hashCode consistency")
    void testEqualsAndHashCode() {
        // Arrange
        Customer customer1 = new Customer("C001", "U001", "12345", "City", "ST");
        Customer customer2 = new Customer("C001", "U001", "12345", "City", "ST");
        Customer customer3 = new Customer("C002", "U002", "54321", "Other", "OT");
        
        // Assert
        assertEquals(customer1, customer2, "Customers with same data should be equal");
        assertNotEquals(customer1, customer3, "Customers with different data should not be equal");
        assertEquals(customer1.hashCode(), customer2.hashCode(), "Equal customers should have same hashCode");
    }
    
    @Test
    @Order(7)
    @DisplayName("Should test toString method")
    void testToString() {
        // Arrange
        customer.setCustomerId("C001");
        customer.setCustomerCity("São Paulo");
        
        // Act
        String result = customer.toString();
        
        // Assert
        assertNotNull(result, "toString should not return null");
        assertTrue(result.contains("C001"), "toString should contain customer ID");
    }
    
    @Test
    @Order(8)
    @DisplayName("Should handle null values gracefully")
    void testNullValues() {
        // Act
        customer.setCustomerId(null);
        customer.setCustomerCity(null);
        
        // Assert
        assertNull(customer.getCustomerId(), "Should allow null customer ID");
        assertNull(customer.getCustomerCity(), "Should allow null city");
    }
}
