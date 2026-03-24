package com.placementtracker.repository;

import com.placementtracker.entity.TopicStrength;
import com.placementtracker.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TopicStrengthRepository extends JpaRepository<TopicStrength, Long> {
    List<TopicStrength> findByStudent(Student student);
    List<TopicStrength> findByStudentAndCategory(Student student, String category);
    Optional<TopicStrength> findByStudentAndTopicName(Student student, String topicName);
}
