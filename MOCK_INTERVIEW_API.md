# Mock Interview API Quick Reference

## Base URL
```
http://localhost:8080/api/mock-interview
```

## Authentication
All requests require the `X-Clerk-ID` header:
```
X-Clerk-ID: {clerk_user_id}
```

---

## Endpoints

### 1. Start Mock Interview
**POST** `/start`

Initiates a new mock interview session.

**Request Body:**
```json
{
  "type": "dsa" | "behavioral" | "hr"
}
```

**Response:**
```json
{
  "interview": {
    "id": 1,
    "type": "dsa",
    "score": 0,
    "feedback": null,
    "startedAt": "2025-06-03T10:30:00",
    "completedAt": null,
    "totalQuestions": null,
    "answeredQuestions": null,
    "status": null
  },
  "firstQuestion": {
    "id": 101,
    "interviewId": 1,
    "questionNumber": 1,
    "type": "dsa",
    "question": "Write a function to reverse a string...",
    "context": "Problem involves string manipulation",
    "difficulty": 1
  }
}
```

**Example cURL:**
```bash
curl -X POST http://localhost:8080/api/mock-interview/start \
  -H "X-Clerk-ID: user123" \
  -H "Content-Type: application/json" \
  -d '{"type": "dsa"}'
```

---

### 2. Submit Answer
**POST** `/{interviewId}/submit`

Submits student answer for a question.

**Request Body:**
```json
{
  "questionId": 101,
  "answer": "Here is my solution to the problem..."
}
```

**Response:**
```json
{
  "answer": {
    "id": 501,
    "questionId": 101,
    "interviewId": 1,
    "studentAnswer": "Here is my solution...",
    "scoreObtained": 75,
    "aiAnalysis": "Your answer demonstrates good understanding...",
    "suggestions": "Practice similar problems on LeetCode...",
    "submittedAt": 1717413000000
  },
  "nextQuestion": {
    "id": 102,
    "interviewId": 1,
    "questionNumber": 2,
    "type": "dsa",
    "question": "Implement binary search...",
    "context": "...",
    "difficulty": 2
  }
}
```

**Note:** If no more questions, `nextQuestion` will be the string `"completed"`

**Example cURL:**
```bash
curl -X POST http://localhost:8080/api/mock-interview/1/submit \
  -H "X-Clerk-ID: user123" \
  -H "Content-Type: application/json" \
  -d '{
    "questionId": 101,
    "answer": "Here is my solution..."
  }'
```

---

### 3. Get Interview Feedback
**GET** `/{interviewId}/feedback`

Retrieves final feedback and scoring for completed interview.

**Response:**
```json
{
  "id": 1,
  "type": "dsa",
  "score": 72,
  "feedback": null,
  "startedAt": "2025-06-03T10:30:00",
  "completedAt": null,
  "totalQuestions": 3,
  "answeredQuestions": 3,
  "status": "completed"
}
```

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/mock-interview/1/feedback \
  -H "X-Clerk-ID: user123"
```

---

### 4. Get Interview History
**GET** `/history`

Retrieves all past interviews for the student.

**Response:**
```json
[
  {
    "id": 1,
    "type": "dsa",
    "score": 72,
    "feedback": null,
    "startedAt": "2025-06-03T10:30:00",
    "completedAt": null,
    "totalQuestions": null,
    "answeredQuestions": null,
    "status": null
  },
  {
    "id": 2,
    "type": "behavioral",
    "score": 78,
    "feedback": null,
    "startedAt": "2025-06-02T14:15:00",
    "completedAt": null,
    "totalQuestions": null,
    "answeredQuestions": null,
    "status": null
  }
]
```

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/mock-interview/history \
  -H "X-Clerk-ID: user123"
```

---

### 5. Get Interview Details
**GET** `/{interviewId}/details`

Retrieves comprehensive details including all questions and answers.

**Response:**
```json
{
  "interview": {
    "id": 1,
    "type": "dsa",
    "score": 72,
    "feedback": null,
    "startedAt": "2025-06-03T10:30:00",
    "completedAt": null,
    "totalQuestions": null,
    "answeredQuestions": null,
    "status": null
  },
  "questionsCount": 3,
  "answersCount": 3,
  "answers": [
    {
      "id": 501,
      "questionId": 101,
      "interviewId": 1,
      "studentAnswer": "...",
      "scoreObtained": 75,
      "aiAnalysis": "...",
      "suggestions": "...",
      "submittedAt": 1717413000000
    },
    {
      "id": 502,
      "questionId": 102,
      "interviewId": 1,
      "studentAnswer": "...",
      "scoreObtained": 70,
      "aiAnalysis": "...",
      "suggestions": "...",
      "submittedAt": 1717413060000
    },
    {
      "id": 503,
      "questionId": 103,
      "interviewId": 1,
      "studentAnswer": "...",
      "scoreObtained": 72,
      "aiAnalysis": "...",
      "suggestions": "...",
      "submittedAt": 1717413120000
    }
  ]
}
```

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/mock-interview/1/details \
  -H "X-Clerk-ID: user123"
```

---

### 6. Get First Question
**GET** `/{interviewId}/first-question`

Retrieves the first question of an interview (useful for resuming).

**Response:**
```json
{
  "id": 101,
  "interviewId": 1,
  "questionNumber": 1,
  "type": "dsa",
  "question": "Write a function to reverse a string...",
  "context": "Problem involves string manipulation",
  "difficulty": 1
}
```

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/mock-interview/1/first-question \
  -H "X-Clerk-ID: user123"
```

---

## Interview Types & Question Count

| Type | Count | Duration |
|------|-------|----------|
| dsa | 3 | 60 min |
| behavioral | 4 | 30 min |
| hr | 3 | 20 min |

---

## Scoring Scale

| Score | Performance |
|-------|-------------|
| 80-100 | Excellent |
| 60-79 | Good |
| 40-59 | Fair |
| 0-39 | Needs Improvement |

---

## Error Responses

### 400 Bad Request
```json
{
  "error": "Invalid request body or parameters"
}
```

### 404 Not Found
```json
{
  "error": "Interview or question not found"
}
```

### 500 Internal Server Error
```json
{
  "error": "Internal server error occurred"
}
```

---

## Frontend Integration

### Using in React Components

```javascript
import { mockInterviewService } from '../services/api'

// Start interview
const response = await mockInterviewService.startInterview('dsa')
const { interview, firstQuestion } = response.data

// Submit answer
const answerResponse = await mockInterviewService.submitAnswer(
  interviewId,
  questionId,
  answer
)

// Get feedback
const feedbackResponse = await mockInterviewService.getInterviewFeedback(interviewId)

// Get history
const historyResponse = await mockInterviewService.getInterviewHistory()
```

---

## Testing Scenarios

### Scenario 1: Complete DSA Interview
1. POST /start with type="dsa"
2. POST /submit for question 1
3. POST /submit for question 2
4. POST /submit for question 3
5. GET /feedback

### Scenario 2: Partial Behavioral Interview
1. POST /start with type="behavioral"
2. POST /submit for question 1
3. POST /submit for question 2
4. (User closes browser)
5. GET /history to see incomplete interview

### Scenario 3: Check Progress
1. GET /history to see all past interviews
2. GET /details for specific interview to review answers

---

## Notes

- All timestamps are in ISO 8601 format for startedAt/completedAt
- All submitted_at values are Unix epoch milliseconds
- Difficulty ratings go from 1 (easiest) to 5 (hardest)
- Scores are averaged across all questions in an interview
- The "completed" response on nextQuestion indicates no more questions
