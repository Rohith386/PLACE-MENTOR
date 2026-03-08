package com.placementtracker.repository;

import com.placementtracker.entity.StudentCompany;
import com.placementtracker.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StudentCompanyRepository extends JpaRepository<StudentCompany, Long> {
    List<StudentCompany> findByStudent(Student student);
}
