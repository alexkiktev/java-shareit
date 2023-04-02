package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingOutputDto createBooking(Long bookerId, BookingInputDto bookingInputDto) {
        User booker = existUserById(bookerId);
        Item item = getItem(bookingInputDto.getItemId());
        if (bookerId.equals(item.getOwner())) {
            throw new BookingOwnerException("Владелец не может бронировать свои же вещи!");
        }
        if (bookingInputDto.getEnd().isBefore(bookingInputDto.getStart())) {
            throw new IncorrectDateTimeException("Дата и время конца бронирования не могут быть раньше начала");
        }
        if (bookingInputDto.getStart().equals(bookingInputDto.getEnd())) {
            throw new IncorrectDateTimeException("Дата и время начала и конца бронирования не могут совпадать");
        }
        if (item.getAvailable().equals(true)) {
            Booking booking = bookingMapper.toBooking(bookingInputDto);
            booking.setBooker(booker);
            booking.setItem(item);
            return bookingMapper.toBookingOutputDto(bookingRepository.save(booking));
        }
        throw new BookingValidatedException(String.format("Бронирование вещи id %s недоступно",
                bookingInputDto.getItemId()));
    }

    @Override
    public BookingOutputDto approvedBooking(Long userId, Long bookingId, boolean approved) {
        existUserById(userId);
        Booking booking = getBooking(bookingId);
        if (!Objects.equals(userId, booking.getItem().getOwner())) {
            throw new BookingOwnerException("Только владелец может подтвердить бронь");
        }
        if (booking.getStatus() == Status.APPROVED) {
            throw new BookingValidatedException("Бронь уже в статусе Подтверждено");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return bookingMapper.toBookingOutputDto(bookingRepository.save(booking));
    }

    @Override
    public BookingOutputDto getBookingById(Long userId, Long bookingId) {
        existUserById(userId);
        Booking booking = getBooking(bookingId);
        if (!Objects.equals(userId, booking.getItem().getOwner()) &&
                !Objects.equals(userId, booking.getBooker().getId())) {
            throw new NotFoundException("Просмотр брони возможен только владельцем или автором бронирования");
        }
        return bookingMapper.toBookingOutputDto(booking);
    }

    @Override
    public List<BookingOutputDto> getBookingByUser(Long userId, State state, Integer from, Integer size) {
        existUserById(userId);
        Pageable pageParams = PageRequest.of(from / size, size);
        switch (state) {
            case ALL:
                return bookingRepository.findBookingsByBookerIdOrderByStartDesc(userId, pageParams)
                        .stream()
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .map(bookingMapper::toBookingOutputDto)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findBookingsByBookerIdOrderByStartDesc(userId, pageParams)
                        .stream()
                        .sorted(Comparator.comparing(Booking::getStart))
                        .filter(b -> b.getStart().isBefore(LocalDateTime.now()) &&
                                b.getEnd().isAfter(LocalDateTime.now()))
                        .map(bookingMapper::toBookingOutputDto)
                        .collect(Collectors.toList());
            case PAST:
                return bookingRepository.findBookingsByBookerIdOrderByStartDesc(userId, pageParams)
                        .stream()
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .filter(b -> b.getEnd().isBefore(LocalDateTime.now()))
                        .map(bookingMapper::toBookingOutputDto)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findBookingsByBookerIdOrderByStartDesc(userId, pageParams)
                        .stream()
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                        .map(bookingMapper::toBookingOutputDto)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findBookingsByBookerIdOrderByStartDesc(userId, pageParams)
                        .stream()
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .filter(b -> b.getStatus() == Status.WAITING)
                        .map(bookingMapper::toBookingOutputDto)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findBookingsByBookerIdOrderByStartDesc(userId, pageParams)
                        .stream()
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .filter(b -> b.getStatus() == Status.REJECTED)
                        .map(bookingMapper::toBookingOutputDto)
                        .collect(Collectors.toList());
            default:
                throw new NotFoundStateException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    public List<BookingOutputDto> getBookingByOwner(Long ownerId, State state, Integer from, Integer size) {
        existUserById(ownerId);
        Pageable pageParams = PageRequest.of(from / size, size);
        switch (state) {
            case ALL:
                return bookingRepository.findBookingByItem_OwnerOrderByStartDesc(ownerId, pageParams)
                        .stream()
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .map(bookingMapper::toBookingOutputDto)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findBookingByItem_OwnerOrderByStartDesc(ownerId, pageParams)
                        .stream()
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .filter(b -> b.getStart().isBefore(LocalDateTime.now()) &&
                                b.getEnd().isAfter(LocalDateTime.now()))
                        .map(bookingMapper::toBookingOutputDto)
                        .collect(Collectors.toList());
            case PAST:
                return bookingRepository.findBookingByItem_OwnerOrderByStartDesc(ownerId, pageParams)
                        .stream()
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .filter(b -> b.getEnd().isBefore(LocalDateTime.now()))
                        .map(bookingMapper::toBookingOutputDto)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findBookingByItem_OwnerOrderByStartDesc(ownerId, pageParams)
                        .stream()
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                        .map(bookingMapper::toBookingOutputDto)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findBookingByItem_OwnerOrderByStartDesc(ownerId, pageParams)
                        .stream()
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .filter(b -> b.getStatus() == Status.WAITING)
                        .map(bookingMapper::toBookingOutputDto)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findBookingByItem_OwnerOrderByStartDesc(ownerId, pageParams)
                        .stream()
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .filter(b -> b.getStatus() == Status.REJECTED)
                        .map(bookingMapper::toBookingOutputDto)
                        .collect(Collectors.toList());
            default:
                throw new NotFoundStateException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    private User existUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь id %s не найден!", userId)));
    }

    private Item getItem(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException(String.format("Вещь id %s не найдена!", itemId)));
    }

    private Booking getBooking(Long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException(String.format("Бронирование id %s не найдено!", bookingId)));
    }

}
