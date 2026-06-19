# TransitFlow Backend - Quick Upgrade Guide

## 🚀 Common Upgrade Scenarios

This guide provides code snippets for common upgrade scenarios.

---

## 1️⃣ Adding a New Analytics Strategy

### Step 1: Create Strategy Class
**Location**: `src/main/java/com/transitflow/strategy/impl/YourNewStrategy.java`

```java
package com.transitflow.strategy.impl;

import com.transitflow.strategy.AnalyticsStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;

/**
 * Your New Strategy - Brief description
 * 
 * PURPOSE: Explain what this strategy analyzes
 * 
 * ALGORITHM: Describe the algorithm used
 * 
 * PERFORMANCE: Note any performance considerations
 */
@Slf4j
@Component
public class YourNewStrategy implements AnalyticsStrategy {
    
    @Autowired
    private YourRepository repository;
    
    @Override
    public Map<String, Object> execute() {
        log.info("Executing Your New Analysis");
        
        Map<String, Object> result = new HashMap<>();
        
        // Your analysis logic here
        // Use aggregated queries to avoid N+1 problems!
        
        return result;
    }
    
    @Override
    public String getStrategyName() {
        return "YourNewStrategy";
    }
}
```

### Step 2: Register in Facade
**Location**: `src/main/java/com/transitflow/facade/TransitFlowFacade.java`

```java
@Autowired
private YourNewStrategy yourNewStrategy;

// Add to switch statement in executeAnalysis():
case "your_new_strategy" -> yourNewStrategy;
```

### Step 3: Add Controller Endpoint
**Location**: `src/main/java/com/transitflow/controller/AnalyticsController.java`

```java
@GetMapping("/your-new-strategy")
public ResponseEntity<Map<String, Object>> getYourNewAnalysis() {
    try {
        Map<String, Object> result = facade.executeAnalysis("your_new_strategy");
        return ResponseEntity.ok(result);
    } catch (Exception e) {
        log.error("Error in your new analysis", e);
        return ResponseEntity.internalServerError()
            .body(Map.of("error", e.getMessage()));
    }
}
```

---

## 2️⃣ Adding a New Entity/Table

### Step 1: Create Model Class
**Location**: `src/main/java/com/transitflow/model/YourEntity.java`

```java
package com.transitflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * YourEntity - Represents [description]
 * 
 * PURPOSE: Stores [what data]
 * 
 * RELATIONSHIPS:
 * - [Entity1] (N:1) - Foreign key description
 * - [Entity2] (1:N) - Relationship description
 */
@Entity
@Table(name = "your_table_name")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class YourEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "field_name", nullable = false)
    private String fieldName;
    
    // Add more fields as needed
}
```

### Step 2: Create Repository
**Location**: `src/main/java/com/transitflow/repository/YourRepository.java`

```java
package com.transitflow.repository;

import com.transitflow.model.YourEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository for YourEntity
 * 
 * PERFORMANCE TIP: Use aggregated queries instead of findAll() + loops
 */
@Repository
public interface YourRepository extends JpaRepository<YourEntity, Long> {
    
    // Custom query methods
    List<YourEntity> findByFieldName(String fieldName);
    
    // Aggregated query example (BEST PRACTICE)
    @Query("SELECT y.fieldName, COUNT(y) FROM YourEntity y GROUP BY y.fieldName")
    List<Object[]> countByFieldName();
}
```

---

## 3️⃣ Switching from H2 to PostgreSQL

### Step 1: Update pom.xml
Remove H2 dependency, add PostgreSQL:

```xml
<!-- Remove this -->
<!--
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
-->

<!-- Add this -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

### Step 2: Update application.properties
**Location**: `src/main/resources/application.properties`

```properties
# PostgreSQL Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/transitflow_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# Connection Pool
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
```

### Step 3: Create Database
```sql
CREATE DATABASE transitflow_db;
CREATE USER your_username WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE transitflow_db TO your_username;
```

---

## 4️⃣ Adding Redis Caching

### Step 1: Add Dependency
**Location**: `pom.xml`

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```

### Step 2: Enable Caching
**Location**: `TransitFlowApplication.java`

```java
@SpringBootApplication
@EnableCaching  // Add this
public class TransitFlowApplication {
    // ...
}
```

### Step 3: Configure Redis
**Location**: `application.properties`

```properties
# Redis Configuration
spring.redis.host=localhost
spring.redis.port=6379
spring.cache.type=redis
spring.cache.redis.time-to-live=3600000
```

### Step 4: Add Cache Annotations
**Location**: Strategy classes

```java
@Override
@Cacheable(value = "analytics", key = "'peak_delivery'")
public Map<String, Object> execute() {
    // This result will be cached
}
```

---

## 5️⃣ Adding Spring Security with JWT

### Step 1: Add Dependencies
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.9.1</version>
</dependency>
```

### Step 2: Create Security Config
**Location**: `src/main/java/com/transitflow/config/SecurityConfig.java`

```java
package com.transitflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/**").authenticated()
            )
            .httpBasic();
        
        return http.build();
    }
}
```

---

## 6️⃣ Adding Async Processing

### Step 1: Enable Async
**Location**: `TransitFlowApplication.java`

```java
@SpringBootApplication
@EnableAsync  // Add this
public class TransitFlowApplication {
    // ...
}
```

### Step 2: Make Method Async
```java
@Async
public CompletableFuture<Map<String, Object>> executeAnalysis(String strategyName) {
    Map<String, Object> result = // ... execute analysis
    return CompletableFuture.completedFuture(result);
}
```

---

## 7️⃣ Adding API Versioning

### Current Structure
```
/api/dataset/ingest
/api/analytics/peak-delivery
```

### New Versioned Structure
```
/api/v1/dataset/ingest
/api/v1/analytics/peak-delivery
/api/v2/analytics/peak-delivery  (improved version)
```

### Update Controllers
```java
@RestController
@RequestMapping("/api/v1/analytics")  // Add version
public class AnalyticsController {
    // ...
}
```

---

## 8️⃣ Adding Pagination

### Update Repository
```java
public interface YourRepository extends JpaRepository<YourEntity, Long>,
                                        PagingAndSortingRepository<YourEntity, Long> {
    
    Page<YourEntity> findAll(Pageable pageable);
}
```

### Update Controller
```java
@GetMapping
public ResponseEntity<Page<YourEntity>> getAll(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size) {
    
    Pageable pageable = PageRequest.of(page, size);
    Page<YourEntity> result = repository.findAll(pageable);
    return ResponseEntity.ok(result);
}
```

---

## 9️⃣ Adding Swagger/OpenAPI Documentation

### Add Dependency
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.0.2</version>
</dependency>
```

### Access Documentation
After starting the application:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

### Add API Descriptions
```java
@Operation(summary = "Get peak delivery analysis", 
           description = "Returns hourly, daily, and weekly peak times")
@ApiResponse(responseCode = "200", description = "Success")
@GetMapping("/peak-delivery")
public ResponseEntity<Map<String, Object>> getPeakDelivery() {
    // ...
}
```

---

## 🔟 Performance Monitoring with Actuator

### Add Dependency
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

### Configure Endpoints
**Location**: `application.properties`

```properties
# Actuator Configuration
management.endpoints.web.exposure.include=health,info,metrics,prometheus
management.endpoint.health.show-details=always
management.metrics.export.prometheus.enabled=true
```

### Access Endpoints
- Health: `http://localhost:8080/actuator/health`
- Metrics: `http://localhost:8080/actuator/metrics`
- Prometheus: `http://localhost:8080/actuator/prometheus`

---

## ⚠️ Common Pitfalls to Avoid

### ❌ N+1 Query Problem
```java
// BAD - 10,000 database queries
for (Order order : orders) {
    Double revenue = paymentRepository.sumByOrderId(order.getId());
}
```

```java
// GOOD - 1 database query
List<Object[]> revenues = paymentRepository.sumRevenueGroupedByOrder();
```

### ❌ Loading All Data into Memory
```java
// BAD - May cause OutOfMemoryError
List<Order> allOrders = orderRepository.findAll();
```

```java
// GOOD - Use pagination or streaming
Page<Order> orders = orderRepository.findAll(PageRequest.of(0, 1000));
```

### ❌ Missing Transaction Boundaries
```java
// BAD - Each save is a separate transaction
for (Customer customer : customers) {
    customerRepository.save(customer);
}
```

```java
// GOOD - Single transaction
@Transactional
public void saveAll(List<Customer> customers) {
    customerRepository.saveAll(customers);
}
```

---

## 📚 Testing New Features

### Unit Test Template
```java
@SpringBootTest
class YourStrategyTest {
    
    @Autowired
    private YourStrategy strategy;
    
    @Test
    void testExecute() {
        Map<String, Object> result = strategy.execute();
        assertNotNull(result);
        assertTrue(result.containsKey("expected_key"));
    }
}
```

---

## 🆘 Troubleshooting

### Problem: Port 8080 already in use
**Solution**: Kill process or change port in `application.properties`
```properties
server.port=8081
```

### Problem: JAVA_HOME not set
**Solution**: Set environment variable
```bash
# Windows
set JAVA_HOME=C:\Program Files\Java\jdk-24

# Linux/Mac
export JAVA_HOME=/usr/lib/jvm/java-24
```

### Problem: Lombok not working
**Solution**: 
1. Enable annotation processing in IDE
2. Ensure Lombok plugin is installed
3. Check Maven imported Lombok dependency

---

**Last Updated**: 2026-06-02  
**Version**: 1.0.0
