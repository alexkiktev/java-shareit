package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestItemInfoDto;

import java.util.List;

public interface RequestService {

    RequestDto createRequest(Long userId, RequestDto requestDto);

    List<RequestItemInfoDto> getRequestByUser(Long userId);

    List<RequestItemInfoDto> getAllRequests(Long userId, Integer from, Integer size);

    RequestItemInfoDto getRequestById(Long userId, Long requestId);

}