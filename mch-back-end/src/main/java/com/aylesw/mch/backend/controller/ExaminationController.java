package com.aylesw.mch.backend.controller;

import com.aylesw.mch.backend.dto.ExaminationDto;
import com.aylesw.mch.backend.service.ExaminationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ExaminationController {
    private final ExaminationService examinationService;

    @GetMapping("/child/{child-id}/examination")
    public ResponseEntity<List<ExaminationDto>> getExaminations(@PathVariable("child-id") Long childId) {
        return ResponseEntity.ok(examinationService.getExaminations(childId));
    }

    @PostMapping("/child/{child-id}/examination")
    public ResponseEntity<String> addExamination(@PathVariable("child-id") Long childId,
                                                 @Valid @RequestBody ExaminationDto examinationDto) {
        examinationService.addExamination(childId, examinationDto);
        return ResponseEntity.ok("Examination information added successfully");
    }

    @PutMapping("/child/{child-id}/examination/{id}")
    public ResponseEntity<String> update(@PathVariable("child-id") Long childId,
                                         @PathVariable("id") Long examinationId,
                                         @Valid @RequestBody ExaminationDto examinationDto) {
        examinationService.updateExamination(childId, examinationId, examinationDto);
        return ResponseEntity.ok("Examination information updated successfully");
    }

    @DeleteMapping("/child/{child-id}/examination/{id}")
    public ResponseEntity<String> delete(@PathVariable("child-id") Long childId,
                                         @PathVariable("id") Long examinationId) {
        examinationService.delete(childId, examinationId);
        return ResponseEntity.ok("Examination information deleted successfully");
    }

}