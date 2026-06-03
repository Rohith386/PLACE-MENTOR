package com.placementtracker.repository;

import com.placementtracker.entity.MockInterviewQuestion;
import com.placementtracker.entity.MockInterview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MockInterviewQuestionRepository extends JpaRepository<MockInterviewQuestion, Long> {
    List<MockInterviewQuestion> findByInterview(MockInterview interview);
    List<MockInterviewQuestion> findByInterviewAndType(MockInterview interview, String type);
}
