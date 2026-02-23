#!/bin/bash

echo "Starting dev environment..."
docker-compose up -d
mvn spring-boot:run