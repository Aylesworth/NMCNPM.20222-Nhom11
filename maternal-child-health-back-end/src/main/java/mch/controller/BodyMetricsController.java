package mch.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import mch.dto.SimpleResponse;
import mch.model.BodyMetrics;
import mch.service.BodyMetricsService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BodyMetricsController {
	private final BodyMetricsService bodyMetricsService;
	
	@GetMapping("/children/{childId}/body-metrics")
	public ResponseEntity<List<BodyMetrics>> getBodyMetricsOfChild(@PathVariable Long childId) {
		return ResponseEntity.ok(bodyMetricsService.getBodyMetricsOfChild(childId));
	}
	
	@PostMapping("/children/{childId}/body-metrics")
	public ResponseEntity<SimpleResponse> addBodyMetrics(
			@PathVariable Long childId,
			@RequestBody BodyMetrics metrics) {
		
		bodyMetricsService.addBodyMetrics(childId, metrics);
		return ResponseEntity.ok(new SimpleResponse("New body metrics recorded"));
	}
	
	@PutMapping("/children/{childId}/body-metrics/{id}")
	public ResponseEntity<SimpleResponse> updateBodyMetrics(
			@PathVariable Long childId,
			@PathVariable Long id,
			@RequestBody BodyMetrics metrics) {
		
		bodyMetricsService.updateBodyMetrics(childId, id, metrics);
		return ResponseEntity.ok(new SimpleResponse("Body metrics record updated"));
	}
	
	@DeleteMapping("/children/{childId}/body-metrics/{id}")
	public ResponseEntity<SimpleResponse> deleteBodyMetrics(
			@PathVariable("childId") Long childId,
			@PathVariable("id") Long id) {
		
		bodyMetricsService.deleteBodyMetrics(childId, id);
		return ResponseEntity.ok(new SimpleResponse("Body metrics record deleted"));
	}
}
