package io.github.aylesw.mch.backend.service.impl;

import io.github.aylesw.mch.backend.common.Utils;
import io.github.aylesw.mch.backend.dto.EventDto;
import io.github.aylesw.mch.backend.dto.NotificationDetails;
import io.github.aylesw.mch.backend.dto.UserDto;
import io.github.aylesw.mch.backend.exception.ApiException;
import io.github.aylesw.mch.backend.exception.ResourceNotFoundException;
import io.github.aylesw.mch.backend.model.Event;
import io.github.aylesw.mch.backend.model.User;
import io.github.aylesw.mch.backend.repository.EventRepository;
import io.github.aylesw.mch.backend.repository.UserRepository;
import io.github.aylesw.mch.backend.service.EventService;
import io.github.aylesw.mch.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final NotificationService notificationService;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Override
    public List<EventDto> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(event -> mapper.map(event, EventDto.class))
                .toList();
    }

    @Override
    public List<EventDto> getCurrentEvents() {
        return eventRepository.findEventsAvailableOn(Date.valueOf(LocalDate.now())).stream()
                .map(event -> mapper.map(event, EventDto.class))
                .toList();
    }

    @Override
    public void addEvent(EventDto eventDto) {
        Event event = mapper.map(eventDto, Event.class);
        event.setId(null);
        eventRepository.save(event);
    }

    @Override
    public void updateEvent(Long id, EventDto eventDto) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", id));

        List<User> participants = event.getParticipants();
        event = mapper.map(eventDto, Event.class);
        event.setId(id);
        event.setParticipants(participants);
        eventRepository.save(event);
    }

    @Override
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", id));

        eventRepository.delete(event);

        event.getParticipants().forEach(user -> {
            NotificationDetails notificationDetails = NotificationDetails.builder()
                    .user(user)
                    .time(Utils.currentTimestamp())
                    .title("Sự kiện bị hủy")
                    .message("Sự kiện %s mà bạn tham gia đã bị hủy.".formatted(event.getName()))
                    .build();

            notificationService.createSystemNotification(notificationDetails);
        });
    }

    @Override
    public void register(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (event.getParticipants().contains(user))
            throw new ApiException(HttpStatus.BAD_REQUEST, "User already registered for event");

        event.getParticipants().add(user);
        eventRepository.save(event);
    }

    @Override
    public void unregister(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (!event.getParticipants().contains(user))
            throw new ApiException(HttpStatus.BAD_REQUEST, "User haven't registered for event");

        event.getParticipants().remove(user);
        eventRepository.save(event);
    }

    @Override
    public List<UserDto> getParticipants(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        return event.getParticipants().stream()
                .map(user -> mapper.map(user, UserDto.class))
                .toList();
    }

    @Override
    public void sendNotification(Long eventId, String notification) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        event.getParticipants().forEach(user -> {
            NotificationDetails notificationDetails = NotificationDetails.builder()
                    .user(user)
                    .time(Utils.currentTimestamp())
                    .title("Thông báo mới về sự kiện")
                    .message("Sự kiện %s mà bạn tham gia có thông báo mới với nội dung: %s"
                            .formatted(event.getName(), notification))
                    .build();

            notificationService.createSystemNotification(notificationDetails);
        });
    }

    @Override
    public void removeParticipant(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        event.getParticipants().removeIf(user -> user.getId().equals(userId));
        eventRepository.save(event);
    }

    @Override
    public List<EventDto> getEventsOfUser(Long userId) {
        return eventRepository.findByUserId(userId).stream()
                .map(event -> mapper.map(event, EventDto.class))
                .toList();
    }
}
