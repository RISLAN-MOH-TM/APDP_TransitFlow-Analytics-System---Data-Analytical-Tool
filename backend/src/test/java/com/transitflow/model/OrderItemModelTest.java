package com.transitflow.model;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.*;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderItem Model Tests")
class OrderItemModelTest {

    private OrderItem orderItem;

    @BeforeEach
    void setUp() {
        orderItem = new OrderItem();
    }

    @Test
    @DisplayName("Should create order item with default constructor")
    void testDefaultConstructor() {
        assertNotNull(new OrderItem());
    }

    @Test
    @DisplayName("Should create order item with all-args constructor")
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        OrderItem item = new OrderItem(1L, "O001", 1, "P001", "S001", now, 99.99, 15.50);
        assertEquals("O001", item.getOrderId());
        assertEquals("P001", item.getProductId());
        assertEquals(99.99, item.getPrice(), 0.01);
        assertEquals(15.50, item.getFreightValue(), 0.01);
    }

    @Test
    @DisplayName("Should set and get order item fields")
    void testSetAndGet() {
        orderItem.setOrderId("O001");
        orderItem.setOrderItemId(1);
        orderItem.setProductId("P001");
        orderItem.setSellerId("S001");
        orderItem.setPrice(199.99);
        orderItem.setFreightValue(25.00);
        assertEquals("O001", orderItem.getOrderId());
        assertEquals(1, orderItem.getOrderItemId());
        assertEquals("P001", orderItem.getProductId());
        assertEquals(199.99, orderItem.getPrice(), 0.01);
    }

    @Test
    @DisplayName("Should handle null shipping date")
    void testNullShippingDate() {
        orderItem.setShippingLimitDate(null);
        assertNull(orderItem.getShippingLimitDate());
    }

    @Test
    @DisplayName("Should handle zero price")
    void testZeroPrice() {
        orderItem.setPrice(0.0);
        assertEquals(0.0, orderItem.getPrice(), 0.01);
    }

    @Test
    @DisplayName("Should test equals and hashCode")
    void testEqualsAndHashCode() {
        OrderItem item1 = new OrderItem(1L, "O001", 1, "P001", "S001", null, 99.99, 10.0);
        OrderItem item2 = new OrderItem(1L, "O001", 1, "P001", "S001", null, 99.99, 10.0);
        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    @DisplayName("Should test toString")
    void testToString() {
        orderItem.setOrderId("O001");
        String str = orderItem.toString();
        assertNotNull(str);
        assertTrue(str.contains("O001"));
    }
}
