package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestItemInfoDto;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class RequestController {

    private final RequestService requestService;
    public static final String USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public RequestDto createRequest(@RequestHeader(USER_ID) Long userId,
                                    @RequestBody @Valid RequestDto requestDto) {
        return requestService.createRequest(userId, requestDto);
    }

    @GetMapping
    public List<RequestItemInfoDto> getRequestsByUser(@RequestHeader(USER_ID) Long userId) {
        return requestService.getRequestByUser(userId);
    }

    @GetMapping("/all")
    public List<RequestItemInfoDto> getAllRequests(@RequestHeader(USER_ID) Long userId,
                                                   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                                   Integer from,
                                                   @Positive @RequestParam(name = "size", defaultValue = "20")
                                                       Integer size) {
        return requestService.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public RequestItemInfoDto getRequestById(@RequestHeader(USER_ID) Long userId,
                                             @PositiveOrZero @PathVariable Long requestId) {
        return requestService.getRequestById(userId, requestId);
    }

}
