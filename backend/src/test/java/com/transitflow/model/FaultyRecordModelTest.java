package com.transitflow.model;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.*;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FaultyRecord Model Tests")
class FaultyRecordModelTest {

    private FaultyRecord record;

    @BeforeEach
    void setUp() {
        record = new FaultyRecord();
    }

    @Test
    @DisplayName("Should create faulty record with default constructor")
    void testDefaultConstructor() {
        assertNotNull(new FaultyRecord());
    }

    @Test
    @DisplayName("Should create faulty record with all-args constructor")
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        FaultyRecord r = new FaultyRecord(1L, "customers.csv", "raw,data", "Invalid field", now, 42L);
        assertEquals(1L, r.getId());
        assertEquals("customers.csv", r.getSourceFile());
        assertEquals("raw,data", r.getRawContent());
        assertEquals("Invalid field", r.getFailureReason());
        assertEquals(now, r.getTimestamp());
        assertEquals(42L, r.getLineNumber());
    }

    @Test
    @DisplayName("Should set and get ID")
    void testSetAndGetId() {
        record.setId(5L);
        assertEquals(5L, record.getId());
    }

    @Test
    @DisplayName("Should set and get source file")
    void testSetAndGetSourceFile() {
        record.setSourceFile("orders.csv");
        assertEquals("orders.csv", record.getSourceFile());
    }

    @Test
    @DisplayName("Should set and get failure reason")
    void testSetAndGetFailureReason() {
        record.setFailureReason("Missing required field");
        assertEquals("Missing required field", record.getFailureReason());
    }

    @Test
    @DisplayName("Should set and get line number")
    void testSetAndGetLineNumber() {
        record.setLineNumber(100L);
        assertEquals(100L, record.getLineNumber());
    }

    @Test
    @DisplayName("Should handle null timestamp")
    void testNullTimestamp() {
        record.setTimestamp(null);
        assertNull(record.getTimestamp());
    }

    @Test
    @DisplayName("Should test equals and hashCode")
    void testEqualsAndHashCode() {
        FaultyRecord r1 = new FaultyRecord(1L, "f.csv", "raw", "err", null, 1L);
        FaultyRecord r2 = new FaultyRecord(1L, "f.csv", "raw", "err", null, 1L);
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    @DisplayName("Should test toString")
    void testToString() {
        record.setSourceFile("test.csv");
        String str = record.toString();
        assertNotNull(str);
        assertTrue(str.contains("test.csv"));
    }
}
