package com.aylesw.mch.backend.service.impl;

import com.aylesw.mch.backend.dto.EventDto;
import com.aylesw.mch.backend.exception.ResourceNotFoundException;
import com.aylesw.mch.backend.model.Event;
import com.aylesw.mch.backend.model.User;
import com.aylesw.mch.backend.repository.EventRepository;
import com.aylesw.mch.backend.service.EventService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
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
    }
}
