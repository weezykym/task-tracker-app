package com.kevin.tasks.mappers;

import com.kevin.tasks.domain.dto.TaskListDto;
import com.kevin.tasks.domain.entities.TaskList;

public interface TaskListMapper {

    TaskList fromDto(TaskListDto taskListDto);

    TaskListDto toDto(TaskList taskList);
}
