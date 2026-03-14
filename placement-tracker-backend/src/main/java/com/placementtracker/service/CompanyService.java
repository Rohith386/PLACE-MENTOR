package com.placementtracker.service;

import com.placementtracker.dto.CompanyDTO;
import com.placementtracker.entity.Company;
import com.placementtracker.entity.Student;
import com.placementtracker.entity.StudentCompany;
import com.placementtracker.repository.CompanyRepository;
import com.placementtracker.repository.StudentCompanyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final StudentCompanyRepository studentCompanyRepository;

    public CompanyService(CompanyRepository companyRepository, StudentCompanyRepository studentCompanyRepository) {
        this.companyRepository = companyRepository;
        this.studentCompanyRepository = studentCompanyRepository;
    }

    public List<CompanyDTO> getAllCompanies() {
        return companyRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public CompanyDTO getCompany(Long companyId) {
        return companyRepository.findById(companyId)
            .map(this::convertToDTO)
            .orElse(null);
    }

    /**
     * Get all companies selected by a student for their dream list
     */
    public List<CompanyDTO> getStudentCompanies(Student student) {
        return studentCompanyRepository.findByStudent(student).stream()
            .map(sc -> convertStudentCompanyToDTO(sc))
            .collect(Collectors.toList());
    }

    /**
     * Add a company to student's dream list
     */
    public void addCompanyToStudent(Student student, Long companyId) {
        Optional<Company> company = companyRepository.findById(companyId);
        if (company.isPresent()) {
            // Check if already added
            Optional<StudentCompany> existing = studentCompanyRepository.findByStudentAndCompany(student, company.get());
            if (existing.isEmpty()) {
                StudentCompany studentCompany = new StudentCompany();
                studentCompany.setStudent(student);
                studentCompany.setCompany(company.get());
                studentCompany.setProbability(company.get().getBaseProbability());
                studentCompanyRepository.save(studentCompany);
                log.info("Company {} added to student {}", companyId, student.getId());
            }
        }
    }

    /**
     * Remove a company from student's dream list
     */
    public void removeCompanyFromStudent(Student student, Long companyId) {
        Optional<Company> company = companyRepository.findById(companyId);
        if (company.isPresent()) {
            studentCompanyRepository.deleteByStudentAndCompany(student, company.get());
            log.info("Company {} removed from student {}", companyId, student.getId());
        }
    }

    private CompanyDTO convertToDTO(Company company) {
        return new CompanyDTO(
            company.getId(),
            company.getName(),
            company.getDescription(),
            company.getLogo(),
            company.getBaseProbability(),
            company.getInterviewPattern()
        );
    }

    private CompanyDTO convertStudentCompanyToDTO(StudentCompany studentCompany) {
        Company company = studentCompany.getCompany();
        return new CompanyDTO(
            company.getId(),
            company.getName(),
            company.getDescription(),
            company.getLogo(),
            studentCompany.getProbability(),
            company.getInterviewPattern()
        );
    }
}
