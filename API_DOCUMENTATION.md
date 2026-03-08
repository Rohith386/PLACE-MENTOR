# Placement Tracker - API Documentation

## Base URL
```
http://localhost:8080/api
```

## Authentication
All API endpoints (except public ones) require:
```
Header: X-Clerk-ID: <clerk_user_id>
```

---

## 👨‍🎓 Student APIs

### Get Student Profile
```http
GET /students/profile
Header: X-Clerk-ID: <user_id>
```

**Response (200):**
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "branch": "CSE",
  "year": 3,
  "skillLevel": "intermediate",
  "readinessScore": 65,
  "topicsCompleted": 12,
  "problemsSolved": 145,
  "mockInterviews": 3,
  "createdAt": "2024-01-20T10:30:00"
}
```

### Create Student Profile
```http
POST /students/profile
Header: X-Clerk-ID: <user_id>
Content-Type: application/json
```

**Request:**
```json
{
  "email": "john@example.com",
  "firstName": "John",
  "lastName": "Doe"
}
```

### Update Student Profile
```http
PUT /students/profile
Header: X-Clerk-ID: <user_id>
Content-Type: application/json
```

**Request:**
```json
{
  "branch": "CSE",
  "year": 3,
  "skillLevel": "intermediate"
}
```

### Get Student Progress
```http
GET /students/progress
Header: X-Clerk-ID: <user_id>
```

**Response (200):**
```json
{
  "id": 1,
  "readinessScore": 65,
  "topicsCompleted": 12,
  "problemsSolved": 145,
  "mockInterviews": 3,
  "dsaProgress": 70,
  "csFundamentalsProgress": 60,
  "projectsProgress": 50,
  "aptitudeProgress": 75,
  "softSkillsProgress": 55
}
```

---

## 🏢 Company APIs

### Get All Companies
```http
GET /companies
```

**Response (200):**
```json
[
  {
    "id": 1,
    "name": "Google",
    "description": "Search and Advertising Giant",
    "logo": "google.png",
    "probability": 45,
    "interviewPattern": "Phone Screen -> DSA Round -> System Design -> HR"
  },
  {
    "id": 2,
    "name": "Amazon",
    "description": "E-commerce and Cloud Leader",
    "logo": "amazon.png",
    "probability": 72,
    "interviewPattern": "Online Assessment -> DSA Round -> System Design -> Bar Raiser"
  }
]
```

### Get Company Details
```http
GET /companies/{companyId}
```

**Response (200):**
```json
{
  "id": 1,
  "name": "Google",
  "description": "Search and Advertising Giant",
  "logo": "google.png",
  "probability": 45,
  "interviewPattern": "Phone Screen -> DSA Round -> System Design -> HR",
  "commonQuestions": ["Design YouTube", "LRU Cache", "Median of Two Sorted Arrays"]
}
```

---

## 🤖 AI APIs

### Get Recommendations
```http
GET /ai/recommendations
Header: X-Clerk-ID: <user_id>
```

**Response (200):**
```json
[
  {
    "title": "Study: Dynamic Programming",
    "description": "Complete this topic to improve your overall readiness",
    "priority": "high",
    "type": "topic"
  },
  {
    "title": "Practice 5 medium-level DSA problems",
    "description": "Your DSA accuracy is 65%. Practice more to reach 85%",
    "priority": "high",
    "type": "problem"
  },
  {
    "title": "Build a Full-Stack Todo App",
    "description": "Create a project to strengthen your portfolio",
    "priority": "medium",
    "type": "project"
  }
]
```

### Get Motivational Message
```http
GET /ai/motivation
Header: X-Clerk-ID: <user_id>
```

**Response (200):**
```json
{
  "message": "You're almost there! 🚀 Your preparation is outstanding. Keep pushing for that dream company!"
}
```

### Get Daily Goals
```http
GET /ai/daily-goals
Header: X-Clerk-ID: <user_id>
```

**Response (200):**
```json
{
  "goals": [
    "✓ Solve 3 DSA problems",
    "✓ Review 1 CS Fundamental concept",
    "✓ Spend 30 mins on weak area",
    "✓ Schedule mock interview"
  ]
}
```

### Plan Next Steps
```http
POST /ai/plan-next-steps
Header: X-Clerk-ID: <user_id>
```

**Response (200):**
```json
{
  "status": "Plan generated successfully"
}
```

---

## 📚 Roadmap APIs

### Get Student Roadmap
```http
GET /roadmap
Header: X-Clerk-ID: <user_id>
```

**Response (200):**
```json
{
  "studentId": 1,
  "overallProgress": 45,
  "categories": [
    {
      "name": "DSA",
      "emoji": "📊",
      "completedTopics": 5,
      "totalTopics": 12,
      "topics": [
        {
          "id": 1,
          "name": "Arrays",
          "status": "completed",
          "priority": "high",
          "completionPercentage": 100
        }
      ]
    }
  ]
}
```

### Update Roadmap Topic
```http
PUT /roadmap/{topicId}
Header: X-Clerk-ID: <user_id>
Content-Type: application/json
```

**Request:**
```json
{
  "status": "completed"
}
```

---

## 🎬 Mock Interview APIs

### Start Interview
```http
POST /mock-interview/start
Header: X-Clerk-ID: <user_id>
Content-Type: application/json
```

**Request:**
```json
{
  "type": "dsa"
}
```

**Response (200):**
```json
{
  "interviewId": 1,
  "firstQuestion": {
    "id": 101,
    "questionNumber": 1,
    "question": "Design a system to find median in a stream of numbers",
    "context": "Medium difficulty, 45 minutes"
  }
}
```

### Submit Answer
```http
POST /mock-interview/{interviewId}/submit
Header: X-Clerk-ID: <user_id>
Content-Type: application/json
```

**Request:**
```json
{
  "questionId": 101,
  "answer": "Use two heaps approach..."
}
```

### Get Interview Feedback
```http
GET /mock-interview/{interviewId}/feedback
Header: X-Clerk-ID: <user_id>
```

**Response (200):**
```json
{
  "score": 78,
  "strengths": [
    "Good problem-solving approach",
    "Clear explanation of algorithm"
  ],
  "improvements": [
    "Optimize space complexity",
    "Better edge case handling"
  ]
}
```

### Get Interview History
```http
GET /mock-interview/history
Header: X-Clerk-ID: <user_id>
```

**Response (200):**
```json
[
  {
    "id": 1,
    "type": "dsa",
    "score": 78,
    "completedAt": "2024-01-20T14:30:00"
  }
]
```

---

## Error Responses

### 400 Bad Request
```json
{
  "error": "Invalid request data",
  "message": "Missing required fields"
}
```

### 401 Unauthorized
```json
{
  "error": "Unauthorized",
  "message": "Invalid or missing authentication token"
}
```

### 404 Not Found
```json
{
  "error": "Not Found",
  "message": "Resource does not exist"
}
```

### 500 Internal Server Error
```json
{
  "error": "Internal Server Error",
  "message": "An unexpected error occurred"
}
```

---

## Rate Limiting

Currently no rate limiting is enforced in development.

## CORS

Frontend CORS is enabled for `http://localhost:3000`

## Versioning

Current API version: v1 (implicit)

---

Generated: January 31, 2024
