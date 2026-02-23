package com.kevin.tasks.services;

import com.kevin.tasks.domain.entities.Task;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    List<Task> findTasks(UUID taskListId);
    Task createTask(UUID taskListId, Task task);
}
