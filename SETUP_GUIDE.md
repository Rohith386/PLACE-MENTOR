# Placement Tracker - Setup Guide

Complete setup guide for the Placement Tracker application.

## 🚀 Quick Start

### Frontend Setup

```bash
cd placement-tracker-frontend
npm install
```

Create `.env.local`:
```
VITE_CLERK_PUBLISHABLE_KEY=pk_test_bmVlZGVkLWRvZy0wLmNsZXJrLmFjY291bnRzLmRldiQ
VITE_API_URL=http://localhost:8080/api
```

Start development server:
```bash
npm run dev
```

Frontend: http://localhost:3000

### Backend Setup

```bash
cd placement-tracker-backend
```

Create MySQL database:
```bash
mysql -u root -p
CREATE DATABASE placement_tracker;
```

Update `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/placement_tracker
    username: root
    password: your_password
```

Run backend:
```bash
mvn clean install
mvn spring-boot:run
```

Backend API: http://localhost:8080/api

## 📋 Environment Variables

### Frontend (.env.local)
```
VITE_CLERK_PUBLISHABLE_KEY=pk_test_bmVlZGVkLWRvZy0wLmNsZXJrLmFjY291bnRzLmRldiQ
VITE_API_URL=http://localhost:8080/api
```

### Backend (application.yml or system env)
```
CLERK_API_KEY=your_clerk_secret_key
CLERK_JWT_KEY=your_clerk_jwt_key
OPENAI_API_KEY=your_openai_api_key
```

## 🏗️ Architecture

### Frontend (React + Tailwind)
- Clerk AI Authentication
- Responsive Dashboard
- Progress Tracking
- Dream Company Selection
- Mock Interview System
- Personalized Roadmap

### Backend (Spring Boot + MySQL)
- RESTful APIs
- Agentic AI Recommendations
- Roadmap Generation
- Mock Interview Management
- Company Probability Calculation
- Student Progress Tracking

### Database (MySQL)
- Student profiles
- Company data
- Roadmap tracking
- Mock interview history
- Progress metrics

## 🤖 Agentic AI Features

The Agentic AI Virtual Guide:

1. **Analyzes Student Progress** - Tracks completion across all areas
2. **Generates Recommendations** - Suggests next topics, problems, projects
3. **Calculates Probabilities** - Dynamically updates company crack chances
4. **Creates Daily Goals** - Personalized actionable daily targets
5. **Provides Motivation** - Context-aware motivational messages
6. **Plans Strategies** - Adapts roadmaps based on performance

## 📊 Database Schema

### Students
- Profile information
- Skill levels
- Progress metrics
- Readiness score

### Companies
- Company details
- Interview patterns
- Base probability
- Frequent questions

### Student-Companies
- Dream company selections
- Dynamic probability scores
- Selection timestamps

### Roadmap Topics
- Topic categories (DSA, CS Fundamentals, Projects, etc.)
- Completion status
- Priority levels
- Progress percentage

### Mock Interviews
- Interview type (DSA, Behavioral, HR)
- Scores and feedback
- Interview history

## 🔐 Authentication

Uses Clerk AI for secure authentication:
- Student signup/login
- Profile creation
- Email verification
- Session management

## 🎯 Key Features

✓ Personalized roadmap generation
✓ Progress tracking dashboard
✓ Dream company probability calculator
✓ AI-powered mock interviews
✓ Agentic AI recommendations
✓ Motivational insights
✓ Daily actionable goals
✓ Resume & behavioral prep tracking

## 🛠️ Tech Stack

**Frontend:**
- React 18
- Vite
- Tailwind CSS
- Recharts
- Clerk AI

**Backend:**
- Java 17
- Spring Boot 3.1.5
- Spring Data JPA
- MySQL

**Database:**
- MySQL 8.0+

## 📞 Support

For issues or questions:
1. Check logs in both frontend and backend
2. Verify Clerk API keys are correct
3. Ensure MySQL is running
4. Check CORS configuration

## 📝 Notes

- Clerk key: `pk_test_bmVlZGVkLWRvZy0wLmNsZXJrLmFjY291bnRzLmRldiQ`
- CORS enabled for `http://localhost:3000`
- API base URL: `http://localhost:8080/api`
- Database: MySQL 8.0+

Happy Coding! 🚀
