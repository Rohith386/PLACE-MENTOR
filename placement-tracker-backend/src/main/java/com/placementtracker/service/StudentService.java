package com.placementtracker.service;

import com.placementtracker.dto.StudentDTO;
import com.placementtracker.entity.Student;
import com.placementtracker.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public StudentDTO getStudentProfile(String clerkId) {
        Optional<Student> student = studentRepository.findByClerkId(clerkId);
        return student.map(this::convertToDTO).orElse(null);
    }

    public StudentDTO createOrUpdateStudent(String clerkId, String email, String firstName, String lastName) {
        Optional<Student> existingStudent = studentRepository.findByClerkId(clerkId);

        Student student = existingStudent.orElseGet(() -> {
            Student newStudent = new Student();
            newStudent.setClerkId(clerkId);
            newStudent.setEmail(email);
            return newStudent;
        });

        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setUpdatedAt(LocalDateTime.now());

        Student savedStudent = studentRepository.save(student);
        return convertToDTO(savedStudent);
    }

    public StudentDTO updateProfile(String clerkId, StudentDTO updatedDTO) {
        Optional<Student> student = studentRepository.findByClerkId(clerkId);

        if (student.isPresent()) {
            Student s = student.get();
            s.setBranch(updatedDTO.getBranch());
            s.setYear(updatedDTO.getYear());
            s.setSkillLevel(updatedDTO.getSkillLevel());
            s.setUpdatedAt(LocalDateTime.now());

            Student savedStudent = studentRepository.save(s);
            return convertToDTO(savedStudent);
        }

        return null;
    }

    public void updateReadinessScore(String clerkId, Integer score) {
        Optional<Student> student = studentRepository.findByClerkId(clerkId);
        student.ifPresent(s -> {
            s.setReadinessScore(score);
            s.setUpdatedAt(LocalDateTime.now());
            studentRepository.save(s);
        });
    }

    private StudentDTO convertToDTO(Student student) {
        return new StudentDTO(
            student.getId(),
            student.getFirstName(),
            student.getLastName(),
            student.getEmail(),
            student.getBranch(),
            student.getYear(),
            student.getSkillLevel(),
            student.getReadinessScore(),
            student.getTopicsCompleted(),
            student.getProblemsSolved(),
            student.getMockInterviews(),
            student.getCreatedAt()
        );
    }
}
