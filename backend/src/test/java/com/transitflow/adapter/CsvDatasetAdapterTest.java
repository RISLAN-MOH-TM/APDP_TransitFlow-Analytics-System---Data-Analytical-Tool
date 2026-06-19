package com.transitflow.adapter;

import com.transitflow.model.Customer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CSV Dataset Adapter Tests")
class CsvDatasetAdapterTest {

    @TempDir
    Path tempDir;

    private Path csvFile;
    private CsvDatasetAdapter<Customer> adapter;

    @BeforeEach
    void setUp() throws IOException {
        csvFile = tempDir.resolve("customers.csv");
        String content = """
            customer_id,customer_unique_id,customer_zip_code_prefix,customer_city,customer_state
            C001,U001,01310,São Paulo,SP
            C002,U002,20040,Rio de Janeiro,RJ
            C003,U003,70040,Brasília,DF
            """;
        Files.writeString(csvFile, content);
        adapter = new CsvDatasetAdapter<>(csvFile, record -> {
            Customer c = new Customer();
            c.setCustomerId(record.get("customer_id"));
            c.setCustomerUniqueId(record.get("customer_unique_id"));
            c.setCustomerZipCodePrefix(record.get("customer_zip_code_prefix"));
            c.setCustomerCity(record.get("customer_city"));
            c.setCustomerState(record.get("customer_state"));
            return c;
        });
    }

    @Test
    @DisplayName("Should parse CSV file into chunks")
    void testParseInChunks() throws IOException {
        List<List<Customer>> chunks = adapter.parseInChunks(100);
        assertNotNull(chunks);
        assertFalse(chunks.isEmpty());
        assertEquals(3, chunks.get(0).size());
    }

    @Test
    @DisplayName("Should respect chunk size boundaries")
    void testChunkSize() throws IOException {
        List<List<Customer>> chunks = adapter.parseInChunks(2);
        assertEquals(2, chunks.size());
        assertEquals(2, chunks.get(0).size());
        assertEquals(1, chunks.get(1).size());
    }

    @Test
    @DisplayName("Should parse customer fields correctly")
    void testCustomerFieldMapping() throws IOException {
        List<List<Customer>> chunks = adapter.parseInChunks(100);
        Customer first = chunks.get(0).get(0);
        assertEquals("C001", first.getCustomerId());
        assertEquals("São Paulo", first.getCustomerCity());
        assertEquals("SP", first.getCustomerState());
    }

    @Test
    @DisplayName("getTotalRecords should return record count excluding header")
    void testGetTotalRecords() throws IOException {
        long count = adapter.getTotalRecords();
        assertEquals(3, count);
    }

    @Test
    @DisplayName("getSourceIdentifier returns filename")
    void testGetSourceIdentifier() {
        assertEquals("customers.csv", adapter.getSourceIdentifier());
    }

    @Test
    @DisplayName("Should handle empty CSV file")
    void testEmptyCsvFile() throws IOException {
        Path emptyFile = tempDir.resolve("empty.csv");
        Files.writeString(emptyFile, "customer_id,customer_unique_id,customer_zip_code_prefix,customer_city,customer_state\n");
        CsvDatasetAdapter<Customer> emptyAdapter = new CsvDatasetAdapter<>(emptyFile, record -> new Customer());
        List<List<Customer>> chunks = emptyAdapter.parseInChunks(100);
        assertTrue(chunks.isEmpty());
    }

    @Test
    @DisplayName("Should handle non-existent file gracefully")
    void testNonExistentFile() {
        Path missing = tempDir.resolve("missing.csv");
        CsvDatasetAdapter<Customer> badAdapter = new CsvDatasetAdapter<>(missing, record -> new Customer());
        assertThrows(IOException.class, () -> badAdapter.parseInChunks(100));
    }

    @Test
    @DisplayName("Should handle CSV with missing required columns gracefully")
    void testMissingRequiredColumn() throws IOException {
        Path missingCol = tempDir.resolve("missing_col.csv");
        String content = "customer_id,customer_city\nC001,Unknown\n";
        Files.writeString(missingCol, content);
        CsvDatasetAdapter<Customer> adapter = new CsvDatasetAdapter<>(missingCol, record -> {
            Customer c = new Customer();
            c.setCustomerId(record.get("customer_id"));
            c.setCustomerCity(record.get("customer_city"));
            return c;
        });
        List<List<Customer>> chunks = adapter.parseInChunks(100);
        assertEquals(1, chunks.get(0).size());
        assertEquals("C001", chunks.get(0).get(0).getCustomerId());
    }

    @Test
    @DisplayName("Should skip corrupted CSV lines gracefully")
    void testCorruptedCsvLine() throws IOException {
        Path corruptFile = tempDir.resolve("corrupt.csv");
        String content = """
            customer_id,customer_name,customer_state
            C001,Alice,SP
            C002,Charlie,RJ
            """;
        Files.writeString(corruptFile, content);
        CsvDatasetAdapter<Customer> adapter = new CsvDatasetAdapter<>(corruptFile, record -> {
            Customer c = new Customer();
            c.setCustomerId(record.get("customer_id"));
            return c;
        });
        List<List<Customer>> chunks = adapter.parseInChunks(100);
        assertEquals(2, chunks.get(0).size());
    }

    @Test
    @DisplayName("Should trim whitespace in CSV fields")
    void testWhitespaceHandling() throws IOException {
        Path wsFile = tempDir.resolve("whitespace.csv");
        String content = "customer_id,customer_city,customer_state\n C001 ,  São Paulo  , SP \n";
        Files.writeString(wsFile, content);
        CsvDatasetAdapter<Customer> adapter = new CsvDatasetAdapter<>(wsFile, record -> {
            Customer c = new Customer();
            c.setCustomerId(record.get("customer_id"));
            c.setCustomerCity(record.get("customer_city"));
            c.setCustomerState(record.get("customer_state"));
            return c;
        });
        List<List<Customer>> chunks = adapter.parseInChunks(100);
        assertEquals("C001", chunks.get(0).get(0).getCustomerId());
    }
}
