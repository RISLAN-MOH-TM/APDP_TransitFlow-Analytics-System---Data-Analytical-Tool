package com.transitflow.adapter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * High-performance stream-based CSV reader parsing records in chunks
 */
public class CsvDatasetAdapter<T> implements DatasetAdapter<T> {
    
    private final Path filePath;
    private final Function<CSVRecord, T> mapper;
    private final String sourceIdentifier;
    
    public CsvDatasetAdapter(Path filePath, Function<CSVRecord, T> mapper) {
        this.filePath = filePath;
        this.mapper = mapper;
        this.sourceIdentifier = filePath.getFileName().toString();
    }
    
    @Override
    public List<List<T>> parseInChunks(int chunkSize) throws IOException {
        List<List<T>> chunks = new ArrayList<>();
        
        try (Reader reader = Files.newBufferedReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .withIgnoreHeaderCase()
                     .withTrim())) {
            
            List<T> currentChunk = new ArrayList<>();
            
            for (CSVRecord record : csvParser) {
                try {
                    T entity = mapper.apply(record);
                    if (entity != null) {
                        currentChunk.add(entity);
                        
                        if (currentChunk.size() >= chunkSize) {
                            chunks.add(new ArrayList<>(currentChunk));
                            currentChunk.clear();
                        }
                    }
                } catch (Exception e) {
                    // Skip malformed records - they will be handled by DLQ in the service layer
                }
            }
            
            // Add remaining records
            if (!currentChunk.isEmpty()) {
                chunks.add(currentChunk);
            }
        }
        
        return chunks;
    }
    
    @Override
    public long getTotalRecords() throws IOException {
        try (Reader reader = Files.newBufferedReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .withIgnoreHeaderCase()
                     .withTrim())) {
            
            return csvParser.stream().count();
        }
    }
    
    @Override
    public String getSourceIdentifier() {
        return sourceIdentifier;
    }
}
