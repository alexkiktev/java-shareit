package ru.practicum.shareit.item.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingItemInfoDto;
import ru.practicum.shareit.booking.exception.NotFoundException;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.exception.CommentValidationException;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ItemMapper itemMapper;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserService userService, ItemMapper itemMapper,
                           BookingRepository bookingRepository, BookingMapper bookingMapper,
                           CommentMapper commentMapper, UserRepository userRepository,
                           CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.itemMapper = itemMapper;
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.commentMapper = commentMapper;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        userService.getUser(userId);
        return itemMapper.toItemDto(itemRepository.save(itemMapper.toItemNew(itemDto, userId)));
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        userService.getUser(userId);
        itemDto.setId(itemId);
        Item updatedItem = itemRepository.findById(itemId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Вещь id %s не найдена!", itemId)));
        if (userId.equals(updatedItem.getOwner())) {
            Optional.ofNullable(itemDto.getName()).ifPresent(updatedItem::setName);
            Optional.ofNullable(itemDto.getDescription()).ifPresent(updatedItem::setDescription);
            Optional.ofNullable(itemDto.getAvailable()).ifPresent(updatedItem::setAvailable);
            return itemMapper.toItemDto(itemRepository.save(updatedItem));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Вещь id %s не принадлежит пользователю %s", itemId, userId));
        }
    }

    @Override
    public ItemOwnerDto getItem(Long itemId, Long userId) {
        ItemOwnerDto itemOwnerDto = itemMapper.toItemOwnerDto(itemRepository.findById(itemId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Вещь id %s не найдена!", itemId))));
        List<CommentDto> commentsDto = commentRepository.findCommentByItemId(itemId).stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
        itemOwnerDto.setComments(commentsDto);
        if (itemOwnerDto.getOwnerId().equals(userId)) {
            getLastAndNextBooking(itemOwnerDto);
        }
        return itemOwnerDto;
    }

    @Override
    public List<ItemOwnerDto> getItemsOwners(Long userId) {
        userService.getUser(userId);
        List<Item> itemsOwners = itemRepository.findItemsByOwner(userId);
        if (itemsOwners.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("У пользователя %s нет зарегистрированных вещей", userId));
        }
        List<ItemOwnerDto> itemsDtoForOwner = new ArrayList<>();
        for (Item item : itemsOwners) {
            ItemOwnerDto itemOwnerDto = itemMapper.toItemOwnerDto(item);
            getLastAndNextBooking(itemOwnerDto);
            List<CommentDto> commentsDto = commentRepository.findCommentByItemId(itemOwnerDto.getId())
                    .stream()
                    .map(commentMapper::toDto)
                    .collect(Collectors.toList());
            itemOwnerDto.setComments(commentsDto);
            itemsDtoForOwner.add(itemOwnerDto);
        }
        return itemsDtoForOwner.stream()
                .sorted(Comparator.comparing(ItemOwnerDto :: getId))
                .collect(Collectors.toList());
    }

    private void getLastAndNextBooking(ItemOwnerDto itemOwnerDto) {
        List<Booking> bookings = bookingRepository.findBookingsByItemId(itemOwnerDto.getId());
        BookingItemInfoDto lastBooking = bookings.stream()
                .sorted(Comparator.comparing(Booking::getEnd).reversed())
                .filter(b -> LocalDateTime.now().isAfter(b.getStart()))
                .findFirst()
                .map(bookingMapper :: toBookingItemInfoDto)
                .orElse(null);
        itemOwnerDto.setLastBooking(lastBooking);
        BookingItemInfoDto nextBooking = bookings.stream()
                .sorted(Comparator.comparing(Booking::getStart))
                .filter(b -> LocalDateTime.now().isBefore(b.getStart()))
                .findFirst()
                .map(bookingMapper :: toBookingItemInfoDto)
                .orElse(null);
        itemOwnerDto.setNextBooking(nextBooking);
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (StringUtils.isBlank(text)) {
            return new ArrayList<>();
        }
        List<Item> itemsFound = itemRepository.findAll().stream()
                .filter(i -> StringUtils.containsIgnoreCase(i.getName(), text) ||
                        StringUtils.containsIgnoreCase(i.getDescription(), text) && i.getAvailable().equals(true))
                .collect(Collectors.toList());
        return itemsFound.stream().map(itemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto createComment(CommentDto commentDto, Long itemId, Long userId) {
        if (bookingRepository.findBookingsByItemIdAndBookerIdAndComplete(itemId, userId) > 0) {
            Comment comment = commentMapper.toCommentDto(commentDto);
            comment.setItem(itemRepository.findById(itemId).orElseThrow(() ->
                    new NotFoundException("Вещь не найдена")));
            comment.setAuthor(userRepository.findById(userId).orElseThrow(() ->
                    new NotFoundException("Пользователь не найден")));
            comment.setCreated(LocalDateTime.now());
            return commentMapper.toDto(commentRepository.save(comment));
        } else {
            throw new CommentValidationException(String.format("У пользователя id %s нет завершенной аренды этой вещи",
                    itemId));
        }
    }

}
