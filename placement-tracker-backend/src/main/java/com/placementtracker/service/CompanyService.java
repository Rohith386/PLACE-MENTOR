package com.placementtracker.service;

import com.placementtracker.dto.CompanyDTO;
import com.placementtracker.entity.Company;
import com.placementtracker.repository.CompanyRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
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
}
