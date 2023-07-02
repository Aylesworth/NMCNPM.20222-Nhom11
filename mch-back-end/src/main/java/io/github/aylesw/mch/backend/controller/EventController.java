package io.github.aylesw.mch.backend.controller;

import io.github.aylesw.mch.backend.dto.EventDto;
import io.github.aylesw.mch.backend.dto.UserDto;
import io.github.aylesw.mch.backend.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping("")
    public ResponseEntity<List<EventDto>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/current")
    public ResponseEntity<List<EventDto>> getCurrentEvents() {
        return ResponseEntity.ok(eventService.getCurrentEvents());
    }

    @PostMapping("")
    public ResponseEntity<String> addEvent(@Valid @RequestBody EventDto eventDto) {
        eventService.addEvent(eventDto);
        return ResponseEntity.ok("New event added successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateEvent(@PathVariable Long id,
                                              @Valid @RequestBody EventDto eventDto) {
        eventService.updateEvent(id, eventDto);
        return ResponseEntity.ok("Event updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok("Event deleted successfully");
    }

    @PostMapping("/{id}/register")
    public ResponseEntity<String> register(@PathVariable("id") Long eventId,
                                           @RequestParam(value = "user-id", required = true) Long userId) {
        eventService.register(eventId, userId);
        return ResponseEntity.ok("Registered user for event successfully");
    }

    @PostMapping("/{id}/unregister")
    public ResponseEntity<String> unregister(@PathVariable("id") Long eventId,
                                           @RequestParam(value = "user-id", required = true) Long userId) {
        eventService.unregister(eventId, userId);
        return ResponseEntity.ok("Unregistered user from event successfully");
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<UserDto>> getParticipants(@PathVariable("id") Long eventId) {
        return ResponseEntity.ok(eventService.getParticipants(eventId));
    }

    @PostMapping("/{id}/send-notification")
    public ResponseEntity<String> sendNotification(@PathVariable("id") Long eventId,
                                                   @RequestParam(value = "content", required = true) String notification) {
        eventService.sendNotification(eventId, notification);
        return ResponseEntity.ok("Notification sent successfully");
    }

    @DeleteMapping("/{id}/participants/{user-id}")
    public ResponseEntity<String> removeParticipant(@PathVariable("id") Long eventId,
                                                    @PathVariable("user-id") Long userId) {
        eventService.removeParticipant(eventId, userId);
        return ResponseEntity.ok("Participant removed successfully");
    }

    @GetMapping("/find-by-user")
    public ResponseEntity<List<EventDto>> getEventsOfUser(@RequestParam("id") Long userId) {
        return ResponseEntity.ok(eventService.getEventsOfUser(userId));
    }
}
