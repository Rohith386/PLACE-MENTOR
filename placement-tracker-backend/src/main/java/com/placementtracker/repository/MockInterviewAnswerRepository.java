package com.placementtracker.repository;

import com.placementtracker.entity.MockInterviewAnswer;
import com.placementtracker.entity.MockInterview;
import com.placementtracker.entity.MockInterviewQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MockInterviewAnswerRepository extends JpaRepository<MockInterviewAnswer, Long> {
    List<MockInterviewAnswer> findByInterview(MockInterview interview);
    Optional<MockInterviewAnswer> findByQuestion(MockInterviewQuestion question);
}
