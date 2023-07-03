package io.github.aylesw.mch.backend.service.impl;

import com.google.api.services.calendar.model.Events;
import io.github.aylesw.mch.backend.exception.ApiException;
import io.github.aylesw.mch.backend.model.Injection;
import io.github.aylesw.mch.backend.service.GoogleCalendarService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoogleCalendarServiceImpl implements GoogleCalendarService {

    /**
     * Application name.
     */
    private final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    /**
     * Global instance of the JSON factory.
     */
    private final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    /**
     * Directory to store authorization tokens for this application.
     */
    private final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_EVENTS);
    private final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.
        InputStream in = GoogleCalendarServiceImpl.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        //returns an authorized Credential object.
        return credential;
    }

    public void sendEventInvitation(String recipientEmail, String eventTitle, String eventDescription, LocalDateTime eventDateTime) {
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            // Create the event
            Event event = new Event();
            event.setSummary(eventTitle);
            event.setDescription(eventDescription);

            LocalDateTime endDateTime = eventDateTime.plusHours(8); // Adjust end time as desired

            EventDateTime start = new EventDateTime();
            start.setDateTime(convertToDateTime(eventDateTime));
            event.setStart(start);

            EventDateTime end = new EventDateTime();
            end.setDateTime(convertToDateTime(endDateTime));
            event.setEnd(end);

            // Add the participant to the event
            EventAttendee attendee = new EventAttendee();
            attendee.setEmail(recipientEmail);
            List<EventAttendee> attendees = new ArrayList<>();
            attendees.add(attendee);
            event.setAttendees(attendees);

            // Send the event invitation
            service.events().insert("primary", event)
                    .setSendNotifications(true)
                    .execute();

            System.out.println("Event invitation sent to " + recipientEmail);
        } catch (Exception e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public void createEventOfInjection(Injection injection) {
        LocalDateTime eventDateTime = injection.getDate().toLocalDate().atTime(LocalTime.of(9, 0));

        sendEventInvitation(
                injection.getChild().getParent().getEmail(),
                "Tiêm vaccine %s mũi số %d cho bé %s".formatted(
                        injection.getVaccine().getName(),
                        injection.getVaccine().getDoseNo(),
                        injection.getChild().getFullName()),
                "",
                eventDateTime);
    }

    @Override
    public void deleteEventOfInjection(Injection injection) {
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            Events events = service.events().list("primary")
                    .setQ("vaccine %s mũi số %d cho bé %s"
                            .formatted(injection.getVaccine().getName(),
                                    injection.getVaccine().getDoseNo(),
                                    injection.getChild().getFullName()))
                    .execute();
            if (events.getItems().isEmpty()) return;
            String eventId = events.getItems().get(0).getId();
            service.events().delete("primary", eventId).setSendNotifications(true).execute();
        } catch (Exception e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public void updateEventOfInjection(Injection injection) {
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            Events events = service.events().list("primary")
                    .setQ("vaccine %s mũi số %d cho bé %s"
                            .formatted(injection.getVaccine().getName(),
                                    injection.getVaccine().getDoseNo(),
                                    injection.getChild().getFullName()))
                    .execute();
            if (events.getItems().isEmpty()) return;
            Event event = events.getItems().get(0);
            String eventId = event.getId();

            LocalDateTime startDateTime = injection.getDate().toLocalDate().atTime(9, 0);
            LocalDateTime endDateTime = startDateTime.plusHours(8);

            event.setSummary("Tiêm vaccine %s mũi số %d cho bé %s".formatted(
                    injection.getVaccine().getName(),
                    injection.getVaccine().getDoseNo(),
                    injection.getChild().getFullName()));
            event.setStart(new EventDateTime().setDateTime(convertToDateTime(startDateTime)));
            event.setEnd(new EventDateTime().setDateTime(convertToDateTime(endDateTime)));

            service.events().update("primary", eventId, event).setSendNotifications(true).execute();
        } catch (Exception e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private DateTime convertToDateTime(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneOffset.systemDefault();
        long epochMillis = localDateTime.atZone(zoneId).toInstant().toEpochMilli();
        return new DateTime(epochMillis);
    }
}