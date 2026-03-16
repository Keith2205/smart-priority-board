package com.smartboard.repository;

import com.smartboard.model.TaskPattern;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskPatternRepository extends MongoRepository<TaskPattern, String> {

    Optional<TaskPattern> findByUserIdAndPatternKey(String userId, String patternKey);

    List<TaskPattern> findByUserIdAndOccurrencesGreaterThanEqual(String userId, int minOccurrences);

    List<TaskPattern> findByUserIdAndTypicalDayOfWeek(String userId, String dayOfWeek);
}
