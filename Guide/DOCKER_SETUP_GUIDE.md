# 🐳 TransitFlow Analytics System - Docker Setup Guide

Complete guide for running TransitFlow Analytics System using Docker and Docker Compose.

---

## 📋 Table of Contents

- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Docker Architecture](#docker-architecture)
- [Building the Images](#building-the-images)
- [Running the System](#running-the-system)
- [Accessing the Services](#accessing-the-services)
- [Managing Containers](#managing-containers)
- [Troubleshooting](#troubleshooting)
- [Advanced Configuration](#advanced-configuration)

---

## 🔧 Prerequisites

### Required Software

1. **Docker Desktop** (Windows/Mac) or **Docker Engine** (Linux)
   - Download: https://www.docker.com/products/docker-desktop
   - Minimum Version: 20.10.0+

2. **Docker Compose**
   - Usually included with Docker Desktop
   - Minimum Version: 2.0.0+

### System Requirements

- **RAM**: Minimum 4GB, Recommended 8GB+
- **CPU**: 2 cores minimum, 4+ recommended
- **Disk Space**: 5GB free space
- **OS**: Windows 10/11, macOS 10.15+, or Linux (Ubuntu 20.04+)

### Verify Installation

```bash
# Check Docker version
docker --version
# Output: Docker version 24.0.0 or higher

# Check Docker Compose version
docker-compose --version
# Output: Docker Compose version 2.0.0 or higher

# Verify Docker is running
docker ps
# Should show empty list or running containers (no errors)
```

---

## 🚀 Quick Start

### One-Command Startup

```bash
# Clone the repository (if not already done)
git clone https://github.com/RISLAN-MOH-TM/APDP_TransitFlow-Analytics-System---Data-Analytical-Tool.git
cd APDP_TransitFlow-Analytics-System---Data-Analytical-Tool

# Start the entire system
docker-compose up -d
```

### Wait for Services to Start

The first run will take 3-5 minutes as Docker downloads base images and builds your containers.

```bash
# Watch the startup logs
docker-compose logs -f

# Check service health
docker-compose ps
```

### Access the Application

Once both services show as "healthy":

- **Frontend Dashboard**: http://localhost:8501
- **Backend API**: http://localhost:8080
- **H2 Database Console**: http://localhost:8080/h2-console

---

## 🏗️ Docker Architecture

### System Overview

```
┌─────────────────────────────────────────────────┐
│           TransitFlow Docker Stack              │
├─────────────────────────────────────────────────┤
│                                                 │
│  ┌────────────────┐      ┌──────────────────┐ │
│  │   Frontend     │      │    Backend       │ │
│  │   (Streamlit)  │◄────►│  (Spring Boot)   │ │
│  │   Port: 8501   │      │   Port: 8080     │ │
│  └────────────────┘      └──────────────────┘ │
│         │                         │            │
│         └─────────────────────────┘            │
│              transitflow-network               │
│                                                 │
└─────────────────────────────────────────────────┘
```

### Container Details

| Container | Base Image | Ports | Purpose |
|-----------|-----------|-------|---------|
| `transitflow-backend` | eclipse-temurin:21-jre | 8080 | REST API & Business Logic |
| `transitflow-frontend` | python:3.11-slim | 8501 | Web Dashboard UI |

---

## 🔨 Building the Images

### Build All Services

```bash
# Build both frontend and backend
docker-compose build

# Build with no cache (clean build)
docker-compose build --no-cache
```

### Build Individual Services

```bash
# Build only backend
docker-compose build backend

# Build only frontend
docker-compose build frontend
```

### Check Built Images

```bash
# List Docker images
docker images | grep transitflow

# Expected output:
# system-backend    latest    <image-id>    <size>
# system-frontend   latest    <image-id>    <size>
```

---

## ▶️ Running the System

### Start All Services

```bash
# Start in detached mode (background)
docker-compose up -d

# Start with logs visible
docker-compose up

# Start specific services
docker-compose up -d backend
docker-compose up -d frontend
```

### View Logs

```bash
# View all logs
docker-compose logs

# Follow logs in real-time
docker-compose logs -f

# View specific service logs
docker-compose logs backend
docker-compose logs frontend

# Last 100 lines
docker-compose logs --tail=100
```

### Check Service Status

```bash
# View running containers
docker-compose ps

# Detailed status
docker-compose ps -a

# Check health status
docker inspect --format='{{.State.Health.Status}}' transitflow-backend
docker inspect --format='{{.State.Health.Status}}' transitflow-frontend
```

---

## 🌐 Accessing the Services

### Frontend Dashboard

**URL**: http://localhost:8501

**Features**:
- Upload CSV datasets
- View analytics dashboards
- Generate reports
- Real-time data visualization

### Backend API

**Base URL**: http://localhost:8080

**Key Endpoints**:

```bash
# Health check
curl http://localhost:8080/api/dataset/stats

# Upload dataset
curl -X POST -F "file=@dataset.csv" http://localhost:8080/api/dataset/upload

# Get analytics
curl http://localhost:8080/api/analytics/top-customers
curl http://localhost:8080/api/analytics/monthly-trends
```

### H2 Database Console

**URL**: http://localhost:8080/h2-console

**Credentials**:
- **JDBC URL**: `jdbc:h2:mem:transitflowdb`
- **Username**: `sa`
- **Password**: (leave empty)

---

## 🛠️ Managing Containers

### Stop Services

```bash
# Stop all services
docker-compose stop

# Stop specific service
docker-compose stop backend
docker-compose stop frontend
```

### Restart Services

```bash
# Restart all services
docker-compose restart

# Restart specific service
docker-compose restart backend
```

### Stop and Remove Containers

```bash
# Stop and remove containers, networks
docker-compose down

# Also remove volumes
docker-compose down -v

# Remove everything including images
docker-compose down --rmi all -v
```

### Update and Restart

```bash
# Pull latest code
git pull

# Rebuild and restart
docker-compose up -d --build

# Or in separate steps
docker-compose build
docker-compose down
docker-compose up -d
```

---

## 🔍 Troubleshooting

### Issue 1: Port Already in Use

**Error**: `Bind for 0.0.0.0:8080 failed: port is already allocated`

**Solution**:
```bash
# Find process using the port
netstat -ano | findstr :8080

# Kill the process (Windows)
taskkill /PID <PID> /F

# Or change port in docker-compose.yml
ports:
  - "8081:8080"  # Use different host port
```

### Issue 2: Services Not Starting

**Error**: Container exits immediately

**Solution**:
```bash
# Check logs for errors
docker-compose logs backend
docker-compose logs frontend

# Check container status
docker ps -a

# Restart with fresh build
docker-compose down
docker-compose build --no-cache
docker-compose up
```

### Issue 3: Cannot Connect to Backend from Frontend

**Error**: `Connection refused` or `Network error`

**Solution**:
```bash
# Verify both containers are on same network
docker network inspect transitflow-network

# Check backend health
curl http://localhost:8080/api/dataset/stats

# Restart services
docker-compose restart
```

### Issue 4: Out of Memory

**Error**: `Container killed` or `OOMKilled`

**Solution**:
```bash
# Increase Docker memory limit
# Docker Desktop → Settings → Resources → Memory (set to 4GB+)

# Or modify JAVA_OPTS in docker-compose.yml
environment:
  - JAVA_OPTS=-Xmx256m -Xms128m  # Reduce memory usage
```

### Issue 5: Build Fails

**Error**: Build errors during `docker-compose build`

**Solution**:
```bash
# Clean Docker cache
docker system prune -a

# Clean Maven cache (backend)
docker-compose run --rm backend mvn clean

# Rebuild from scratch
docker-compose build --no-cache --pull
```

---

## ⚙️ Advanced Configuration

### Environment Variables

Create a `.env` file in the project root:

```env
# Backend Configuration
BACKEND_PORT=8080
SPRING_PROFILES_ACTIVE=docker
JAVA_OPTS=-Xmx512m -Xms256m

# Frontend Configuration
FRONTEND_PORT=8501
BACKEND_URL=http://backend:8080
STREAMLIT_SERVER_HEADLESS=true

# Database Configuration (if using external DB)
DB_HOST=localhost
DB_PORT=5432
DB_NAME=transitflow
DB_USER=admin
DB_PASSWORD=secret123
```

### Custom Docker Compose Override

Create `docker-compose.override.yml` for local customization:

```yaml
version: '3.8'

services:
  backend:
    environment:
      - SPRING_PROFILES_ACTIVE=dev
    volumes:
      - ./backend/logs:/app/logs

  frontend:
    volumes:
      - ./frontend/data:/app/data
```

### Production Configuration

For production deployment, create `docker-compose.prod.yml`:

```yaml
version: '3.8'

services:
  backend:
    restart: always
    environment:
      - SPRING_PROFILES_ACTIVE=prod
    deploy:
      resources:
        limits:
          cpus: '2'
          memory: 1G
        reservations:
          cpus: '1'
          memory: 512M

  frontend:
    restart: always
    deploy:
      resources:
        limits:
          cpus: '1'
          memory: 512M
```

Run with:
```bash
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d
```

---

## 📊 Monitoring and Logs

### View Resource Usage

```bash
# Real-time resource monitoring
docker stats

# Specific container stats
docker stats transitflow-backend transitflow-frontend
```

### Export Logs

```bash
# Save logs to file
docker-compose logs > transitflow-logs.txt

# Save with timestamps
docker-compose logs -t > transitflow-logs-$(date +%Y%m%d).txt
```

### Health Checks

```bash
# Check health status
docker-compose ps

# Manual health check
curl http://localhost:8080/api/dataset/stats
curl http://localhost:8501/_stcore/health
```

---

## 🧹 Cleanup

### Remove All TransitFlow Resources

```bash
# Stop and remove containers
docker-compose down

# Remove volumes
docker volume rm transitflow-backend-data transitflow-frontend-data

# Remove network
docker network rm transitflow-network

# Remove images
docker rmi system-backend system-frontend
```

### Complete Docker Cleanup

```bash
# Remove all stopped containers
docker container prune -f

# Remove all unused images
docker image prune -a -f

# Remove all unused volumes
docker volume prune -f

# Remove all unused networks
docker network prune -f

# Complete system cleanup
docker system prune -a --volumes -f
```

---

## 📚 Additional Resources

- **Docker Documentation**: https://docs.docker.com
- **Docker Compose Reference**: https://docs.docker.com/compose
- **Spring Boot Docker Guide**: https://spring.io/guides/gs/spring-boot-docker
- **Streamlit Deployment**: https://docs.streamlit.io/deploy

---

## 🆘 Getting Help

If you encounter issues:

1. Check logs: `docker-compose logs`
2. Verify health: `docker-compose ps`
3. Review this guide's troubleshooting section
4. Check GitHub issues: https://github.com/RISLAN-MOH-TM/APDP_TransitFlow-Analytics-System---Data-Analytical-Tool/issues

---

**Last Updated**: June 20, 2026  
**Docker Version**: 24.0+  
**Docker Compose Version**: 2.0+
