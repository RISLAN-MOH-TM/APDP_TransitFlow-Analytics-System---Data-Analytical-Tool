package com.transitflow.model;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 Test Suite for Payment Entity Model
 * 
 * Tests payment entity with financial data validation
 */
@DisplayName("Payment Model Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PaymentModelTest {
    
    private Payment payment;
    
    @BeforeEach
    void setUp() {
        payment = new Payment();
    }
    
    @Test
    @Order(1)
    @DisplayName("Should create payment with all fields")
    void testPaymentCreation() {
        // Act
        payment.setOrderId("O001");
        payment.setPaymentSequential(1);
        payment.setPaymentType("credit_card");
        payment.setPaymentInstallments(3);
        payment.setPaymentValue(150.50);
        
        // Assert
        assertEquals("O001", payment.getOrderId());
        assertEquals(1, payment.getPaymentSequential());
        assertEquals("credit_card", payment.getPaymentType());
        assertEquals(3, payment.getPaymentInstallments());
        assertEquals(150.50, payment.getPaymentValue(), 0.01);
    }
    
    @Test
    @Order(2)
    @DisplayName("Should handle different payment types")
    void testPaymentTypes() {
        // Arrange
        String[] paymentTypes = {"credit_card", "boleto", "debit_card", "voucher"};
        
        // Act & Assert
        for (String type : paymentTypes) {
            payment.setPaymentType(type);
            assertEquals(type, payment.getPaymentType());
        }
    }
    
    @Test
    @Order(3)
    @DisplayName("Should validate positive payment values")
    void testPositivePaymentValues() {
        // Arrange
        double[] validValues = {0.01, 1.00, 100.00, 1000.00, 9999.99};
        
        // Act & Assert
        for (double value : validValues) {
            payment.setPaymentValue(value);
            assertTrue(payment.getPaymentValue() > 0, 
                "Payment value should be positive");
        }
    }
    
    @Test
    @Order(4)
    @DisplayName("Should handle installment payments")
    void testInstallmentPayments() {
        // Arrange
        payment.setPaymentValue(300.00);
        payment.setPaymentInstallments(3);
        
        // Act
        double installmentValue = payment.getPaymentValue() / payment.getPaymentInstallments();
        
        // Assert
        assertEquals(100.00, installmentValue, 0.01, 
            "Each installment should be 100.00");
    }
    
    @Test
    @Order(5)
    @DisplayName("Should handle zero value payments")
    void testZeroValuePayment() {
        // Act
        payment.setPaymentValue(0.0);
        
        // Assert
        assertEquals(0.0, payment.getPaymentValue(), 0.01);
    }
    
    @Test
    @Order(6)
    @DisplayName("Should handle large payment values")
    void testLargePaymentValues() {
        // Arrange
        double largeValue = 999999.99;
        
        // Act
        payment.setPaymentValue(largeValue);
        
        // Assert
        assertEquals(largeValue, payment.getPaymentValue(), 0.01);
    }
}
