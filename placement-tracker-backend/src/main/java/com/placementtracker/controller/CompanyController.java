package com.placementtracker.controller;

import com.placementtracker.dto.CompanyDTO;
import com.placementtracker.entity.Student;
import com.placementtracker.repository.StudentRepository;
import com.placementtracker.service.CompanyService;
import com.placementtracker.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/companies")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class CompanyController {
    private final CompanyService companyService;
    private final StudentService studentService;
    private final StudentRepository studentRepository;

    public CompanyController(CompanyService companyService, StudentService studentService, StudentRepository studentRepository) {
        this.companyService = companyService;
        this.studentService = studentService;
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyDTO> getCompany(@PathVariable Long companyId) {
        CompanyDTO company = companyService.getCompany(companyId);
        return company != null ? ResponseEntity.ok(company) : ResponseEntity.notFound().build();
    }

    /**
     * Get all companies selected by the student
     */
    @GetMapping("/student/selected")
    public ResponseEntity<?> getStudentCompanies(@RequestHeader("X-Clerk-ID") String clerkId) {
        try {
            Optional<Student> student = studentRepository.findByClerkId(clerkId);
            if (student.isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Student not found");
                return ResponseEntity.status(404).body(response);
            }
            
            List<CompanyDTO> companies = companyService.getStudentCompanies(student.get());
            return ResponseEntity.ok(companies);
        } catch (Exception e) {
            log.error("Error fetching student companies: ", e);
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to fetch companies: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Add company to student's dream list
     */
    @PostMapping("/{companyId}/add")
    public ResponseEntity<?> addCompanyToStudent(
            @RequestHeader("X-Clerk-ID") String clerkId,
            @PathVariable Long companyId) {
        try {
            Optional<Student> student = studentRepository.findByClerkId(clerkId);
            if (student.isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Student not found");
                return ResponseEntity.status(404).body(response);
            }
            
            companyService.addCompanyToStudent(student.get(), companyId);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Company added successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error adding company: ", e);
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error adding company: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Remove company from student's dream list
     */
    @DeleteMapping("/{companyId}/remove")
    public ResponseEntity<?> removeCompanyFromStudent(
            @RequestHeader("X-Clerk-ID") String clerkId,
            @PathVariable Long companyId) {
        try {
            Optional<Student> student = studentRepository.findByClerkId(clerkId);
            if (student.isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Student not found");
                return ResponseEntity.status(404).body(response);
            }
            
            companyService.removeCompanyFromStudent(student.get(), companyId);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Company removed successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error removing company: ", e);
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error removing company: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
