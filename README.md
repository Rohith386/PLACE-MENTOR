<<<<<<< HEAD
# Placement Tracker - Complete Setup & Configuration

## Project Structure

```
PLACEMENT TRACKER 1/
├── placement-tracker-frontend/     # React Frontend
│   ├── src/
│   │   ├── components/            # UI Components
│   │   ├── pages/                 # Page Components
│   │   ├── services/              # API Services
│   │   ├── store/                 # State Management (Zustand)
│   │   ├── hooks/                 # Custom Hooks
│   │   ├── App.jsx
│   │   └── main.jsx
│   ├── public/
│   ├── package.json
│   ├── vite.config.js
│   ├── tailwind.config.js
│   └── README.md
│
├── placement-tracker-backend/      # Spring Boot Backend
│   ├── src/main/java/com/placementtracker/
│   │   ├── config/               # Security & CORS
│   │   ├── controller/           # REST APIs
│   │   ├── service/              # Business Logic
│   │   ├── repository/           # Database Queries
│   │   ├── entity/               # JPA Entities
│   │   ├── dto/                  # Data Transfer Objects
│   │   ├── ai/                   # Agentic AI Logic
│   │   └── PlacementTrackerApplication.java
│   ├── src/main/resources/
│   │   ├── application.yml       # Configuration
│   │   └── schema.sql            # Database Schema
│   ├── pom.xml
│   └── README.md
│
├── SETUP_GUIDE.md
├── DATABASE_SCHEMA.md
└── API_DOCUMENTATION.md
```

# 🚀 PLACE-MENTOR

An AI-powered placement preparation platform designed to help students track their coding progress,
prepare for dream companies, and improve their interview readiness through personalized roadmaps and analytics.

---

## 📌 Features

### 👤 User Management
- User Registration & Login
- Secure Authentication
- Profile Management

### 📊 Progress Tracking
- Track coding progress
- Monitor solved DSA problems
- Visual progress dashboard
- Performance analytics

### 🎯 Company Preparation
- Select dream company
- Personalized preparation roadmap
- Company-wise interview preparation
- Topic-wise progress tracking

### 🤖 AI Features
- AI-powered mock interview assistance
- Intelligent preparation recommendations
- Personalized learning suggestions

### 📅 Roadmap
- Beginner to Advanced learning roadmap
- DSA topic tracking
- Backend Development roadmap
- Interview preparation checklist

---

# 🏗️ System Architecture

```
                React Frontend
                      │
                      │ REST API
                      ▼
           Spring Boot Backend
                      │
        ┌─────────────┴─────────────┐
        │                           │
 Spring Data JPA             Authentication
        │
        ▼
      MySQL Database
```

---

# 🛠️ Tech Stack

## Frontend
- React.js
- Vite
- Tailwind CSS
- Axios

## Backend
- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- REST APIs
- Maven

## Database
- MySQL

## Tools
- Git
- GitHub
- Postman
- VS Code

---

# 📂 Project Structure

```
placement-tracker-backend
│
├── controller
├── service
├── repository
├── entity
├── dto
├── config
├── ai
└── resources

placement-tracker-frontend
│
├── components
├── pages
├── hooks
├── services
├── store
└── assets
```

---

# 📌 Key Functionalities

✔ User Authentication

✔ CRUD Operations

✔ Progress Tracking

✔ Personalized Dashboard

✔ Company Preparation Roadmap

✔ Dream Company Selection

✔ AI Mock Interview Support

✔ REST API Integration

✔ Responsive UI

---

# 📷 Screenshots

## Dashboard

> <img width="1888" height="936" alt="image" src="https://github.com/user-attachments/assets/f9468475-a3c3-49f6-a988-f472f4480bf8" />
> <img width="1850" height="746" alt="image" src="https://github.com/user-attachments/assets/a2f0b368-86d2-4308-8ddd-35af99b452e8" />



---

## Roadmap

> <img width="1916" height="940" alt="image" src="https://github.com/user-attachments/assets/ad1233aa-089d-4205-97c7-9f3b60d14702" />


---

## Progress Analytics

> <img width="1900" height="912" alt="image" src="https://github.com/user-attachments/assets/2f40f2a7-d24c-4c45-88cb-6a5ab2d06ee1" />


---

## Profile Page

> <img width="1919" height="930" alt="image" src="https://github.com/user-attachments/assets/9b93e651-5503-4962-b533-bf851e0738a5" />

## Mock Interview Page

> <img width="1915" height="817" alt="image" src="https://github.com/user-attachments/assets/8271a315-c6ea-401d-9bb7-4ff98a5660aa" />

---

# ⚙️ Installation

## Clone Repository

```bash
git clone https://github.com/Rohith386/PLACE-MENTOR.git
```

### Backend

```bash
cd placement-tracker-backend
```

Install dependencies

```bash
mvn clean install
```

Run

```bash
mvn spring-boot:run
```

---

### Frontend

```bash
cd placement-tracker-frontend
```

Install dependencies

```bash
npm install
```

Run

```bash
npm run dev
```

---

# 🗄️ Database Configuration

Update `application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/placement_tracker

spring.datasource.username=root

spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
```

---

# 📡 REST APIs

## Authentication

```
POST /api/auth/register

POST /api/auth/login
```

## User

```
GET /api/users

PUT /api/users/{id}

DELETE /api/users/{id}
```

## Progress

```
GET /api/progress

POST /api/progress

PUT /api/progress/{id}
```

## Roadmap

```
GET /api/roadmap

POST /api/roadmap
```

---

# 🎯 Future Enhancements

- AI Resume Analyzer
- LeetCode API Integration
- GitHub API Integration
- Coding Contest Tracker
- Resume Builder
- Company Interview Experience Sharing
- Email Notifications
- Docker Deployment
- AWS Deployment
- JWT Authentication
- Admin Dashboard

---

# 📈 Learning Outcomes

This project helped me gain hands-on experience with:

- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- REST API Development
- MySQL Database Design
- React
- Tailwind CSS
- Layered Architecture
- Exception Handling
- Git & GitHub

---

# 👨‍💻 Author

**Rohith K**

Aspiring Software Engineer

- Java Backend Developer
- Spring Boot Developer
- DSA Enthusiast

GitHub: https://github.com/Rohith386

LinkedIn: https://www.linkedin.com/in/rohith-ka1b2c3

---

Happy coding! 🚀 Your placement preparation journey starts here!
=======
# PLACE-MENTOR
>>>>>>> 7e39001797185aeb82c355d4d336a2c290d97871
