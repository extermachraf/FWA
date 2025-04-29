#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}Starting database initialization script...${NC}"

# Check if PostgreSQL is available
echo -e "${YELLOW}Checking connection to PostgreSQL...${NC}"
pg_isready -U postgres
if [ $? -ne 0 ]; then
    echo -e "${RED}PostgreSQL is not ready! Check database service.${NC}"
    exit 1
fi

echo -e "${GREEN}PostgreSQL is available.${NC}"

# Check if cinema database exists
echo -e "${YELLOW}Checking if cinema database exists...${NC}"
DBEXISTS=$(psql -U postgres -tAc "SELECT 1 FROM pg_database WHERE datname='cinema'")
if [ "$DBEXISTS" = "1" ]; then
    echo -e "${GREEN}Cinema database already exists. Skipping creation.${NC}"
else
    echo -e "${YELLOW}Creating cinema database...${NC}"
    createdb -U postgres cinema
    echo -e "${GREEN}Cinema database created successfully.${NC}"
fi

# Clean up temporary SQL files if they exist
echo -e "${YELLOW}Cleaning up temporary files...${NC}"
rm -f /docker-entrypoint-initdb.d/01-schema.sql
rm -f /docker-entrypoint-initdb.d/02-data.sql

echo -e "${GREEN}Database initialization script completed.${NC}"
exit 0 