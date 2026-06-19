package com.transitflow.algorithm;

/**
 * Binary Search Algorithm Implementation
 * Provides efficient O(log n) search functionality for sorted arrays.
 */
public class BinarySearch {
    
    /**
     * Performs binary search on a sorted integer array
     * 
     * @param arr sorted array to search in
     * @param target value to search for
     * @return index of target if found, -1 otherwise
     */
    public static int binarySearch(int[] arr, int target) {
        if (arr == null || arr.length == 0) {
            return -1;
        }
        
        int left = 0;
        int right = arr.length - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2; // Prevents overflow
            
            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return -1; // Target not found
    }
    
    /**
     * Performs binary search on a sorted array of comparable objects
     * 
     * @param arr sorted array to search in
     * @param target value to search for
     * @param <T> type that implements Comparable
     * @return index of target if found, -1 otherwise
     */
    public static <T extends Comparable<T>> int binarySearch(T[] arr, T target) {
        if (arr == null || arr.length == 0 || target == null) {
            return -1;
        }
        
        int left = 0;
        int right = arr.length - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int comparison = arr[mid].compareTo(target);
            
            if (comparison == 0) {
                return mid;
            } else if (comparison < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return -1;
    }
}
