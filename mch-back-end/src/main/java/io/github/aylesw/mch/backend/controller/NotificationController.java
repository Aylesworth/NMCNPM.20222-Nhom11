package io.github.aylesw.mch.backend.controller;

import io.github.aylesw.mch.backend.model.SystemNotification;
import io.github.aylesw.mch.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/users/{id}/notifications")
    public ResponseEntity<List<SystemNotification>> getSystemNotifications(@PathVariable("id") Long userId) {
        return ResponseEntity.ok(notificationService.getSystemNotifications(userId));
    }

    @GetMapping("/users/{id}/notifications/count-new")
    public ResponseEntity<Map<String, Object>> countNewNotificationsOfUser(@PathVariable("id") Long userId) {
        var response = new HashMap<String, Object>();
        response.put("count", notificationService.countNewNotificationsOfUser(userId));
        return ResponseEntity.ok(response);
    }
}
