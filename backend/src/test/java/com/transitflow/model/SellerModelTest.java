package com.transitflow.model;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Seller Model Tests")
class SellerModelTest {

    private Seller seller;

    @BeforeEach
    void setUp() {
        seller = new Seller();
    }

    @Test
    @DisplayName("Should create seller with default constructor")
    void testDefaultConstructor() {
        assertNotNull(new Seller());
    }

    @Test
    @DisplayName("Should create seller with all-args constructor")
    void testAllArgsConstructor() {
        Seller s = new Seller("S001", "01310", "São Paulo", "SP");
        assertEquals("S001", s.getSellerId());
        assertEquals("01310", s.getSellerZipCodePrefix());
        assertEquals("São Paulo", s.getSellerCity());
        assertEquals("SP", s.getSellerState());
    }

    @Test
    @DisplayName("Should set and get seller ID")
    void testSetAndGetSellerId() {
        seller.setSellerId("S001");
        assertEquals("S001", seller.getSellerId());
    }

    @Test
    @DisplayName("Should set and get seller city")
    void testSetAndGetSellerCity() {
        seller.setSellerCity("Rio de Janeiro");
        assertEquals("Rio de Janeiro", seller.getSellerCity());
    }

    @Test
    @DisplayName("Should set and get seller state")
    void testSetAndGetSellerState() {
        seller.setSellerState("RJ");
        assertEquals("RJ", seller.getSellerState());
    }

    @Test
    @DisplayName("Should test equals and hashCode")
    void testEqualsAndHashCode() {
        Seller s1 = new Seller("S001", "01310", "SP", "São Paulo");
        Seller s2 = new Seller("S001", "01310", "SP", "São Paulo");
        Seller s3 = new Seller("S002", "20040", "RJ", "Rio");
        assertEquals(s1, s2);
        assertNotEquals(s1, s3);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    @DisplayName("Should handle null values")
    void testNullValues() {
        seller.setSellerId(null);
        seller.setSellerCity(null);
        assertNull(seller.getSellerId());
        assertNull(seller.getSellerCity());
    }

    @Test
    @DisplayName("Should test toString")
    void testToString() {
        seller.setSellerId("S001");
        String str = seller.toString();
        assertNotNull(str);
        assertTrue(str.contains("S001"));
    }
}
