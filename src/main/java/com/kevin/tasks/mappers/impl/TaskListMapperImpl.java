package com.kevin.tasks.mappers.impl;

import com.kevin.tasks.domain.dto.TaskListDto;
import com.kevin.tasks.domain.entities.Task;
import com.kevin.tasks.domain.entities.TaskList;
import com.kevin.tasks.domain.entities.TaskStatus;
import com.kevin.tasks.mappers.TaskListMapper;
import com.kevin.tasks.mappers.TaskMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TaskListMapperImpl implements TaskListMapper {

    private final TaskMapper taskMapper;

    public TaskListMapperImpl(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }
    @Override
    public TaskList fromDto(TaskListDto taskListDto) {
        return new TaskList(
                taskListDto.id(),
                taskListDto.title(),
                taskListDto.description(),
                Optional.ofNullable(taskListDto.tasks()).map(tasks -> tasks.stream()
                        .map(taskMapper::fromDto)
                        .toList())
                        .orElse(null),
                null,
                null
        );
    }

    @Override
    public TaskListDto toDto(TaskList taskList) {
        return new TaskListDto(
                taskList.getId(),
                taskList.getTitle(),
                taskList.getDescription(),
                countTasks(taskList.getTasks()),
                taskProgressCounter(taskList.getTasks()),
                Optional.ofNullable(taskList.getTasks()
                                .stream()
                                .map(taskMapper::toDto)
                                .toList())
                        .orElse(null)
        );
    }

    private Integer countTasks(List<Task> tasks) {
        return ( tasks != null) ? tasks.size() : 0;
    }

    private Double taskProgressCounter(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            return null;
        }

        long closedTasks = (tasks.stream().filter(task -> task.getStatus() == TaskStatus.CLOSED).count());
        return (double) closedTasks / tasks.size();
    }
}
