package com.placementtracker.service;

import com.placementtracker.dto.*;
import com.placementtracker.entity.*;
import com.placementtracker.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class MockInterviewService {
    private final MockInterviewRepository mockInterviewRepository;
    private final MockInterviewQuestionRepository questionRepository;
    private final MockInterviewAnswerRepository answerRepository;
    private final StudentRepository studentRepository;

    public MockInterviewService(
            MockInterviewRepository mockInterviewRepository,
            MockInterviewQuestionRepository questionRepository,
            MockInterviewAnswerRepository answerRepository,
            StudentRepository studentRepository) {
        this.mockInterviewRepository = mockInterviewRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * Start a new mock interview session
     */
    public MockInterviewDTO startInterview(String clerkId, String interviewType) {
        log.info("Starting {} interview for student: {}", interviewType, clerkId);

        Optional<Student> studentOpt = studentRepository.findByClerkId(clerkId);
        if (studentOpt.isEmpty()) {
            throw new RuntimeException("Student not found");
        }

        Student student = studentOpt.get();
        MockInterview interview = new MockInterview();
        interview.setStudent(student);
        interview.setType(interviewType);
        interview.setStartedAt(LocalDateTime.now());
        interview.setScore(0);

        MockInterview savedInterview = mockInterviewRepository.save(interview);

        // Generate questions for the interview
        generateInterviewQuestions(savedInterview, interviewType);

        // Update student's mock interview count
        student.setMockInterviews(student.getMockInterviews() + 1);
        studentRepository.save(student);

        return convertToDTO(savedInterview);
    }

    /**
     * Generate interview questions based on type
     */
    private void generateInterviewQuestions(MockInterview interview, String interviewType) {
        int questionCount = getQuestionCount(interviewType);
        List<String> questions = generateQuestionsAI(interviewType, questionCount);

        for (int i = 0; i < questions.size(); i++) {
            MockInterviewQuestion question = new MockInterviewQuestion();
            question.setInterview(interview);
            question.setQuestionNumber(i + 1);
            question.setType(interviewType);
            question.setQuestion(questions.get(i));
            question.setDifficulty((i % 3) + 1); // Vary difficulty
            question.setContext(getContextForQuestion(interviewType, i));

            questionRepository.save(question);
        }
        log.info("Generated {} questions for interview: {}", questionCount, interview.getId());
    }

    /**
     * Get first question of the interview
     */
    public MockInterviewQuestionDTO getFirstQuestion(Long interviewId) {
        Optional<MockInterview> interviewOpt = mockInterviewRepository.findById(interviewId);
        if (interviewOpt.isEmpty()) {
            throw new RuntimeException("Interview not found");
        }

        List<MockInterviewQuestion> questions = questionRepository.findByInterview(interviewOpt.get());
        if (questions.isEmpty()) {
            throw new RuntimeException("No questions found for interview");
        }

        return convertQuestionToDTO(questions.get(0));
    }

    /**
     * Submit answer for a question
     */
    @Transactional
    public MockInterviewAnswerDTO submitAnswer(String clerkId, Long interviewId, Long questionId, String studentAnswer) {
        log.info("Submitting answer for interview: {}, question: {}", interviewId, questionId);

        Optional<Student> studentOpt = studentRepository.findByClerkId(clerkId);
        if (studentOpt.isEmpty()) {
            throw new RuntimeException("Student not found");
        }

        Optional<MockInterview> interviewOpt = mockInterviewRepository.findById(interviewId);
        Optional<MockInterviewQuestion> questionOpt = questionRepository.findById(questionId);

        if (interviewOpt.isEmpty() || questionOpt.isEmpty()) {
            throw new RuntimeException("Interview or question not found");
        }

        MockInterview interview = interviewOpt.get();
        MockInterviewQuestion question = questionOpt.get();

        // Verify the interview belongs to the student
        if (!interview.getStudent().getClerkId().equals(clerkId)) {
            throw new RuntimeException("Unauthorized: Interview does not belong to this student");
        }

        // Check if answer already exists
        Optional<MockInterviewAnswer> existingAnswer = answerRepository.findByQuestion(question);
        if (existingAnswer.isPresent()) {
            throw new RuntimeException("Answer already submitted for this question");
        }

        // Validate student answer
        if (studentAnswer == null || studentAnswer.trim().isEmpty()) {
            throw new RuntimeException("Student answer cannot be empty");
        }

        // Generate AI feedback and scoring
        Map<String, Object> feedbackData = generateAIFeedback(question, studentAnswer);

        MockInterviewAnswer answer = new MockInterviewAnswer();
        answer.setInterview(interview);
        answer.setQuestion(question);
        answer.setStudentAnswer(studentAnswer);
        answer.setScoreObtained((Integer) feedbackData.get("score"));
        answer.setAiAnalysis((String) feedbackData.get("analysis"));
        answer.setSuggestions((String) feedbackData.get("suggestions"));
        answer.setSubmittedAt(LocalDateTime.now());

        MockInterviewAnswer savedAnswer = answerRepository.save(answer);

        // Update interview score - get fresh list before calculating
        List<MockInterviewAnswer> allAnswers = answerRepository.findByInterview(interview);
        if (!allAnswers.isEmpty()) {
            int totalScore = allAnswers.stream().mapToInt(MockInterviewAnswer::getScoreObtained).sum();
            int averageScore = totalScore / allAnswers.size();
            interview.setScore(averageScore);
            mockInterviewRepository.save(interview);
        }

        return convertAnswerToDTO(savedAnswer);
    }

    /**
     * Get interview feedback
     */
    public MockInterviewDTO getInterviewFeedback(String clerkId, Long interviewId) {
        Optional<MockInterview> interviewOpt = mockInterviewRepository.findById(interviewId);
        if (interviewOpt.isEmpty()) {
            throw new RuntimeException("Interview not found");
        }

        MockInterview interview = interviewOpt.get();
        
        // Verify the interview belongs to the student
        if (!interview.getStudent().getClerkId().equals(clerkId)) {
            throw new RuntimeException("Unauthorized: Interview does not belong to this student");
        }

        List<MockInterviewQuestion> questions = questionRepository.findByInterview(interview);
        List<MockInterviewAnswer> answers = answerRepository.findByInterview(interview);

        MockInterviewDTO dto = convertToDTO(interview);
        dto.setTotalQuestions(questions.size());
        dto.setAnsweredQuestions(answers.size());
        dto.setStatus(answers.size() == questions.size() ? "completed" : "in-progress");

        return dto;
    }

    /**
     * Get interview history for a student
     */
    public List<MockInterviewDTO> getInterviewHistory(String clerkId) {
        Optional<Student> studentOpt = studentRepository.findByClerkId(clerkId);
        if (studentOpt.isEmpty()) {
            throw new RuntimeException("Student not found");
        }

        List<MockInterview> interviews = mockInterviewRepository.findByStudent(studentOpt.get());
        return interviews.stream().map(this::convertToDTO).toList();
    }

    /**
     * Get interview details with all answers
     */
    public Map<String, Object> getInterviewDetails(String clerkId, Long interviewId) {
        Optional<MockInterview> interviewOpt = mockInterviewRepository.findById(interviewId);
        if (interviewOpt.isEmpty()) {
            throw new RuntimeException("Interview not found");
        }

        MockInterview interview = interviewOpt.get();
        
        // Verify the interview belongs to the student
        if (!interview.getStudent().getClerkId().equals(clerkId)) {
            throw new RuntimeException("Unauthorized: Interview does not belong to this student");
        }

        List<MockInterviewQuestion> questions = questionRepository.findByInterview(interview);
        List<MockInterviewAnswer> answers = answerRepository.findByInterview(interview);

        Map<String, Object> details = new HashMap<>();
        details.put("interview", convertToDTO(interview));
        details.put("questionsCount", questions.size());
        details.put("answersCount", answers.size());
        details.put("answers", answers.stream().map(this::convertAnswerToDTO).toList());

        return details;
    }

    /**
     * Get next question
     */
    public MockInterviewQuestionDTO getNextQuestion(Long interviewId, Long currentQuestionId) {
        Optional<MockInterview> interviewOpt = mockInterviewRepository.findById(interviewId);
        if (interviewOpt.isEmpty()) {
            throw new RuntimeException("Interview not found");
        }

        List<MockInterviewQuestion> questions = questionRepository.findByInterview(interviewOpt.get());
        Optional<MockInterviewQuestion> currentQuestion = questions.stream()
                .filter(q -> q.getId().equals(currentQuestionId))
                .findFirst();

        if (currentQuestion.isEmpty()) {
            throw new RuntimeException("Current question not found");
        }

        int currentIndex = questions.indexOf(currentQuestion.get());
        if (currentIndex < questions.size() - 1) {
            return convertQuestionToDTO(questions.get(currentIndex + 1));
        }

        return null; // No more questions
    }

    // ============ Helper Methods ============

    private int getQuestionCount(String interviewType) {
        return switch (interviewType) {
            case "dsa" -> 3;
            case "behavioral" -> 4;
            case "hr" -> 3;
            default -> 3;
        };
    }

    private String getContextForQuestion(String interviewType, int index) {
        return switch (interviewType) {
            case "dsa" -> "Problem involves data structures and algorithms";
            case "behavioral" -> "Tell me about your experience with " + (index % 2 == 0 ? "teamwork" : "problem solving");
            case "hr" -> "General HR question about your goals and motivation";
            default -> "";
        };
    }

    private List<String> generateQuestionsAI(String interviewType, int count) {
        // Using default questions for now
        // Can be enhanced later with AI integration
        return getDefaultQuestions(interviewType, count);
    }

    private List<String> getDefaultQuestions(String interviewType, int count) {
        List<String> defaultQuestions = switch (interviewType) {
            case "dsa" -> Arrays.asList(
                    "Write a function to find the first non-repeating character in a string",
                    "Implement binary search algorithm",
                    "Find the longest substring without repeating characters"
            );
            case "behavioral" -> Arrays.asList(
                    "Tell me about a time you faced a challenging problem",
                    "How do you handle criticism from colleagues?",
                    "Describe a situation where you had to work in a team",
                    "What is your biggest strength?"
            );
            case "hr" -> Arrays.asList(
                    "Why do you want to join our company?",
                    "Where do you see yourself in 5 years?",
                    "What are your salary expectations?"
            );
            default -> Arrays.asList("Default question 1", "Default question 2", "Default question 3");
        };

        return defaultQuestions.stream().limit(count).toList();
    }

    private Map<String, Object> generateAIFeedback(MockInterviewQuestion question, String studentAnswer) {
        Map<String, Object> feedback = new HashMap<>();

        int score = calculateScore(studentAnswer, question);
        String analysis = generateAnalysis(question.getType(), studentAnswer, score);
        String suggestions = generateSuggestions(question.getType(), score);

        feedback.put("score", score);
        feedback.put("analysis", analysis);
        feedback.put("suggestions", suggestions);

        return feedback;
    }

    private String generateAnalysis(String type, String answer, int score) {
        if (score >= 80) {
            return "Excellent answer! You demonstrated a strong understanding of the " + type + " concept and provided clear explanations.";
        } else if (score >= 60) {
            return "Good attempt! Your answer shows understanding but could be more comprehensive. Consider adding more details or examples.";
        } else if (score >= 40) {
            return "Your answer covers the basics. To improve, focus on clarity and completeness in your response.";
        } else {
            return "This question needs more preparation. Review the core concepts and practice similar problems.";
        }
    }

    private String generateSuggestions(String type, int score) {
        return switch (type) {
            case "dsa" -> "Practice similar DSA problems on LeetCode. Focus on time and space complexity analysis.";
            case "behavioral" -> "Use the STAR method (Situation, Task, Action, Result) for better storytelling.";
            case "hr" -> "Prepare stories about your achievements, challenges, and lessons learned.";
            default -> "Keep practicing and reviewing similar concepts.";
        };
    }

    private int calculateScore(String answer, MockInterviewQuestion question) {
        // Simple scoring based on answer length and content
        if (answer == null || answer.isEmpty()) return 0;
        if (answer.length() < 20) return 40;
        if (answer.length() < 100) return 60;
        if (answer.length() < 300) return 75;
        return 85;
    }

    private MockInterviewDTO convertToDTO(MockInterview interview) {
        MockInterviewDTO dto = new MockInterviewDTO();
        dto.setId(interview.getId());
        dto.setType(interview.getType());
        dto.setScore(interview.getScore());
        dto.setFeedback(interview.getFeedback());
        dto.setStartedAt(interview.getStartedAt());
        dto.setCompletedAt(interview.getCompletedAt());
        return dto;
    }

    private MockInterviewQuestionDTO convertQuestionToDTO(MockInterviewQuestion question) {
        MockInterviewQuestionDTO dto = new MockInterviewQuestionDTO();
        dto.setId(question.getId());
        dto.setInterviewId(question.getInterview().getId());
        dto.setQuestionNumber(question.getQuestionNumber());
        dto.setType(question.getType());
        dto.setQuestion(question.getQuestion());
        dto.setContext(question.getContext());
        dto.setDifficulty(question.getDifficulty());
        return dto;
    }

    private MockInterviewAnswerDTO convertAnswerToDTO(MockInterviewAnswer answer) {
        MockInterviewAnswerDTO dto = new MockInterviewAnswerDTO();
        dto.setId(answer.getId());
        dto.setQuestionId(answer.getQuestion().getId());
        dto.setInterviewId(answer.getInterview().getId());
        dto.setStudentAnswer(answer.getStudentAnswer());
        dto.setScoreObtained(answer.getScoreObtained());
        dto.setAiAnalysis(answer.getAiAnalysis());
        dto.setSuggestions(answer.getSuggestions());
        dto.setSubmittedAt(answer.getSubmittedAt() != null ? answer.getSubmittedAt().toString() : "");
        return dto;
    }
}
