package com.smartboard.repository;

import com.smartboard.model.TaskLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskLogRepository extends MongoRepository<TaskLog, String> {

    List<TaskLog> findByUserId(String userId);

    void deleteByUserId(String userId);

    List<TaskLog> findByAutoDeletedAtBefore(LocalDateTime cutoff);

    long countByUserIdAndCreatedAtAfter(String userId, LocalDateTime since);
}
