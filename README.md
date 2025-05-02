# Jakarta Servlet Web Application

![Java](https://img.shields.io/badge/Java-22-orange)
![Spring](https://img.shields.io/badge/Spring-6.1.5-green)
![Tomcat](https://img.shields.io/badge/Tomcat-11.0.6-blue)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)
![Docker](https://img.shields.io/badge/Docker-✓-blue)

## 📋 Overview

This project is a web application built using Jakarta Servlet API, Spring Framework, and PostgreSQL. It demonstrates core concepts of Java web development including user authentication, file uploads, and containerized deployment with Docker.

The application consists of two modules:

- **ex00**: Basic authentication and user management
- **ex01**: Enhanced version with file upload and authentication history tracking

![Application Architecture](https://via.placeholder.com/800x400?text=Application+Architecture)

## 🚀 Features

- **User Management**

  - Registration/Signup
  - Authentication/Signin
  - Password encryption with BCrypt
  - Session management

- **File Upload System** (ex01)

  - Image upload functionality
  - File metadata storage
  - Secure file access

- **Authentication History** (ex01)
  - IP address tracking
  - Login timestamp recording
  - History display on profile page

## 🛠️ Running the Application

### Prerequisites

- Docker and Docker Compose

### Quick Start

1. Clone the repository:

   ```bash
   git clone https://github.com/yourusername/fwa.git
   cd fwa
   ```

2. Start the application using Docker Compose:

   ```bash
   docker compose up --build
   ```

3. Access the applications:
   - ex00: http://localhost:4000/cinema
   - ex01: http://localhost:4001/cinema

### Development Setup

If you want to run the application locally during development:

1. Install PostgreSQL
2. Execute the database schema in `postgres/init.sql`
3. Run either module using Maven:
   ```bash
   cd ex00 # or ex01
   mvn clean package cargo:run
   ```

## 🎓 Key Learning Concepts

This project serves as a practical guide to several important Java web development concepts:

### 1. Jakarta Servlet API

- Servlet lifecycle management
- HTTP request/response handling
- Web application structure

### 2. Spring Integration

- Dependency Injection
- Application context configuration
- Service-oriented architecture

### 3. Database Interaction

- JDBC operations with Spring
- Connection pooling with HikariCP
- SQL query execution

### 4. Security Practices

- Password hashing
- Authentication flow
- Session management

### 5. File Handling

- Multipart request processing
- File storage strategies
- Content type management

### 6. Containerization

- Docker multi-container setup
- Environment variable configuration
- Volume management for persistence

## 📝 Project Structure

```
fwa/
├── docker-compose.yml
├── postgres/
│   └── init.sql
├── ex00/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/
│           ├── resources/
│           └── webapp/
└── ex01/
    ├── Dockerfile
    ├── pom.xml
    └── src/
        └── main/
            ├── java/
            ├── resources/
            └── webapp/
```

![Application Screenshot](https://via.placeholder.com/800x400?text=Application+Screenshot)

## 🔗 Additional Resources

- [Jakarta EE Documentation](https://jakarta.ee/specifications/servlet/)
- [Spring Framework Documentation](https://spring.io/projects/spring-framework)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Docker Documentation](https://docs.docker.com/)
