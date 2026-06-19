package com.transitflow.factory;

import com.transitflow.adapter.CsvDatasetAdapter;
import com.transitflow.adapter.DatasetAdapter;
import com.transitflow.factory.DatasetLoaderFactory;
import com.transitflow.model.*;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 Test Suite for Factory Pattern Implementation
 * 
 * Tests the DatasetLoaderFactory which implements the Factory Design Pattern
 * to create appropriate adapters for different data sources
 * 
 * Design Pattern: Factory Pattern
 * Purpose: Encapsulates object creation logic and provides loose coupling
 * 
 * Test Coverage:
 * - Factory creates correct adapter types
 * - Adapter processes CSV files correctly
 * - Invalid file types are rejected
 * - All entity types supported (Customer, Order, Payment, Product, Seller)
 * 
 * Testing Techniques:
 * - Unit Testing
 * - Design Pattern Testing
 * - Equivalence Partitioning
 */
@DisplayName("Factory Pattern Tests - DatasetLoaderFactory")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FactoryPatternTest {
    
    @TempDir
    Path tempDir;
    
    private Path customerCsvFile;
    private Path orderCsvFile;
    private Path paymentCsvFile;
    private Path productCsvFile;
    private Path sellerCsvFile;
    private Path invalidFile;
    
    @BeforeEach
    void setUp() throws IOException {
        // Create temporary CSV files for testing
        customerCsvFile = createTempCustomerCsv();
        orderCsvFile = createTempOrderCsv();
        paymentCsvFile = createTempPaymentCsv();
        productCsvFile = createTempProductCsv();
        sellerCsvFile = createTempSellerCsv();
        invalidFile = tempDir.resolve("invalid.txt");
        Files.writeString(invalidFile, "This is not a CSV file");
    }
    
    /**
     * Test 1: Factory Pattern - Creates Customer Adapter
     * Verifies factory creates correct adapter type for Customer CSV
     */
    @Test
    @Order(1)
    @DisplayName("Factory should create CustomerAdapter for customer CSV file")
    void testFactoryCreatesCustomerAdapter() {
        // Act
        DatasetAdapter<Customer> adapter = DatasetLoaderFactory.createCustomerLoader(customerCsvFile);
        
        // Assert
        assertNotNull(adapter, "Factory should return non-null adapter");
        assertInstanceOf(CsvDatasetAdapter.class, adapter, "Should create CsvDatasetAdapter instance");
    }
    
    /**
     * Test 2: Factory Pattern - Creates Order Adapter
     * Verifies factory creates correct adapter type for Order CSV
     */
    @Test
    @Order(2)
    @DisplayName("Factory should create OrderAdapter for order CSV file")
    void testFactoryCreatesOrderAdapter() {
        // Act
        DatasetAdapter<com.transitflow.model.Order> adapter = DatasetLoaderFactory.createOrderLoader(orderCsvFile);
        
        // Assert
        assertNotNull(adapter, "Factory should return non-null adapter");
        assertInstanceOf(CsvDatasetAdapter.class, adapter, "Should create CsvDatasetAdapter instance");
    }
    
    /**
     * Test 3: Factory Pattern - Creates Payment Adapter
     * Verifies factory creates correct adapter type for Payment CSV
     */
    @Test
    @Order(3)
    @DisplayName("Factory should create PaymentAdapter for payment CSV file")
    void testFactoryCreatesPaymentAdapter() {
        // Act
        DatasetAdapter<Payment> adapter = DatasetLoaderFactory.createPaymentLoader(paymentCsvFile);
        
        // Assert
        assertNotNull(adapter, "Factory should return non-null adapter");
        assertInstanceOf(CsvDatasetAdapter.class, adapter, "Should create CsvDatasetAdapter instance");
    }
    
    /**
     * Test 4: Factory Pattern - Creates Product Adapter
     * Verifies factory creates correct adapter type for Product CSV
     */
    @Test
    @Order(4)
    @DisplayName("Factory should create ProductAdapter for product CSV file")
    void testFactoryCreatesProductAdapter() {
        // Act
        DatasetAdapter<Product> adapter = DatasetLoaderFactory.createProductLoader(productCsvFile);
        
        // Assert
        assertNotNull(adapter, "Factory should return non-null adapter");
        assertInstanceOf(CsvDatasetAdapter.class, adapter, "Should create CsvDatasetAdapter instance");
    }
    
    /**
     * Test 5: Factory Pattern - Creates Seller Adapter
     * Verifies factory creates correct adapter type for Seller CSV
     */
    @Test
    @Order(5)
    @DisplayName("Factory should create SellerAdapter for seller CSV file")
    void testFactoryCreatesSellerAdapter() {
        // Act
        DatasetAdapter<Seller> adapter = DatasetLoaderFactory.createSellerLoader(sellerCsvFile);
        
        // Assert
        assertNotNull(adapter, "Factory should return non-null adapter");
        assertInstanceOf(CsvDatasetAdapter.class, adapter, "Should create CsvDatasetAdapter instance");
    }
    
    /**
     * Test 6: Factory Pattern - Rejects Invalid File Type
     * Verifies factory throws exception for unsupported file formats
     * Technique: Equivalence Partitioning (Invalid Input)
     */
    @Test
    @Order(6)
    @DisplayName("Factory should reject invalid file types (non-CSV)")
    void testFactoryRejectsInvalidFileType() {
        // Act & Assert
        assertThrows(UnsupportedOperationException.class, () -> {
            DatasetLoaderFactory.createCustomerLoader(invalidFile);
        }, "Factory should throw UnsupportedOperationException for TXT files");
    }
    
    /**
     * Test 7: Adapter Functionality - Parse Customer CSV
     * Verifies created adapter can parse CSV data correctly
     */
    @Test
    @Order(7)
    @DisplayName("Customer adapter should parse CSV data correctly")
    void testCustomerAdapterParsesData() throws IOException {
        // Arrange
        DatasetAdapter<Customer> adapter = DatasetLoaderFactory.createCustomerLoader(customerCsvFile);
        
        // Act
        List<List<Customer>> chunks = adapter.parseInChunks(100);
        
        // Assert
        assertNotNull(chunks, "Chunks should not be null");
        assertFalse(chunks.isEmpty(), "Should have at least one chunk");
        
        List<Customer> firstChunk = chunks.get(0);
        assertFalse(firstChunk.isEmpty(), "First chunk should have data");
        
        Customer firstCustomer = firstChunk.get(0);
        assertEquals("C001", firstCustomer.getCustomerId(), "Customer ID should match");
        assertEquals("São Paulo", firstCustomer.getCustomerCity(), "City should match");
        assertEquals("SP", firstCustomer.getCustomerState(), "State should match");
    }
    
    /**
     * Test 8: Adapter Functionality - Parse Order CSV
     * Verifies order adapter processes data with timestamps
     */
    @Test
    @Order(8)
    @DisplayName("Order adapter should parse CSV with timestamps")
    void testOrderAdapterParsesTimestamps() throws IOException {
        // Arrange
        DatasetAdapter<com.transitflow.model.Order> adapter = DatasetLoaderFactory.createOrderLoader(orderCsvFile);
        
        // Act
        List<List<com.transitflow.model.Order>> chunks = adapter.parseInChunks(100);
        
        // Assert
        assertNotNull(chunks, "Chunks should not be null");
        assertFalse(chunks.isEmpty(), "Should have at least one chunk");
        
        com.transitflow.model.Order firstOrder = chunks.get(0).get(0);
        assertEquals("O001", firstOrder.getOrderId(), "Order ID should match");
        assertEquals("delivered", firstOrder.getOrderStatus(), "Status should match");
        assertNotNull(firstOrder.getOrderPurchaseTimestamp(), "Timestamp should be parsed");
    }
    
    /**
     * Test 9: Adapter Functionality - Parse Payment CSV
     * Verifies payment adapter handles numeric values correctly
     */
    @Test
    @Order(9)
    @DisplayName("Payment adapter should parse numeric payment values")
    void testPaymentAdapterParsesNumericValues() throws IOException {
        // Arrange
        DatasetAdapter<Payment> adapter = DatasetLoaderFactory.createPaymentLoader(paymentCsvFile);
        
        // Act
        List<List<Payment>> chunks = adapter.parseInChunks(100);
        
        // Assert
        assertNotNull(chunks, "Chunks should not be null");
        Payment firstPayment = chunks.get(0).get(0);
        
        assertEquals("O001", firstPayment.getOrderId(), "Order ID should match");
        assertEquals("credit_card", firstPayment.getPaymentType(), "Payment type should match");
        assertEquals(150.50, firstPayment.getPaymentValue(), 0.01, "Payment value should match");
    }
    
    /**
     * Test 10: Factory Pattern Benefits - Loose Coupling
     * Demonstrates that factory provides abstraction and decoupling
     */
    @Test
    @Order(10)
    @DisplayName("Factory provides loose coupling through interface")
    void testFactoryProvidesLooseCoupling() {
        // Arrange & Act
        DatasetAdapter<Customer> adapter = DatasetLoaderFactory.createCustomerLoader(customerCsvFile);
        
        // Assert - Client code works with interface, not concrete implementation
        // This demonstrates the Factory Pattern benefit of loose coupling
        assertTrue(adapter instanceof DatasetAdapter, "Should work with DatasetAdapter interface");
        
        // Client doesn't need to know about CsvDatasetAdapter implementation
        // Factory handles the instantiation logic
    }
    
    // ========================================
    // Helper Methods - Create Test CSV Files
    // ========================================
    
    private Path createTempCustomerCsv() throws IOException {
        Path file = tempDir.resolve("customers.csv");
        String content = """
            customer_id,customer_unique_id,customer_zip_code_prefix,customer_city,customer_state
            C001,U001,01310,São Paulo,SP
            C002,U002,20040,Rio de Janeiro,RJ
            C003,U003,70040,Brasília,DF
            """;
        Files.writeString(file, content);
        return file;
    }
    
    private Path createTempOrderCsv() throws IOException {
        Path file = tempDir.resolve("orders.csv");
        String content = """
            order_id,customer_id,order_status,order_purchase_timestamp,order_approved_at,order_delivered_carrier_date,order_delivered_customer_date,order_estimated_delivery_date
            O001,C001,delivered,2024-01-15 10:30:00,2024-01-15 11:00:00,2024-01-16 09:00:00,2024-01-20 14:30:00,2024-01-22 23:59:59
            O002,C002,delivered,2024-01-16 14:20:00,2024-01-16 15:00:00,2024-01-17 08:30:00,2024-01-21 16:45:00,2024-01-23 23:59:59
            """;
        Files.writeString(file, content);
        return file;
    }
    
    private Path createTempPaymentCsv() throws IOException {
        Path file = tempDir.resolve("payments.csv");
        String content = """
            order_id,payment_sequential,payment_type,payment_installments,payment_value
            O001,1,credit_card,3,150.50
            O002,1,boleto,1,200.00
            O003,1,debit_card,1,99.99
            """;
        Files.writeString(file, content);
        return file;
    }
    
    private Path createTempProductCsv() throws IOException {
        Path file = tempDir.resolve("products.csv");
        String content = """
            product_id,product_category_name,product_name_lenght,product_description_lenght,product_photos_qty,product_weight_g,product_length_cm,product_height_cm,product_width_cm
            P001,electronics,50,500,5,1000,20,15,10
            P002,furniture,40,300,3,5000,100,50,80
            """;
        Files.writeString(file, content);
        return file;
    }
    
    private Path createTempSellerCsv() throws IOException {
        Path file = tempDir.resolve("sellers.csv");
        String content = """
            seller_id,seller_zip_code_prefix,seller_city,seller_state
            S001,01310,São Paulo,SP
            S002,20040,Rio de Janeiro,RJ
            """;
        Files.writeString(file, content);
        return file;
    }
}
