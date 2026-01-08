#!/bin/bash

echo "================================================"
echo "  Bajaj Broking Trading SDK - Quick Start"
echo "================================================"
echo ""

if ! command -v mvn &> /dev/null; then
    echo "❌ Maven not found. Please install Maven first."
    exit 1
fi

echo "✅ Maven found"
echo "🔨 Building project..."
mvn clean package -DskipTests -q

if [ $? -eq 0 ]; then
    echo "✅ Build successful!"
    echo "🚀 Starting application..."
    echo ""
    mvn spring-boot:run
else
    echo "❌ Build failed."
    exit 1
fi

