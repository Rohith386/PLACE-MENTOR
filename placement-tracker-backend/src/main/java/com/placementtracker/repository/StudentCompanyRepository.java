package com.placementtracker.repository;

import com.placementtracker.entity.StudentCompany;
import com.placementtracker.entity.Student;
import com.placementtracker.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentCompanyRepository extends JpaRepository<StudentCompany, Long> {
    List<StudentCompany> findByStudent(Student student);
    Optional<StudentCompany> findByStudentAndCompany(Student student, Company company);
    void deleteByStudentAndCompany(Student student, Company company);
}
