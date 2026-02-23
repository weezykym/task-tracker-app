package com.kevin.tasks.controllers;

import com.kevin.tasks.domain.dto.TaskDTO;
import com.kevin.tasks.domain.entities.Task;
import com.kevin.tasks.mappers.TaskMapper;
import com.kevin.tasks.services.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/task-lists/{task_list_id}/tasks")
public class TasksController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    public TasksController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @GetMapping()
    public List<TaskDTO> listTasks(@PathVariable("task_list_id") UUID taskListId) {
        return taskService.findTasks(taskListId).stream().map(taskMapper::toDto).toList();
    }

    @PostMapping()
    public TaskDTO createTask(@PathVariable("task_list_id") UUID taskListId,
                              @RequestBody TaskDTO taskDTO) {
        Task newTask = taskService.createTask(taskListId, taskMapper.fromDto(taskDTO));
        return taskMapper.toDto(newTask);
    }
}
