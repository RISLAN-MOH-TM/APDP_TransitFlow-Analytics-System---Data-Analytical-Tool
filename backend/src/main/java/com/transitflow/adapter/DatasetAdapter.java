package com.transitflow.adapter;

import java.io.IOException;
import java.util.List;

/**
 * Strategy interface for parsing different input sources into standard chunk outputs
 */
public interface DatasetAdapter<T> {
    
    /**
     * Parse the dataset in chunks
     * @param chunkSize Number of records per chunk
     * @return List of chunks, where each chunk is a list of entities
     * @throws IOException If parsing fails
     */
    List<List<T>> parseInChunks(int chunkSize) throws IOException;
    
    /**
     * Get the total number of records in the dataset
     * @return Total record count
     */
    long getTotalRecords() throws IOException;
    
    /**
     * Get the source file name or identifier
     * @return Source identifier
     */
    String getSourceIdentifier();
}
