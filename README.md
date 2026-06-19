# 📊 TransitFlow Analytics System

**Enterprise-Grade Data Analytics Tool for Logistics & E-Commerce**

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-green)
![Python](https://img.shields.io/badge/Python-3.11-blue)
![Streamlit](https://img.shields.io/badge/Streamlit-1.32-red)
![Docker](https://img.shields.io/badge/Docker-Ready-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

---

## 🚀 Overview

TransitFlow is a comprehensive analytics system designed for processing and analyzing large-scale logistics and e-commerce datasets. It provides real-time insights into customer behavior, delivery performance, payment trends, and seller analytics.

### Key Features

✅ **High-Performance Data Processing**
- Chunk-based ingestion (5,000 records per batch)
- Automatic duplicate removal
- Real-time data validation
- Dead-Letter Queue for faulty records

✅ **Advanced Analytics**
- Top customers by spending
- Top sellers by revenue
- Delivery delay analysis
- Payment method insights
- Monthly revenue trends

✅ **Modern Architecture**
- RESTful API backend (Spring Boot)
- Interactive web dashboard (Streamlit)
- In-memory H2 database for speed
- Docker-ready deployment

✅ **Algorithm Implementation**
- Binary Search with O(log n) efficiency
- Comprehensive unit testing (100% pass rate)

---

## 🏗️ Architecture

```
┌──────────────────────────────────────────────────┐
│         TransitFlow Analytics System             │
├──────────────────────────────────────────────────┤
│                                                  │
│  ┌─────────────────┐       ┌─────────────────┐ │
│  │    Frontend     │       │     Backend     │ │
│  │   (Streamlit)   │◄─────►│  (Spring Boot)  │ │
│  │                 │  HTTP │                 │ │
│  │  - Dashboard    │       │  - REST API     │ │
│  │  - File Upload  │       │  - Analytics    │ │
│  │  - Reports      │       │  - Data Layer   │ │
│  └─────────────────┘       └─────────────────┘ │
│         :8501                     :8080         │
│                           │                      │
│                           ▼                      │
│                  ┌─────────────────┐            │
│                  │   H2 Database   │            │
│                  │   (In-Memory)   │            │
│                  └─────────────────┘            │
│                                                  │
└──────────────────────────────────────────────────┘
```

---

## 📋 Prerequisites

### Option 1: Docker (Recommended)

- Docker Desktop 20.10+
- Docker Compose 2.0+
- 4GB RAM minimum

### Option 2: Local Development

- Java 21 or 24
- Python 3.11+
- Maven 3.9+
- 4GB RAM minimum

---

## 🐳 Quick Start with Docker

### 1. Clone the Repository

```bash
git clone https://github.com/RISLAN-MOH-TM/APDP_TransitFlow-Analytics-System---Data-Analytical-Tool.git
cd APDP_TransitFlow-Analytics-System---Data-Analytical-Tool
```

### 2. Start the System

```bash
docker-compose up -d
```

### 3. Access the Application

- **Frontend Dashboard**: http://localhost:8501
- **Backend API**: http://localhost:8080
- **H2 Console**: http://localhost:8080/h2-console

### 4. Stop the System

```bash
docker-compose down
```

📖 **Full Docker Guide**: See [DOCKER_SETUP_GUIDE.md](./DOCKER_SETUP_GUIDE.md)

---

## 💻 Local Development Setup

### Backend (Spring Boot)

```bash
# Navigate to backend directory
cd backend

# Compile the project
./mvnw.cmd clean compile

# Run tests
./mvnw.cmd test

# Start the server
./mvnw.cmd spring-boot:run
```

The backend will start on http://localhost:8080

### Frontend (Streamlit)

```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
pip install -r requirements.txt

# Run the application
streamlit run streamlit_app.py
```

The frontend will open automatically at http://localhost:8501

---

## 🧪 Testing

### Run All Tests

```bash
cd backend
./mvnw.cmd test
```

### Run Specific Test Suites

```bash
# Algorithm tests (Binary Search)
./mvnw.cmd test -Dtest=BinarySearchTest

# Model tests
./mvnw.cmd test -Dtest=CustomerModelTest

# Controller tests
./mvnw.cmd test -Dtest=*ControllerTest
```

### Test Results

```
✅ Binary Search Algorithm: 11/11 tests passing
✅ Model Layer: 100% coverage
✅ Controller Layer: REST API verified
✅ Integration Tests: E2E workflow validated
```

---

## 📡 API Documentation

### Dataset Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/dataset/upload` | Upload CSV dataset |
| POST | `/api/dataset/ingest` | Process uploaded dataset |
| GET | `/api/dataset/stats` | Get dataset statistics |
| DELETE | `/api/dataset/clear` | Clear all data |
| GET | `/api/dataset/faulty-records` | Get error records |

### Analytics Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/analytics/top-customers` | Top 10 customers by spending |
| GET | `/api/analytics/top-sellers` | Top 10 sellers by revenue |
| GET | `/api/analytics/delivery-delays` | Delivery delay analysis |
| GET | `/api/analytics/payment-analysis` | Payment method breakdown |
| GET | `/api/analytics/monthly-trends` | Monthly revenue trends |

### Example API Call

```bash
# Get dataset statistics
curl http://localhost:8080/api/dataset/stats

# Response
{
  "total_deliveries": 100000,
  "total_orders": 99441,
  "total_customers": 96096,
  "total_sellers": 2989,
  "total_products": 32000,
  "faulty_records": 0
}
```

---

## 🛠️ Technology Stack

### Backend
- **Framework**: Spring Boot 3.3.5
- **Language**: Java 21
- **Database**: H2 (In-Memory)
- **ORM**: Hibernate/JPA
- **Build Tool**: Maven 3.9
- **Testing**: JUnit 5
- **Patterns**: Strategy, Adapter, Factory, Facade

### Frontend
- **Framework**: Streamlit 1.32
- **Language**: Python 3.11
- **Visualization**: Plotly, Altair
- **HTTP Client**: Requests
- **Data Processing**: Pandas

### DevOps
- **Containerization**: Docker
- **Orchestration**: Docker Compose
- **CI/CD**: GitHub Actions (optional)

---

## 📁 Project Structure

```
TransitFlow-Analytics-System/
├── backend/                    # Spring Boot backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/transitflow/
│   │   │   │   ├── algorithm/      # Binary Search implementation
│   │   │   │   ├── controller/     # REST controllers
│   │   │   │   ├── service/        # Business logic
│   │   │   │   ├── repository/     # Data access layer
│   │   │   │   ├── model/          # Domain entities
│   │   │   │   ├── facade/         # Facade pattern
│   │   │   │   └── adapter/        # Adapter pattern
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/                   # Unit & integration tests
│   ├── Dockerfile
│   ├── pom.xml
│   └── mvnw.cmd
├── frontend/                   # Streamlit frontend
│   ├── streamlit_app.py
│   ├── requirements.txt
│   └── Dockerfile
├── docker-compose.yml          # Docker orchestration
├── DOCKER_SETUP_GUIDE.md       # Docker documentation
├── README.md                   # This file
└── .dockerignore
```

---

## 🎯 Use Cases

### 1. E-Commerce Analytics
- Track customer purchase patterns
- Identify top-performing sellers
- Analyze payment preferences

### 2. Logistics Optimization
- Monitor delivery performance
- Identify delay patterns
- Optimize shipping routes

### 3. Business Intelligence
- Monthly revenue tracking
- Customer segmentation
- Product performance analysis

---

## 🔧 Configuration

### Backend Configuration (application.properties)

```properties
# Server
server.port=8080

# Database (H2 In-Memory)
spring.datasource.url=jdbc:h2:mem:transitflowdb
spring.datasource.username=sa
spring.datasource.password=

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=false

# File Upload
spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=100MB

# CORS
spring.web.cors.allowed-origins=http://localhost:8501
```

### Frontend Configuration (.streamlit/config.toml)

```toml
[server]
port = 8501
address = "0.0.0.0"
headless = true

[theme]
primaryColor = "#FF4B4B"
backgroundColor = "#FFFFFF"
secondaryBackgroundColor = "#F0F2F6"
textColor = "#262730"
```

---

## 🚦 Performance Metrics

### Data Processing
- **Ingestion Speed**: 5,000 records per chunk
- **Duplicate Removal**: Automatic
- **Memory Usage**: ~512MB (backend)
- **Response Time**: <100ms (API calls)

### Startup Performance
- **Backend**: ~12-15 seconds
- **Frontend**: ~3-5 seconds
- **Docker**: ~30-40 seconds (first run)

---

## 🐛 Known Issues & Limitations

1. **In-Memory Database**: Data is lost on restart (use PostgreSQL for persistence)
2. **File Upload Limit**: 100MB maximum
3. **No Authentication**: Add Spring Security for production use
4. **Single Instance**: Not horizontally scalable without modifications

---

## 🗺️ Roadmap

### Version 2.0 (Planned)
- [ ] PostgreSQL integration
- [ ] Redis caching
- [ ] JWT authentication
- [ ] API rate limiting
- [ ] Batch processing
- [ ] Real-time analytics with WebSockets
- [ ] Export to PDF/Excel
- [ ] Advanced data visualization
- [ ] Multi-tenant support
- [ ] Kubernetes deployment

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Development Guidelines
- Follow existing code style
- Write unit tests for new features
- Update documentation
- Ensure all tests pass

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👥 Authors

**RISLAN MOH TM**
- GitHub: [@RISLAN-MOH-TM](https://github.com/RISLAN-MOH-TM)

---

## 🙏 Acknowledgments

- Spring Boot community
- Streamlit team
- H2 Database project
- Docker community
- Contributors and testers

---

## 📞 Support

For support, email: [rislanmohammed151@gmail.com] or open an issue on GitHub.

---

## 📊 Project Stats

![GitHub stars](https://img.shields.io/github/stars/RISLAN-MOH-TM/APDP_TransitFlow-Analytics-System---Data-Analytical-Tool?style=social)
![GitHub forks](https://img.shields.io/github/forks/RISLAN-MOH-TM/APDP_TransitFlow-Analytics-System---Data-Analytical-Tool?style=social)
![GitHub issues](https://img.shields.io/github/issues/RISLAN-MOH-TM/APDP_TransitFlow-Analytics-System---Data-Analytical-Tool)
![GitHub pull requests](https://img.shields.io/github/issues-pr/RISLAN-MOH-TM/APDP_TransitFlow-Analytics-System---Data-Analytical-Tool)

---

**⭐ If you find this project useful, please star it on GitHub! ⭐**

---

**Last Updated**: June 20, 2026  
**Version**: 1.0.0  
**Status**: Production Ready 🚀
