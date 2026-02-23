package com.kevin.tasks.services.impl;

import com.kevin.tasks.domain.entities.Task;
import com.kevin.tasks.domain.entities.TaskList;
import com.kevin.tasks.domain.entities.TaskPriority;
import com.kevin.tasks.domain.entities.TaskStatus;
import com.kevin.tasks.repositories.TaskListRepository;
import com.kevin.tasks.repositories.TaskRepository;
import com.kevin.tasks.services.TaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;

    public TaskServiceImpl(TaskRepository taskRepository, TaskListRepository taskListRepository) {
        this.taskRepository = taskRepository;
        this.taskListRepository= taskListRepository;
    }

    @Override
    public List<Task> findTasks(UUID taskListId) {
        return taskRepository.findByTaskListId(taskListId);
    }

    @Override
    public Task createTask(UUID taskListId, Task task) {
        if(null != task.getId()) {
            throw new IllegalArgumentException("Task already exists");
        }

        if(null == task.getTitle() || task.getTitle().isBlank()) {
            throw new IllegalArgumentException("Task title is required");
        }
        TaskStatus taskStatus = TaskStatus.OPEN;
        TaskPriority taskPriority = Optional.ofNullable(task.getPriority()).orElse(TaskPriority.MEDIUM);

        TaskList taskList = taskListRepository.getReferenceById(taskListId);
        LocalDateTime now =  LocalDateTime.now();


        Task newTask = new Task(
                null,
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                taskStatus,
                taskPriority,
                taskList,
                now,
                now
        );

        return taskRepository.save(newTask);
    }
}
