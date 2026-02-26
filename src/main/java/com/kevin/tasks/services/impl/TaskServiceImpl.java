package com.kevin.tasks.services.impl;

import com.kevin.tasks.domain.entities.Task;
import com.kevin.tasks.domain.entities.TaskList;
import com.kevin.tasks.domain.entities.TaskPriority;
import com.kevin.tasks.domain.entities.TaskStatus;
import com.kevin.tasks.repositories.TaskListRepository;
import com.kevin.tasks.repositories.TaskRepository;
import com.kevin.tasks.services.TaskService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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

    @Transactional
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

    @Override
    public Optional<Task> getTask(UUID taskListId, UUID taskId) {
        return taskRepository.findByTaskListIdAndId(taskListId, taskId);
    }

    @Transactional
    @Override
    public Task updateTask(UUID taskListId, UUID taskID, Task task) {
        if(null == task.getId()) {
            throw new IllegalArgumentException("Task must have an ID");
        }
        if(!Objects.equals(task.getId(), taskID)) {
            throw new IllegalArgumentException("Task IDs must match");
        }
        if(null == task.getTitle() || task.getTitle().isBlank()) {
            throw new IllegalArgumentException("Task must have a title");
        }
        TaskPriority taskPriority= Optional.ofNullable(task.getPriority()).orElse(TaskPriority.MEDIUM);
        TaskStatus taskStatus = Optional.ofNullable(task.getStatus()).orElse(TaskStatus.OPEN);
        Task existingTask = taskRepository.findByTaskListIdAndId(taskListId, taskID)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setDueDate(task.getDueDate());
        existingTask.setPriority(taskPriority);
        existingTask.setStatus(taskStatus);
        existingTask.setUpdated(LocalDateTime.now());

        return taskRepository.save(existingTask);
    }

    @Transactional
    @Override
    public void deleteTask(UUID taskListId, UUID taskId) {
        taskRepository.deleteByTaskListIdAndId(taskListId, taskId);
    }


}
