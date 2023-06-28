package io.github.aylesw.mch.backend.controller;

import io.github.aylesw.mch.backend.model.SystemNotification;
import io.github.aylesw.mch.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/users/{id}/notifications")
    public ResponseEntity<List<SystemNotification>> getSystemNotifications(@PathVariable("id") Long userId) {
        return ResponseEntity.ok(notificationService.getSystemNotifications(userId));
    }
}
