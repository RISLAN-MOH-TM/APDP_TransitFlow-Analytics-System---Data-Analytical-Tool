# TransitFlow Analytics Backend

High-performance Spring Boot backend for processing large-scale logistics and e-commerce datasets.

## Features

- **Chunk-Based Processing:** Memory-efficient ingestion of 50MB+ CSV files
- **In-Memory H2 Database:** Fast SQL queries without external DB setup
- **Design Patterns:** Factory, Strategy, Adapter, and Facade patterns
- **Data Quality:** Duplicate removal, validation, and Dead-Letter Queue (DLQ)
- **REST APIs:** JSON endpoints for all analytics modules

## Prerequisites

- Java 21+
- Maven 3.8+

## Quick Start

### 1. Build the Project

```bash
cd backend
mvnw clean install
```

### 2. Run the Application

```bash
mvnw spring-boot:run
```

The backend will start on **http://localhost:8080**

### 3. Verify It's Running

Open your browser and navigate to:
- H2 Console: http://localhost:8080/h2-console
- Stats API: http://localhost:8080/api/dataset/stats

## API Endpoints

### Dataset Management

**POST** `/api/dataset/ingest`
- Ingest datasets from a local directory
- Body: `{"directory_path": "C:\\path\\to\\datasets"}`
- Returns ingestion statistics and metrics

**GET** `/api/dataset/stats`
- Get current database statistics

### Analytics

**GET** `/api/analytics/peak-delivery`
- Hourly, daily, and weekly delivery trends

**GET** `/api/analytics/customer-segmentation`
- Geographic and behavioral customer segments

**GET** `/api/analytics/delivery-efficiency`
- Transit times, delays, and bottlenecks

**GET** `/api/analytics/revenue-analysis`
- Revenue trends and payment analysis

**GET** `/api/analytics/anomaly-detection`
- Outlier detection for prices and delays

## Architecture

```
com.transitflow
├── adapter/           # Adapter pattern for CSV parsing
├── controller/        # REST API controllers
├── facade/           # Facade pattern for service aggregation
├── factory/          # Factory pattern for data loaders
├── model/            # JPA entities
├── repository/       # Spring Data repositories
├── service/          # Business logic and processing
└── strategy/         # Strategy pattern for analytics
    └── impl/         # Analytics implementations
```

## Configuration

Edit `src/main/resources/application.properties`:

```properties
server.port=8080
spring.datasource.url=jdbc:h2:mem:transitflowdb
spring.jpa.hibernate.ddl-auto=create-drop
spring.servlet.multipart.max-file-size=100MB
```

## Testing

Run unit tests:

```bash
mvnw test
```

## Performance Tuning

- Chunk size: Adjust `CHUNK_SIZE` in `DatasetService.java`
- JVM heap: Set in `application.properties` or command line
- Batch processing: Configured via Hibernate properties

## Troubleshooting

**Out of Memory Errors:**
- Increase JVM heap: `java -Xmx2G -jar app.jar`
- Reduce chunk size in `DatasetService`

**Port Already in Use:**
- Change port in `application.properties`
- Or: `mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081`
