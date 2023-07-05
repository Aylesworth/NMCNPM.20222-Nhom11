package io.github.aylesw.mch.backend.controller;

import io.github.aylesw.mch.backend.dto.BodyMetricsDto;
import io.github.aylesw.mch.backend.service.BodyMetricsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BodyMetricsController {
    private final BodyMetricsService bodyMetricsService;

    @GetMapping("/children/{child-id}/body-metrics")
    public ResponseEntity<List<BodyMetricsDto>> getBodyMetrics(@PathVariable("child-id") Long childId) {
        return ResponseEntity.ok(bodyMetricsService.getBodyMetrics(childId));
    }

    @PostMapping("/children/{child-id}/body-metrics")
    public ResponseEntity<String> addBodyMetrics(@PathVariable("child-id") Long childId,
                                                 @Valid @RequestBody BodyMetricsDto bodyMetricsDto) {
        bodyMetricsService.addBodyMetrics(childId, bodyMetricsDto);
        return ResponseEntity.ok("New body metrics added successfully");
    }

    @DeleteMapping("/children/{child-id}/body-metrics/{id}")
    public ResponseEntity<String> deleteBodyMetrics(@PathVariable("child-id") Long childId,
                                                    @PathVariable("id") Long bodyMetricsId) {
        bodyMetricsService.deleteBodyMetrics(childId, bodyMetricsId);
        return ResponseEntity.ok("Body metrics deleted successfully");
    }

    @PostMapping("/children/{child-id}/body-metrics/request-update")
    public ResponseEntity<String> requestUpdate(@PathVariable("child-id") Long childId) {
        bodyMetricsService.requestUpdate(childId);
        return ResponseEntity.ok("Requested body metrics update");
    }
}
