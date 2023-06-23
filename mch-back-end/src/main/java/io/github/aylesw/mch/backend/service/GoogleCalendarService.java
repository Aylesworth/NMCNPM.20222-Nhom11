package io.github.aylesw.mch.backend.service;

import java.time.LocalDateTime;

public interface GoogleCalendarService {
    void sendEventInvitation(String recipientEmail, String eventTitle, String eventDescription, LocalDateTime eventDateTime);
}
