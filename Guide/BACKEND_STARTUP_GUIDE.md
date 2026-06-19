# TransitFlow Backend - Startup Guide

## ✅ Backend Status: RUNNING

**Server URL**: `http://localhost:8080`  
**H2 Console**: `http://localhost:8080/h2-console`  
**API Base**: `http://localhost:8080/api`

---

## Quick Start

### Option 1: Using the Startup Script (Recommended)
```batch
.\START_BACKEND.bat
```

### Option 2: Using Maven Directly
```batch
.\mvnw.cmd spring-boot:run
```

---

## What Was Fixed

### Problem
The backend was failing to start with error:
```
NoClassDefFoundError: net/bytebuddy/NamingStrategy$SuffixingRandom$BaseNameResolver
```

### Root Cause
ByteBuddy dependencies were scoped as `test` only, but Hibernate requires them at runtime for Java 24 compatibility.

### Solution Applied
1. **Updated pom.xml**: Removed `<scope>test</scope>` from ByteBuddy dependencies
2. **Added JVM arguments**: Configured Spring Boot Maven plugin with Java 24 compatibility flags:
   - `-Dnet.bytebuddy.experimental=true`
   - `--add-opens java.base/java.lang=ALL-UNNAMED`
   - `--add-opens java.base/java.util=ALL-UNNAMED`
   - `--add-opens java.base/java.lang.reflect=ALL-UNNAMED`
3. **Created startup script**: `START_BACKEND.bat` for easy server launch

---

## API Endpoints

### Root Endpoint
- **GET** `/` - Welcome message

### Dataset Management
- **POST** `/api/dataset/upload` - Upload CSV dataset
- **POST** `/api/dataset/ingest` - Process uploaded dataset
- **GET** `/api/dataset/stats` - Get dataset statistics
- **DELETE** `/api/dataset/clear` - Clear all data

### Analytics
- **GET** `/api/analytics/top-customers` - Top 10 customers by spending
- **GET** `/api/analytics/top-sellers` - Top 10 sellers by revenue
- **GET** `/api/analytics/delivery-delays` - Analyze delivery delays
- **GET** `/api/analytics/payment-analysis` - Payment method analysis
- **GET** `/api/analytics/monthly-trends` - Monthly revenue trends

### Error Records
- **GET** `/api/dataset/faulty-records` - Get all faulty records from DLQ

---

## Testing the Backend

### Test 1: Check Server Status
```batch
curl http://localhost:8080/
```

Expected Response:
```json
{
  "status": "running",
  "message": "TransitFlow Backend is operational"
}
```

### Test 2: Get Dataset Stats
```batch
curl http://localhost:8080/api/dataset/stats
```

Expected Response:
```json
{
  "total_deliveries": 0,
  "total_order_items": 0,
  "total_payments": 0,
  "total_customers": 0,
  "total_sellers": 0,
  "total_orders": 0,
  "faulty_records": 0,
  "total_products": 0
}
```

### Test 3: Health Check via H2 Console
1. Open browser: `http://localhost:8080/h2-console`
2. Use connection details:
   - **JDBC URL**: `jdbc:h2:mem:transitflowdb`
   - **Username**: `sa`
   - **Password**: (leave empty)

---

## Server Configuration

### application.properties
```properties
server.port=8080
spring.datasource.url=jdbc:h2:mem:transitflowdb
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.web.cors.allowed-origins=http://localhost:8501
```

---

## Logs and Monitoring

### Startup Logs Location
The server logs are displayed in the terminal/console window.

### Key Startup Messages
```
✅ Tomcat initialized with port 8080 (http)
✅ HikariPool-1 - Start completed
✅ H2 console available at '/h2-console'
✅ Tomcat started on port 8080 (http)
✅ Started TransitFlowApplication in X.XXX seconds
```

---

## Common Issues and Solutions

### Issue 1: Port 8080 Already in Use
**Error**: `Port 8080 is already in use`

**Solution**:
```batch
# Find process using port 8080
netstat -ano | findstr :8080

# Kill the process (replace PID with actual process ID)
taskkill /PID <PID> /F
```

### Issue 2: Java Version Mismatch
**Error**: `Unsupported class file major version`

**Solution**:
Ensure Java 21 or 24 is installed:
```batch
java -version
```

### Issue 3: Cannot Connect from Frontend
**Error**: `Connection refused on localhost:8080`

**Solution**:
1. Verify backend is running: `curl http://localhost:8080/api/dataset/stats`
2. Check firewall settings
3. Ensure CORS is configured correctly in `application.properties`

---

## Stopping the Server

### If Started via Script
Press `Ctrl + C` in the terminal window

### If Started in Background
```batch
# Find the Java process
tasklist | findstr java

# Kill it
taskkill /F /IM java.exe
```

---

## Performance Metrics

### Startup Performance
- **Compilation Time**: ~4-8 seconds
- **Application Startup**: ~4.5 seconds
- **Total Ready Time**: ~12-15 seconds

### Runtime Configuration
- **JVM Heap**: Default Spring Boot settings
- **Database**: H2 In-Memory (fast but non-persistent)
- **Batch Size**: 1000 records per batch
- **Max File Upload**: 100MB

---

## Development Notes

### Technologies Used
- Spring Boot 3.3.5
- Java 21 (compatible with Java 24)
- H2 In-Memory Database
- Hibernate ORM
- Tomcat Embedded Server
- Lombok (for reducing boilerplate)
- ByteBuddy 1.15.11 (for Java 24 compatibility)

### Architecture Pattern
- **Controller Layer**: REST endpoints
- **Facade Layer**: Business orchestration
- **Service Layer**: Business logic
- **Repository Layer**: Data access

---

## Next Steps

1. ✅ Backend is running on port 8080
2. ✅ All endpoints are accessible
3. ✅ Algorithm tests passing (11/11)
4. 🔄 Ready for frontend integration
5. 🔄 Ready to ingest CSV datasets

---

## Support Files

- `START_BACKEND.bat` - Easy server startup
- `RUN_TESTS.bat` - Run all tests
- `RUN_ALGORITHM_TESTS.bat` - Run binary search tests
- `pom.xml` - Maven configuration

---

**Last Updated**: June 19, 2026  
**Status**: ✅ OPERATIONAL
