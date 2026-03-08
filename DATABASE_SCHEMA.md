# Placement Tracker - Database Schema Documentation

## Overview
Complete MySQL database schema for the Placement Tracker application.

## Tables

### 1. students
Stores student profile information and progress metrics.

```sql
CREATE TABLE students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    clerk_id VARCHAR(255) UNIQUE NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(255) UNIQUE NOT NULL,
    branch VARCHAR(100),
    year INT,
    skill_level VARCHAR(50),
    readiness_score INT DEFAULT 0,
    topics_completed INT DEFAULT 0,
    problems_solved INT DEFAULT 0,
    mock_interviews INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

**Fields:**
- `clerk_id` - Unique identifier from Clerk authentication
- `branch` - Academic branch (CSE, ECE, Mechanical, etc.)
- `year` - Academic year (1-4)
- `skill_level` - Proficiency (beginner, intermediate, advanced)
- `readiness_score` - 0-100, overall preparation score
- `topics_completed` - Count of finished topics
- `problems_solved` - Total DSA problems solved
- `mock_interviews` - Number of mock interviews attended

---

### 2. companies
Stores information about target companies.

```sql
CREATE TABLE companies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    logo VARCHAR(255),
    base_probability INT DEFAULT 50,
    interview_pattern VARCHAR(255),
    common_questions TEXT
);
```

**Fields:**
- `name` - Company name (Google, Amazon, Microsoft, etc.)
- `description` - Brief company description
- `logo` - Logo image URL
- `base_probability` - Base crack probability (50%)
- `interview_pattern` - Typical interview rounds
- `common_questions` - Frequently asked questions (JSON format)

**Pre-populated Companies:**
- Google
- Amazon
- Microsoft
- Meta (Facebook)
- Apple
- Tesla
- Netflix
- LinkedIn

---

### 3. student_companies
Junction table linking students to their dream companies.

```sql
CREATE TABLE student_companies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    company_id BIGINT NOT NULL,
    probability INT DEFAULT 50,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE CASCADE,
    UNIQUE KEY (student_id, company_id)
);
```

**Fields:**
- `student_id` - Reference to students table
- `company_id` - Reference to companies table
- `probability` - Dynamic crack probability (calculated by AI)
- `added_at` - When student selected this company

---

### 4. roadmap_topics
Tracks learning roadmap for each student.

```sql
CREATE TABLE roadmap_topics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    category VARCHAR(100),
    topic_name VARCHAR(255),
    status VARCHAR(50) DEFAULT 'not-started',
    priority VARCHAR(20) DEFAULT 'medium',
    completion_percentage INT DEFAULT 0,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
);
```

**Fields:**
- `student_id` - Reference to students table
- `category` - Topic category (DSA, CS Fundamentals, Projects, etc.)
- `topic_name` - Specific topic name
- `status` - not-started, in-progress, completed
- `priority` - high, medium, low
- `completion_percentage` - 0-100 progress

**Categories:**
1. **DSA** (Arrays, Strings, Trees, Graphs, DP, etc.)
2. **CS Fundamentals** (OS, DBMS, CN, OOPS)
3. **Projects** (Beginner → Advanced)
4. **Aptitude** (Quantitative, Logical, Verbal)
5. **Soft Skills** (Communication, Leadership, etc.)

---

### 5. mock_interviews
Stores mock interview history and results.

```sql
CREATE TABLE mock_interviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    type VARCHAR(50),
    score INT DEFAULT 0,
    feedback TEXT,
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP NULL,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
);
```

**Fields:**
- `student_id` - Reference to students table
- `type` - dsa, behavioral, hr
- `score` - 0-100 interview score
- `feedback` - AI-generated feedback (JSON format)
- `started_at` - Interview start time
- `completed_at` - Interview completion time

**Interview Types:**
- **DSA** - Coding and algorithm problems
- **Behavioral** - HR behavioral questions
- **HR** - General HR and culture fit

---

## Indexes

For performance optimization:

```sql
CREATE INDEX idx_students_clerk_id ON students(clerk_id);
CREATE INDEX idx_student_companies_student_id ON student_companies(student_id);
CREATE INDEX idx_roadmap_topics_student_id ON roadmap_topics(student_id);
CREATE INDEX idx_mock_interviews_student_id ON mock_interviews(student_id);
```

---

## Entity Relationships

```
students (1) --- (N) student_companies
  ↓
  (1) --- (N) roadmap_topics
  ↓
  (1) --- (N) mock_interviews

companies (1) --- (N) student_companies
```

---

## Sample Data

### Sample Student Profile
```json
{
  "id": 1,
  "clerk_id": "user_1234567890",
  "first_name": "Raj",
  "last_name": "Kumar",
  "email": "raj@example.com",
  "branch": "CSE",
  "year": 3,
  "skill_level": "intermediate",
  "readiness_score": 65,
  "topics_completed": 12,
  "problems_solved": 145,
  "mock_interviews": 3
}
```

### Sample Dream Company Selection
```json
{
  "student_id": 1,
  "company_id": 1,  // Google
  "probability": 45,
  "added_at": "2024-01-20T10:30:00"
}
```

### Sample Roadmap Topic
```json
{
  "student_id": 1,
  "category": "DSA",
  "topic_name": "Dynamic Programming",
  "status": "in-progress",
  "priority": "high",
  "completion_percentage": 60
}
```

### Sample Mock Interview
```json
{
  "student_id": 1,
  "type": "dsa",
  "score": 78,
  "feedback": "{\"strengths\": [...], \"improvements\": [...]}",
  "started_at": "2024-01-20T14:00:00",
  "completed_at": "2024-01-20T14:45:00"
}
```

---

## Queries

### Find student with highest readiness score
```sql
SELECT * FROM students ORDER BY readiness_score DESC LIMIT 1;
```

### Get all companies for a specific student
```sql
SELECT c.* FROM companies c
JOIN student_companies sc ON c.id = sc.company_id
WHERE sc.student_id = 1;
```

### Get incomplete topics for a student
```sql
SELECT * FROM roadmap_topics
WHERE student_id = 1 AND status != 'completed';
```

### Average mock interview score by type
```sql
SELECT type, AVG(score) as avg_score
FROM mock_interviews
WHERE student_id = 1
GROUP BY type;
```

---

## Data Integrity

- Foreign key constraints ensure referential integrity
- `ON DELETE CASCADE` removes related records when student is deleted
- `UNIQUE` constraint on `(student_id, company_id)` prevents duplicate selections
- Timestamps automatically track record creation/updates

---

## Scalability Considerations

1. **Partitioning** - Consider time-based partitioning for mock_interviews
2. **Archiving** - Archive old records after 2 years
3. **Caching** - Cache readiness scores and company probabilities
4. **Denormalization** - Store probability in student_companies for faster queries

---

## Backup & Recovery

Recommended backup strategy:
```bash
# Daily backup
mysqldump -u root -p placement_tracker > backup_$(date +%Y%m%d).sql

# Restore
mysql -u root -p placement_tracker < backup_20240131.sql
```

---

Generated: January 31, 2024
