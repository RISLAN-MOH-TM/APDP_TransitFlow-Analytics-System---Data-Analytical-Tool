#!/bin/bash

# TransitFlow Analytics System - Docker Startup Script (Linux/Mac)

set -e

echo "========================================"
echo "TransitFlow Analytics System"
echo "Docker Startup Script"
echo "========================================"
echo ""

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "❌ Error: Docker is not installed"
    echo "Please install Docker from: https://www.docker.com/products/docker-desktop"
    exit 1
fi

# Check if Docker Compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Error: Docker Compose is not installed"
    echo "Please install Docker Compose from: https://docs.docker.com/compose/install/"
    exit 1
fi

# Check if Docker is running
if ! docker info &> /dev/null; then
    echo "❌ Error: Docker daemon is not running"
    echo "Please start Docker and try again"
    exit 1
fi

echo "✅ Docker is installed and running"
echo ""

# Check if docker-compose.yml exists
if [ ! -f "docker-compose.yml" ]; then
    echo "❌ Error: docker-compose.yml not found"
    echo "Please run this script from the project root directory"
    exit 1
fi

echo "Building and starting TransitFlow services..."
echo ""

# Build and start services
docker-compose up -d --build

echo ""
echo "Waiting for services to be healthy..."
sleep 10

# Check service status
echo ""
echo "Service Status:"
docker-compose ps

echo ""
echo "========================================"
echo "✅ TransitFlow System Started!"
echo "========================================"
echo ""
echo "Access the application at:"
echo "  Frontend:  http://localhost:8501"
echo "  Backend:   http://localhost:8080"
echo "  H2 Console: http://localhost:8080/h2-console"
echo ""
echo "To view logs:"
echo "  docker-compose logs -f"
echo ""
echo "To stop the system:"
echo "  docker-compose down"
echo ""
echo "========================================"
