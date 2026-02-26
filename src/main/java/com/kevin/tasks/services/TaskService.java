package com.kevin.tasks.services;

import com.kevin.tasks.domain.entities.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskService {
    List<Task> findTasks(UUID taskListId);
    Task createTask(UUID taskListId, Task task);

    Optional<Task> getTask(UUID taskListId, UUID taskId);
    Task updateTask(UUID taskLIstId, UUID taskID, Task task);
    void deleteTask(UUID taskListId, UUID taskId);
}
