package ru.practicum.shareit.request.mapper;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestItemInfoDto;
import ru.practicum.shareit.request.model.Request;

import java.time.LocalDateTime;

@Component
public class RequestMapper {

    public RequestDto toRequestDto(@NotNull Request request) {
        return new RequestDto(
                request.getId(),
                request.getDescription(),
                request.getRequestor(),
                request.getCreated()
        );
    }

    public Request toRequest(@NotNull RequestDto requestDto) {
        return new Request(
                requestDto.getId(),
                requestDto.getDescription(),
                requestDto.getRequestor(),
                LocalDateTime.now()
        );
    }

    public RequestItemInfoDto toRequestItemInfo(@NotNull Request request) {
        return RequestItemInfoDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .build();
    }

}
