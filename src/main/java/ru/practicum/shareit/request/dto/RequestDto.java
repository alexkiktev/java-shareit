package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RequestDto {
    private Long id;
    @NotNull(message = "Описание не может быть пустым!")
    private String description;
    private Long requestor;
    private LocalDateTime created;
}