package com.transitflow.algorithm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests for Binary Search Algorithm
 * Purpose: Validates that the Binary Search algorithm correctly returns 
 * the expected index position for various test scenarios.
 */
@DisplayName("Binary Search Algorithm Tests")
public class BinarySearchTest {
    
    @Test
    @DisplayName("Test binary search finds element at correct index")
    public void testBinarySearchFindsElement() {
        // Arrange
        int[] data = {1, 2, 3, 4, 5};
        int target = 4;
        int expectedIndex = 3;
        
        // Act
        int actualIndex = BinarySearch.binarySearch(data, target);
        
        // Assert
        assertEquals(expectedIndex, actualIndex, 
            "Binary search should find element 4 at index 3");
    }
    
    @Test
    @DisplayName("Test binary search finds first element")
    public void testBinarySearchFindsFirstElement() {
        // Arrange
        int[] data = {1, 2, 3, 4, 5};
        int target = 1;
        int expectedIndex = 0;
        
        // Act
        int actualIndex = BinarySearch.binarySearch(data, target);
        
        // Assert
        assertEquals(expectedIndex, actualIndex);
    }
    
    @Test
    @DisplayName("Test binary search finds last element")
    public void testBinarySearchFindsLastElement() {
        // Arrange
        int[] data = {1, 2, 3, 4, 5};
        int target = 5;
        int expectedIndex = 4;
        
        // Act
        int actualIndex = BinarySearch.binarySearch(data, target);
        
        // Assert
        assertEquals(expectedIndex, actualIndex);
    }
    
    @Test
    @DisplayName("Test binary search returns -1 for missing element")
    public void testBinarySearchElementNotFound() {
        // Arrange
        int[] data = {1, 2, 3, 4, 5};
        int target = 10;
        
        // Act
        int actualIndex = BinarySearch.binarySearch(data, target);
        
        // Assert
        assertEquals(-1, actualIndex, 
            "Binary search should return -1 when element is not found");
    }
    
    @Test
    @DisplayName("Test binary search with empty array")
    public void testBinarySearchEmptyArray() {
        // Arrange
        int[] data = {};
        int target = 1;
        
        // Act
        int actualIndex = BinarySearch.binarySearch(data, target);
        
        // Assert
        assertEquals(-1, actualIndex);
    }
    
    @Test
    @DisplayName("Test binary search with null array")
    public void testBinarySearchNullArray() {
        // Arrange
        int[] data = null;
        int target = 1;
        
        // Act
        int actualIndex = BinarySearch.binarySearch(data, target);
        
        // Assert
        assertEquals(-1, actualIndex);
    }
    
    @Test
    @DisplayName("Test binary search with single element - found")
    public void testBinarySearchSingleElementFound() {
        // Arrange
        int[] data = {5};
        int target = 5;
        
        // Act
        int actualIndex = BinarySearch.binarySearch(data, target);
        
        // Assert
        assertEquals(0, actualIndex);
    }
    
    @Test
    @DisplayName("Test binary search with single element - not found")
    public void testBinarySearchSingleElementNotFound() {
        // Arrange
        int[] data = {5};
        int target = 3;
        
        // Act
        int actualIndex = BinarySearch.binarySearch(data, target);
        
        // Assert
        assertEquals(-1, actualIndex);
    }
    
    @Test
    @DisplayName("Test binary search with large dataset")
    public void testBinarySearchLargeDataset() {
        // Arrange
        int[] data = new int[1000];
        for (int i = 0; i < data.length; i++) {
            data[i] = i * 2; // Even numbers: 0, 2, 4, 6, ...
        }
        int target = 500;
        int expectedIndex = 250;
        
        // Act
        int actualIndex = BinarySearch.binarySearch(data, target);
        
        // Assert
        assertEquals(expectedIndex, actualIndex);
    }
    
    @Test
    @DisplayName("Test binary search with String array")
    public void testBinarySearchWithStrings() {
        // Arrange
        String[] data = {"apple", "banana", "cherry", "date", "elderberry"};
        String target = "cherry";
        int expectedIndex = 2;
        
        // Act
        int actualIndex = BinarySearch.binarySearch(data, target);
        
        // Assert
        assertEquals(expectedIndex, actualIndex);
    }
    
    @Test
    @DisplayName("Test binary search with negative numbers")
    public void testBinarySearchWithNegativeNumbers() {
        // Arrange
        int[] data = {-50, -20, -10, 0, 10, 20, 50};
        int target = -10;
        int expectedIndex = 2;
        
        // Act
        int actualIndex = BinarySearch.binarySearch(data, target);
        
        // Assert
        assertEquals(expectedIndex, actualIndex);
    }
}
