# Placement Tracker Backend

AI-powered Placement Tracker Backend built with Spring Boot and MySQL.

## Features

- RESTful API for student management
- Company tracking with dynamic probability calculation
- Agentic AI for personalized recommendations
- Mock interview tracking
- Roadmap generation and tracking
- Clerk AI authentication integration

## Tech Stack

- Java 17
- Spring Boot 3.1.5
- MySQL
- Spring Data JPA
- Spring Security
- Lombok

## Setup

### Prerequisites
- Java 17+
- Maven 3.6+
- MySQL 8.0+

### Installation

1. Clone repository and navigate to backend folder
```bash
cd placement-tracker-backend
```

2. Create MySQL database:
```bash
mysql -u root -p < src/main/resources/schema.sql
```

3. Configure `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/placement_tracker
    username: root
    password: your_password
```

4. Build and run:
```bash
mvn clean install
mvn spring-boot:run
```

Server runs at http://localhost:8080

## API Endpoints

### Students
- `GET /api/students/profile` - Get student profile
- `PUT /api/students/profile` - Update profile
- `GET /api/students/progress` - Get progress

### Companies
- `GET /api/companies` - Get all companies
- `GET /api/companies/{id}` - Get company details

### AI Recommendations
- `GET /api/ai/recommendations` - Get personalized recommendations
- `GET /api/ai/motivation` - Get motivational message
- `GET /api/ai/daily-goals` - Get daily actionable goals

## Project Structure

```
src/main/java/com/placementtracker/
├── config/           # Security & CORS config
├── controller/       # REST endpoints
├── service/          # Business logic
├── repository/       # Database queries
├── entity/           # JPA entities
├── dto/              # Data transfer objects
└── ai/               # Agentic AI logic
```
