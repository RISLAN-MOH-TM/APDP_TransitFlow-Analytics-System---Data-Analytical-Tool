package com.transitflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * TransitFlow Analytics Backend - Main Application Entry Point
 * 
 * PURPOSE:
 * This is the main Spring Boot application that provides enterprise-level logistics 
 * and e-commerce analytics for large-scale datasets. It processes CSV data and 
 * provides REST APIs for analytics insights.
 * 
 * ARCHITECTURE OVERVIEW:
 * - Uses Spring Boot 3.3.5 with Java 21 (compatible with Java 24 runtime)
 * - Follows layered architecture: Controller -> Facade -> Service -> Repository
 * - Uses Strategy Pattern for analytics implementations
 * - Uses Adapter Pattern for dataset loading
 * - Uses Factory Pattern for adapter creation
 * - In-memory H2 database for high-performance data processing
 * 
 * KEY FEATURES:
 * - Chunk-based ingestion (5,000 records per chunk)
 * - Automatic duplicate removal
 * - Data validation and cleansing
 * - Dead-Letter Queue for faulty records
 * - Memory-efficient streaming
 * - CORS enabled for frontend integration
 * 
 * FUTURE UPGRADE CONSIDERATIONS:
 * 1. DATABASE: Replace H2 with PostgreSQL/MySQL for persistent storage
 * 2. SCALABILITY: Add Redis for caching frequently accessed analytics
 * 3. ASYNC PROCESSING: Implement message queue (RabbitMQ/Kafka) for large datasets
 * 4. SECURITY: Add Spring Security with JWT authentication
 * 5. MONITORING: Integrate Actuator endpoints for health checks
 * 6. API VERSIONING: Add /v1/, /v2/ versioning for backward compatibility
 * 7. RATE LIMITING: Implement API rate limiting for production use
 * 8. BATCH PROCESSING: Use Spring Batch for scheduled data imports
 * 9. MICROSERVICES: Split into separate services (Ingestion, Analytics, Reporting)
 * 10. CONTAINERIZATION: Add Docker support for easy deployment
 * 
 * @author TransitFlow Team
 * @version 1.0.0
 * @since 2026-06-02
 */
@SpringBootApplication
public class TransitFlowApplication {
    
    /**
     * Main method - Entry point for Spring Boot application
     * 
     * @param args Command line arguments (not used currently)
     */
    public static void main(String[] args) {
        SpringApplication.run(TransitFlowApplication.class, args);
    }
}
