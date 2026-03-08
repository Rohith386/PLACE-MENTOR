-- Placement Tracker Database Schema
-- MySQL Setup

DROP DATABASE IF EXISTS placement_tracker;
CREATE DATABASE placement_tracker;
USE placement_tracker;

-- Students Table
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
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX (clerk_id),
    INDEX (email)
);

-- Companies Table
CREATE TABLE companies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    logo VARCHAR(255),
    base_probability INT DEFAULT 50,
    interview_pattern VARCHAR(255),
    common_questions TEXT
);

-- Student Companies (Many-to-Many)
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

-- Roadmap Topics Table
CREATE TABLE roadmap_topics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    category VARCHAR(100),
    topic_name VARCHAR(255),
    status VARCHAR(50) DEFAULT 'not-started',
    priority VARCHAR(20) DEFAULT 'medium',
    completion_percentage INT DEFAULT 0,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    INDEX (student_id, category)
);

-- Mock Interviews Table
CREATE TABLE mock_interviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    type VARCHAR(50),
    score INT DEFAULT 0,
    feedback TEXT,
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP NULL,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    INDEX (student_id, type)
);

-- Insert Default Companies
INSERT INTO companies (name, description, logo, base_probability, interview_pattern) VALUES
('Google', 'Search and Advertising Giant', 'google.png', 45, 'Phone Screen -> DSA Round -> System Design -> HR'),
('Amazon', 'E-commerce and Cloud Leader', 'amazon.png', 55, 'Online Assessment -> DSA Round -> System Design -> Bar Raiser'),
('Microsoft', 'Cloud and Enterprise Software', 'microsoft.png', 50, 'Phone Screen -> DSA Round -> System Design -> HR'),
('Facebook (Meta)', 'Social Media and VR', 'meta.png', 48, 'Phone Screen -> DSA Round -> System Design -> Behavioral'),
('Apple', 'Hardware and Ecosystem', 'apple.png', 40, 'Phone Screen -> DSA Round -> System Design -> HR'),
('Tesla', 'Electric Vehicles and Energy', 'tesla.png', 35, 'Technical Round -> Onsite -> Executive Interview'),
('Netflix', 'Streaming and Entertainment', 'netflix.png', 42, 'Phone Screen -> DSA Round -> System Design -> HR'),
('LinkedIn', 'Professional Network', 'linkedin.png', 50, 'Phone Screen -> DSA Round -> System Design -> HR');

SELECT 'Database created successfully!' AS Status;
