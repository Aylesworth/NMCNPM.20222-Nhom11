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
import mch.model.Examination;
import mch.service.ExaminationService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ExaminationController {
	private final ExaminationService examinationService;

	@GetMapping("/children/{childId}/examination")
	public ResponseEntity<List<Examination>> getExaminationsOfChild(@PathVariable Long childId) {
		return ResponseEntity.ok(examinationService.getExaminationsOfChild(childId));
	}

	@PostMapping("/children/{childId}/examination")
	public ResponseEntity<SimpleResponse> addExamination(@PathVariable Long childId,
			@RequestBody Examination examination) {

		examinationService.addExamination(childId, examination);
		return ResponseEntity.ok(new SimpleResponse("New medical examination recorded"));
	}

	@PutMapping("/children/{childId}/examination/{id}")
	public ResponseEntity<SimpleResponse> updateExamination(@PathVariable Long childId, @PathVariable Long id,
			@RequestBody Examination examination) {

		examinationService.updateExamination(childId, id, examination);
		return ResponseEntity.ok(new SimpleResponse("Medical examination record updated"));
	}

	@DeleteMapping("/children/{childId}/examination/{id}")
	public ResponseEntity<SimpleResponse> deleteExamination(@PathVariable("childId") Long childId,
			@PathVariable("id") Long id) {

		examinationService.deleteExamination(childId, id);
		return ResponseEntity.ok(new SimpleResponse("Medical examination record deleted"));
	}
}
