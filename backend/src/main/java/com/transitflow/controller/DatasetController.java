package com.transitflow.controller;

import com.transitflow.facade.TransitFlowFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/dataset")
public class DatasetController {
    
    @Autowired
    private TransitFlowFacade facade;
    
    @PostMapping("/ingest")
    public ResponseEntity<Map<String, Object>> ingestDataset(@RequestBody Map<String, String> request) {
        try {
            String directoryPath = request.get("directory_path");
            
            if (directoryPath == null || directoryPath.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "directory_path is required"));
            }
            
            log.info("Received ingestion request for: {}", directoryPath);
            Map<String, Object> result = facade.ingestDatasets(directoryPath);
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            log.error("Error during ingestion", e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        try {
            Map<String, Object> stats = facade.getIngestionStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error getting stats", e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", e.getMessage()));
        }
    }
}
