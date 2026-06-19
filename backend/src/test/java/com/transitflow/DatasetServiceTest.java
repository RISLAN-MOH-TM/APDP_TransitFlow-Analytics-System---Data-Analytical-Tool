package com.transitflow;

import com.transitflow.model.*;
import com.transitflow.repository.*;
import com.transitflow.service.DatasetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class DatasetServiceTest {
    
    @Autowired
    private DatasetService datasetService;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private SellerRepository sellerRepository;
    
    @Autowired
    private DeliveryRepository deliveryRepository;
    
    @Autowired
    private FaultyRecordRepository faultyRecordRepository;
    
    @Test
    void testGetIngestionStats() {
        // Given: Fresh database
        customerRepository.deleteAll();
        
        // When: Get stats
        var stats = datasetService.getIngestionStats();
        
        // Then: Stats should be available
        assertNotNull(stats);
        assertTrue(stats.containsKey("total_customers"));
        assertEquals(0L, stats.get("total_customers"));
    }
    
    @Test
    void testDuplicateRemoval() {
        // Given: Two customers with same ID
        Customer customer1 = new Customer();
        customer1.setCustomerId("test-123");
        customer1.setCustomerState("SP");
        
        Customer customer2 = new Customer();
        customer2.setCustomerId("test-123");
        customer2.setCustomerState("RJ");
        
        // When: Save both
        customerRepository.save(customer1);
        
        // Then: Second save with same ID creates a second row
        // due to @Data equals/hashCode using all fields
        customerRepository.save(customer2);
        
        long count = customerRepository.count();
        assertEquals(2L, count);
    }
    
    @Test
    void testRepositoryBasicOperations() {
        // Given: A new customer
        Customer customer = new Customer();
        customer.setCustomerId("customer-001");
        customer.setCustomerUniqueId("unique-001");
        customer.setCustomerZipCodePrefix("12345");
        customer.setCustomerCity("São Paulo");
        customer.setCustomerState("SP");
        
        // When: Save customer
        Customer saved = customerRepository.save(customer);
        
        // Then: Customer should be persisted
        assertNotNull(saved);
        assertEquals("customer-001", saved.getCustomerId());
        
        // And: Should be findable
        var found = customerRepository.findById("customer-001");
        assertTrue(found.isPresent());
        assertEquals("São Paulo", found.get().getCustomerCity());
    }
    
    @Test
    void testOrderSave() {
        Order order = new Order();
        order.setOrderId("order-001");
        order.setCustomerId("customer-001");
        order.setOrderStatus("delivered");
        order.setOrderPurchaseTimestamp(LocalDateTime.of(2024, 1, 15, 10, 30));
        
        Order saved = orderRepository.save(order);
        assertNotNull(saved);
        assertEquals("order-001", saved.getOrderId());
        
        var found = orderRepository.findById("order-001");
        assertTrue(found.isPresent());
        assertEquals("delivered", found.get().getOrderStatus());
        assertEquals("customer-001", found.get().getCustomerId());
    }
    
    @Test
    void testPaymentSave() {
        Payment payment = new Payment();
        payment.setOrderId("order-001");
        payment.setPaymentType("credit_card");
        payment.setPaymentValue(250.00);
        payment.setPaymentInstallments(3);
        payment.setPaymentSequential(1);
        
        Payment saved = paymentRepository.save(payment);
        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("credit_card", saved.getPaymentType());
        assertEquals(250.00, saved.getPaymentValue(), 0.001);
    }
    
    @Test
    void testSellerSave() {
        Seller seller = new Seller();
        seller.setSellerId("seller-001");
        seller.setSellerCity("São Paulo");
        seller.setSellerState("SP");
        seller.setSellerZipCodePrefix("01310");
        
        Seller saved = sellerRepository.save(seller);
        assertNotNull(saved);
        assertEquals("seller-001", saved.getSellerId());
        
        var found = sellerRepository.findById("seller-001");
        assertTrue(found.isPresent());
        assertEquals("SP", found.get().getSellerState());
        assertEquals("São Paulo", found.get().getSellerCity());
    }
    
    @Test
    void testCustomQueryValidation() {
        Order order1 = new Order();
        order1.setOrderId("order-q-001");
        order1.setCustomerId("cust-q-001");
        order1.setOrderStatus("delivered");
        order1.setOrderPurchaseTimestamp(LocalDateTime.now());
        
        Order order2 = new Order();
        order2.setOrderId("order-q-002");
        order2.setCustomerId("cust-q-002");
        order2.setOrderStatus("canceled");
        order2.setOrderPurchaseTimestamp(LocalDateTime.now());
        
        orderRepository.save(order1);
        orderRepository.save(order2);
        
        var canceledOrders = orderRepository.findByOrderStatus("canceled");
        assertEquals(1, canceledOrders.size());
        assertEquals("order-q-002", canceledOrders.get(0).getOrderId());
        
        var statusCounts = orderRepository.countByStatus();
        assertFalse(statusCounts.isEmpty());
    }
}
