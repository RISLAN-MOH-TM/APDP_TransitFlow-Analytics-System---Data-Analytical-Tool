package com.transitflow.model;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 Test Suite for Product Entity Model
 * 
 * Tests product entity with dimension and weight validation
 */
@DisplayName("Product Model Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductModelTest {
    
    private Product product;
    
    @BeforeEach
    void setUp() {
        product = new Product();
    }
    
    @Test
    @Order(1)
    @DisplayName("Should create product with all dimensions")
    void testProductCreation() {
        // Act
        product.setProductId("P001");
        product.setProductCategoryName("electronics");
        product.setProductWeightG(1000);
        product.setProductLengthCm(20);
        product.setProductHeightCm(15);
        product.setProductWidthCm(10);
        
        // Assert
        assertEquals("P001", product.getProductId());
        assertEquals("electronics", product.getProductCategoryName());
        assertEquals(1000, product.getProductWeightG());
        assertEquals(20, product.getProductLengthCm());
        assertEquals(15, product.getProductHeightCm());
        assertEquals(10, product.getProductWidthCm());
    }
    
    @Test
    @Order(2)
    @DisplayName("Should calculate product volume")
    void testProductVolumeCalculation() {
        // Arrange
        product.setProductLengthCm(20);
        product.setProductHeightCm(15);
        product.setProductWidthCm(10);
        
        // Act
        int volume = product.getProductLengthCm() * 
                     product.getProductHeightCm() * 
                     product.getProductWidthCm();
        
        // Assert
        assertEquals(3000, volume, "Volume should be 20 * 15 * 10");
    }
    
    @Test
    @Order(3)
    @DisplayName("Should handle different product categories")
    void testProductCategories() {
        // Arrange
        String[] categories = {"electronics", "furniture", "clothing", "books"};
        
        // Act & Assert
        for (String category : categories) {
            product.setProductCategoryName(category);
            assertEquals(category, product.getProductCategoryName());
        }
    }
}
