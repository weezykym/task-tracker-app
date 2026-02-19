package com.kevin.tasks.mappers;

import com.kevin.tasks.domain.dto.TaskDTO;
import com.kevin.tasks.domain.entities.Task;

public interface TaskMapper {

    Task fromDto(TaskDTO taskDTO);

    TaskDTO toDto(Task task);
}
