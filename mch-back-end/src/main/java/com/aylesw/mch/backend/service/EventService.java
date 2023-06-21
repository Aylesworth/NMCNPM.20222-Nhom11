package com.aylesw.mch.backend.service;

import com.aylesw.mch.backend.dto.EventDto;

import java.util.List;

public interface EventService {
    List<EventDto> getAllEvents();

    List<EventDto> getCurrentEvents();

    void addEvent(EventDto eventDto);

    void updateEvent(Long id, EventDto eventDto);

    void deleteEvent(Long id);
}
