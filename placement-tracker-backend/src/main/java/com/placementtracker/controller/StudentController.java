package com.placementtracker.controller;

import com.placementtracker.dto.StudentDTO;
import com.placementtracker.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
@Slf4j
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/profile")
    public ResponseEntity<StudentDTO> getProfile(@RequestHeader("X-Clerk-ID") String clerkId) {
        if (clerkId == null || clerkId.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        StudentDTO student = studentService.getStudentProfile(clerkId);
        if (student != null) {
            return ResponseEntity.ok(student);
        }

        return ResponseEntity.ok(StudentDTO.empty());
    }

    @PostMapping("/profile")
    public ResponseEntity<StudentDTO> createProfile(
            @RequestHeader("X-Clerk-ID") String clerkId,
            @RequestBody StudentDTO studentDTO) {
        if (clerkId == null || clerkId.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        StudentDTO created = studentService.createOrUpdateStudent(
            clerkId,
            studentDTO != null ? studentDTO.getEmail() : "",
            studentDTO != null ? studentDTO.getFirstName() : "",
            studentDTO != null ? studentDTO.getLastName() : ""
        );
        return ResponseEntity.ok(created);
    }

    @PutMapping("/profile")
    public ResponseEntity<StudentDTO> updateProfile(
            @RequestHeader("X-Clerk-ID") String clerkId,
            @RequestBody StudentDTO studentDTO) {
        if (clerkId == null || clerkId.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        StudentDTO updated = studentService.updateProfile(clerkId, studentDTO);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.ok(StudentDTO.empty());
    }

    @GetMapping("/progress")
    public ResponseEntity<StudentDTO> getProgress(@RequestHeader("X-Clerk-ID") String clerkId) {
        if (clerkId == null || clerkId.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        StudentDTO student = studentService.getStudentProfile(clerkId);
        return student != null ? ResponseEntity.ok(student) : ResponseEntity.ok(StudentDTO.empty());
    }

    @PostMapping("/progress")
    public ResponseEntity<StudentDTO> updateProgress(
            @RequestHeader("X-Clerk-ID") String clerkId,
            @RequestBody StudentDTO studentDTO) {
        if (clerkId == null || clerkId.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        StudentDTO updated = studentService.updateProfile(clerkId, studentDTO);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.ok(StudentDTO.empty());
    }
}
