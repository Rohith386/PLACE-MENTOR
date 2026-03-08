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
        StudentDTO student = studentService.getStudentProfile(clerkId);
        return student != null ? ResponseEntity.ok(student) : ResponseEntity.notFound().build();
    }

    @PostMapping("/profile")
    public ResponseEntity<StudentDTO> createProfile(
            @RequestHeader("X-Clerk-ID") String clerkId,
            @RequestBody StudentDTO studentDTO) {
        StudentDTO created = studentService.createOrUpdateStudent(
            clerkId,
            studentDTO.getEmail(),
            studentDTO.getFirstName(),
            studentDTO.getLastName()
        );
        return ResponseEntity.ok(created);
    }

    @PutMapping("/profile")
    public ResponseEntity<StudentDTO> updateProfile(
            @RequestHeader("X-Clerk-ID") String clerkId,
            @RequestBody StudentDTO studentDTO) {
        StudentDTO updated = studentService.updateProfile(clerkId, studentDTO);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @GetMapping("/progress")
    public ResponseEntity<StudentDTO> getProgress(@RequestHeader("X-Clerk-ID") String clerkId) {
        StudentDTO student = studentService.getStudentProfile(clerkId);
        return student != null ? ResponseEntity.ok(student) : ResponseEntity.notFound().build();
    }
}
