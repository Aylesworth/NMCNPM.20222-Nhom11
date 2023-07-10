package io.github.aylesw.mch.backend.controller;

import io.github.aylesw.mch.backend.dto.OverallStatistics;
import io.github.aylesw.mch.backend.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @GetMapping("/overall")
    public ResponseEntity<OverallStatistics> getOverallStatistics() {
        return ResponseEntity.ok(statisticsService.getOverallStatistics());
    }
}
