package io.github.aylesw.mch.backend.controller;

import io.github.aylesw.mch.backend.dto.EventDto;
import io.github.aylesw.mch.backend.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/event")
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
        return ResponseEntity.ok("User registered to event successfully");
    }
}
