package com.smartboard.repository;

import com.smartboard.model.Task;
import com.smartboard.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {

    Page<Task> findByUserId(String userId, Pageable pageable);

    Page<Task> findByUserIdAndStatus(String userId, TaskStatus status, Pageable pageable);

    long countByUserIdAndStatus(String userId, TaskStatus status);

    List<Task> findByStatusAndCompletedAtBefore(TaskStatus status, LocalDateTime cutoff);
}
