package com.placementtracker.repository;

import com.placementtracker.entity.MockInterview;
import com.placementtracker.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MockInterviewRepository extends JpaRepository<MockInterview, Long> {
    List<MockInterview> findByStudent(Student student);
    List<MockInterview> findByStudentAndType(Student student, String type);
}
