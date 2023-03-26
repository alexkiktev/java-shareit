package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestItemInfoDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public RequestDto createRequest(Long userId, RequestDto requestDto) {
        existUserById(userId);
        requestDto.setRequestor(userId);
        return requestMapper.toRequestDto(requestRepository.save(requestMapper.toRequest(requestDto)));
    }

    public List<RequestItemInfoDto> getRequestByUser(Long userId) {
        existUserById(userId);
        List<Request> requestsByUser = requestRepository.findByRequestorOrderByCreatedDesc(userId);
        List<RequestItemInfoDto> requestItemInfoDtoList = new ArrayList<>();
        for (Request request : requestsByUser) {
            RequestItemInfoDto requestItemInfoDto = requestMapper.toRequestItemInfo(request);
            requestItemInfoDto.setItems(getResponseItems(request.getId()));
            requestItemInfoDtoList.add(requestItemInfoDto);
        }
        return requestItemInfoDtoList;
    }

    public List<RequestItemInfoDto> getAllRequests(Long userId, Integer from, Integer size) {
        existUserById(userId);
        Pageable pageParams = PageRequest.of(from / size, size, Sort.by("created").descending());
        Page<Request> allPages = requestRepository.findAllByRequestorNot(userId, pageParams);
        List<Request> allRequests = allPages.getContent();
        List<RequestItemInfoDto> requestItemInfoDtoList = new ArrayList<>();
        for (Request request : allRequests) {
            RequestItemInfoDto requestItemInfoDto = requestMapper.toRequestItemInfo(request);
            requestItemInfoDto.setItems(getResponseItems(request.getId()));
            requestItemInfoDtoList.add(requestItemInfoDto);
        }
        return requestItemInfoDtoList;
    }

    public RequestItemInfoDto getRequestById(Long userId, Long requestId) {
        existUserById(userId);
        Request request = requestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException(String.format("Запрос id %s не найден", requestId)));
        RequestItemInfoDto requestItemInfoDto = requestMapper.toRequestItemInfo(request);
        requestItemInfoDto.setItems(getResponseItems(request.getId()));
        return requestItemInfoDto;
    }

    private List<ItemDto> getResponseItems(Long requestId) {
        return itemRepository.findItemsByRequestId(requestId).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private User existUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь id %s не найден!", userId)));
    }

}