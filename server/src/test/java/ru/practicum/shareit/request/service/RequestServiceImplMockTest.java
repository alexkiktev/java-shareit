package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestItemInfoDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplMockTest {

    @Mock
    private RequestRepository requestRepository;
    @Mock
    private RequestMapper requestMapper;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private RequestServiceImpl requestServiceImpl;

    private Long userId;
    private Long requestId;
    private Integer from;
    private Integer size;
    private LocalDateTime created;
    private User user;
    private ItemDto itemDto;
    private List<ItemDto> listItemDto;
    private Request request;
    private RequestDto newRequestDto;
    private RequestDto savedRequestDto;
    private RequestItemInfoDto requestItemInfoDto;
    private List<Request> emptyListRequest;

    @BeforeEach
    void setUp() {
        userId = 1L;
        requestId = 1L;
        from = 0;
        size = 20;
        created = LocalDateTime.now().plusHours(1);
        user = new User(1L, "alex@mail.ru", "Alex");
        itemDto = new ItemDto(null, "Отвертка", "Крестовая отвертка", true, null);
        listItemDto = List.of(itemDto);
        request = new Request(requestId, "Описание", userId, created);
        newRequestDto = new RequestDto(null, "Описание", null, created);
        savedRequestDto = new RequestDto(null, "Описание", userId, created);
        requestItemInfoDto = new RequestItemInfoDto(requestId, "Описание", created, listItemDto);
        emptyListRequest = new ArrayList<>();
    }

    @Test
    void givenRequestDto_whenSaveUser_createRequestTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(requestMapper.toRequest(savedRequestDto)).thenReturn(request);
        when(requestRepository.save(request)).thenReturn(request);
        when(requestMapper.toRequestDto(request)).thenReturn(savedRequestDto);

        RequestDto controlRequestDto = requestServiceImpl.createRequest(userId, newRequestDto);

        Assertions.assertEquals(controlRequestDto, savedRequestDto);
        verify(requestRepository, times(1)).save(request);
    }

    @Test
    void throwException_whenUserNotFound_createRequestTest() {
        Long controlUserId = 999999999L;
        when(userRepository.findById(controlUserId)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class,
                () -> requestServiceImpl.createRequest(controlUserId, newRequestDto));
    }

    @Test
    void givenEmptyList_whenUserDoesNotHaveRequests_getRequestByUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(requestRepository.findByRequestorOrderByCreatedDesc(userId)).thenReturn(emptyListRequest);

        List<RequestItemInfoDto> controlRequestItemInfoDtoList = requestServiceImpl.getRequestByUser(userId);

        Assertions.assertTrue(controlRequestItemInfoDtoList.isEmpty());
    }

    @Test
    void throwException_whenUserNotFound_getRequestByUser() {
        Long controlUserId = 999999999L;
        when(userRepository.findById(controlUserId)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class,
                () -> requestServiceImpl.getRequestByUser(controlUserId));
    }

    @Test
    void throwException_whenUserNotFound_getAllRequests() {
        Long controlUserId = 999999999L;
        when(userRepository.findById(controlUserId)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class,
                () -> requestServiceImpl.getAllRequests(controlUserId, from, size));
    }

    @Test
    void throwException_whenUserNotFound_getRequestById() {
        Long controlUserId = 999999999L;
        when(userRepository.findById(controlUserId)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class,
                () -> requestServiceImpl.getRequestById(controlUserId, requestId));
    }

}