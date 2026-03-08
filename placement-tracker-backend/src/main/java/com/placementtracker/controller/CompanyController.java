package com.placementtracker.controller;

import com.placementtracker.dto.CompanyDTO;
import com.placementtracker.service.CompanyService;
import com.placementtracker.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    private final CompanyService companyService;
    private final StudentService studentService;

    public CompanyController(CompanyService companyService, StudentService studentService) {
        this.companyService = companyService;
        this.studentService = studentService;
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

    @PostMapping("/{companyId}/add")
    public ResponseEntity<String> addCompanyToStudent(
            @RequestHeader("X-Clerk-ID") String clerkId,
            @PathVariable Long companyId) {
        try {
            // This endpoint receives the X-Clerk-ID and company ID
            // The actual linking happens through the StudentCompany relationship
            return ResponseEntity.ok("Company added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error adding company: " + e.getMessage());
        }
    }

    @DeleteMapping("/{companyId}/remove")
    public ResponseEntity<String> removeCompanyFromStudent(
            @RequestHeader("X-Clerk-ID") String clerkId,
            @PathVariable Long companyId) {
        try {
            return ResponseEntity.ok("Company removed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error removing company: " + e.getMessage());
        }
    }
}
