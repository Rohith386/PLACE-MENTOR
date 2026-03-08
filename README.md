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

## Installation Steps

### Step 1: Clone/Extract Project
All files are already created in the workspace.

### Step 2: Frontend Installation

```bash
cd placement-tracker-frontend
npm install
```

Create `.env.local`:
```
VITE_CLERK_PUBLISHABLE_KEY=pk_test_bmVlZGVkLWRvZy0wLmNsZXJrLmFjY291bnRzLmRldiQ
VITE_API_URL=http://localhost:8080/api
```

Start frontend:
```bash
npm run dev
```

### Step 3: Backend Installation

### Prerequisites
- Java 17 JDK
- Maven 3.6+
- MySQL 8.0+

### MySQL Setup

1. Install MySQL if not already installed
2. Create database and schema:

```bash
mysql -u root -p
```

Then execute:
```sql
CREATE DATABASE placement_tracker;
USE placement_tracker;
-- Run schema.sql content
```

Or import schema file:
```bash
mysql -u root -p placement_tracker < placement-tracker-backend/src/main/resources/schema.sql
```

### Update Backend Configuration

Edit `placement-tracker-backend/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/placement_tracker
    username: root
    password: your_mysql_password
```

### Build and Run Backend

```bash
cd placement-tracker-backend
mvn clean install
mvn spring-boot:run
```

Backend will start at `http://localhost:8080`

## ✅ Verification

### Frontend Health Check
- Open http://localhost:3000
- Should see login page with Clerk authentication
- Try signing up with test account

### Backend Health Check
```bash
curl http://localhost:8080/api/companies
```

Should return list of companies.

## 🤖 Agentic AI Features

The system includes an Agentic AI Virtual Guide that:

1. **Analyzes Progress**
   - Tracks all preparation areas
   - Calculates readiness score
   - Identifies weak areas

2. **Generates Recommendations**
   - Next topics to study
   - Practice problems to solve
   - Projects to build
   - When to take mock interviews

3. **Dynamic Probability**
   - Calculates company crack chances
   - Updates based on readiness score
   - Considers mock interview performance
   - Weighted algorithm for accuracy

4. **Motivation System**
   - Context-aware messages
   - Performance-based insights
   - Celebration of milestones
   - Adaptive encouragement

5. **Daily Goals**
   - Personalized action items
   - Adaptive based on progress
   - Prioritized recommendations

## 📱 Core Features

### 1. Dashboard
- Overall preparation readiness score
- Progress by category (DSA, CS, Projects, etc.)
- Recent achievements
- AI recommendations
- Weekly progress trends

### 2. Personalized Roadmap
- Auto-generated learning path
- Adaptive based on progress
- Categories: DSA, CS Fundamentals, Projects, Aptitude, Soft Skills
- Topic tracking with completion status
- Priority indicators

### 3. Dream Companies
- Select target companies
- View company-specific questions
- Dynamic probability calculator
- Interview pattern insights
- Past interview analysis

### 4. Mock Interview System
- AI-powered interviews
- Types: DSA, Behavioral, HR
- Real-time feedback
- Strengths & improvement areas
- Score tracking

### 5. Progress Tracking
- Visual charts and graphs
- Topics completed count
- Problems solved
- Mock interview history
- Overall readiness percentage

### 6. AI Recommendations
- What to study next
- Priority-based suggestions
- Motivational messages
- Daily goals

## 🔑 Clerk Authentication

The app uses Clerk AI for secure authentication:

**Publishable Key:** `pk_test_bmVlZGVkLWRvZy0wLmNsZXJrLmFjY291bnRzLmRldiQ`

This key is already configured in the frontend `.env.local`.

## 📊 Database Overview

### Tables Created
1. **students** - User profiles
2. **companies** - Company data
3. **student_companies** - Dream company selections
4. **roadmap_topics** - Learning roadmap
5. **mock_interviews** - Interview history

### Pre-populated Data
- 8 major companies (Google, Amazon, Microsoft, etc.)
- DSA topic roadmap
- Sample CS fundamentals topics

## 🚀 Running Everything

### Terminal 1 - Frontend
```bash
cd placement-tracker-frontend
npm run dev
```
Runs on: http://localhost:3000

### Terminal 2 - Backend
```bash
cd placement-tracker-backend
mvn spring-boot:run
```
Runs on: http://localhost:8080

### Terminal 3 - MySQL (if not running as service)
```bash
mysql -u root -p
```

## 📝 API Endpoints

### Student APIs
- `GET /api/students/profile` - Get profile
- `POST /api/students/profile` - Create profile
- `PUT /api/students/profile` - Update profile
- `GET /api/students/progress` - Get progress

### Company APIs
- `GET /api/companies` - List all companies
- `GET /api/companies/{id}` - Get company details

### AI APIs
- `GET /api/ai/recommendations` - Get recommendations
- `GET /api/ai/motivation` - Get motivational message
- `GET /api/ai/daily-goals` - Get daily goals

### Roadmap APIs
- `GET /api/roadmap` - Get student roadmap
- `PUT /api/roadmap/{topicId}` - Update topic progress

### Mock Interview APIs
- `POST /api/mock-interview/start` - Start interview
- `POST /api/mock-interview/{id}/submit` - Submit answer
- `GET /api/mock-interview/{id}/feedback` - Get feedback

## 🔒 Security

- Clerk AI authentication
- CORS configured for localhost:3000
- Spring Security enabled
- JWT token-based API calls

## 🎯 Next Steps

1. ✅ Complete setup steps above
2. ✅ Create a test student account
3. ✅ Set up dream companies
4. ✅ Start practicing with mock interviews
5. ✅ Track progress on dashboard

## 📖 Documentation Files

- `SETUP_GUIDE.md` - Installation and setup
- `DATABASE_SCHEMA.md` - Database structure
- `API_DOCUMENTATION.md` - API endpoints and usage
- Frontend `README.md` - React-specific setup
- Backend `README.md` - Spring Boot-specific setup

## 🆘 Troubleshooting

### Frontend won't connect to backend
- Check if backend is running on port 8080
- Verify CORS is enabled
- Check browser console for errors

### Backend won't start
- Verify MySQL is running
- Check database credentials in application.yml
- Ensure Java 17+ is installed

### Mock interviews not loading
- Check that backend AI service is initialized
- Verify student profile is complete

## 💡 Key Technologies

- **Frontend**: React 18, Vite, Tailwind CSS, Recharts
- **Backend**: Spring Boot 3.1.5, Spring Data JPA
- **Database**: MySQL 8.0+
- **Authentication**: Clerk AI
- **AI**: Agentic AI for recommendations

---

Happy coding! 🚀 Your placement preparation journey starts here!
=======
# PLACE-MENTOR
>>>>>>> 7e39001797185aeb82c355d4d336a2c290d97871
