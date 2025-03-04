package io.github.aylesw.mch.backend.service;

import io.github.aylesw.mch.backend.entity.Injection;

import java.time.LocalDateTime;

public interface GoogleCalendarService {
    void sendEventInvitation(String recipientEmail, String eventTitle, String eventDescription, LocalDateTime eventDateTime);

    void createEventOfInjection(Injection injection);

    void deleteEventOfInjection(Injection injection);

    void updateEventOfInjection(Injection injection);
}
