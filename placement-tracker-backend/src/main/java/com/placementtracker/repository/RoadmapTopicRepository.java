package com.placementtracker.repository;

import com.placementtracker.entity.RoadmapTopic;
import com.placementtracker.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RoadmapTopicRepository extends JpaRepository<RoadmapTopic, Long> {
    List<RoadmapTopic> findByStudent(Student student);
    List<RoadmapTopic> findByStudentAndCategory(Student student, String category);
}
