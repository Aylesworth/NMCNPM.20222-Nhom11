package io.github.aylesw.mch.backend.controller;

import io.github.aylesw.mch.backend.dto.InjectionDto;
import io.github.aylesw.mch.backend.model.Vaccine;
import io.github.aylesw.mch.backend.service.InjectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class InjectionController {

    private final InjectionService injectionService;

    @GetMapping("/children/{child-id}/injections")
    public ResponseEntity<List<InjectionDto>> getInjections(@PathVariable("child-id") Long childId) {
        return ResponseEntity.ok(injectionService.getInjections(childId));
    }

    @PostMapping("/children/{child-id}/injections")
    public ResponseEntity<String> addInjection(@PathVariable("child-id") Long childId,
                                               @Valid @RequestBody InjectionDto injectionDto) {
        injectionService.addInjection(childId, injectionDto);
        return ResponseEntity.ok("New injection information added successfully");
    }

    @PutMapping("/children/{child-id}/injections/{id}")
    public ResponseEntity<String> updateInjection(@PathVariable("child-id") Long childId,
                                                  @PathVariable("id") Long injectionId,
                                                  @Valid @RequestBody InjectionDto injectionDto) {
        injectionService.updateInjection(childId, injectionId, injectionDto);
        return ResponseEntity.ok("Injection information updated successfully");
    }

    @DeleteMapping("/children/{child-id}/injections/{id}")
    public ResponseEntity<String> deleteInjection(@PathVariable("child-id") Long childId,
                                                  @PathVariable("id") Long injectionId) {
        injectionService.deleteInjection(childId, injectionId);
        return ResponseEntity.ok("Injection information deleted successfully");
    }

    @GetMapping("/vaccines")
    public ResponseEntity<List<Vaccine>> getAllVaccines() {
        return ResponseEntity.ok(injectionService.getAllVaccines());
    }

    @PostMapping("/children/{child-id}/injections/{id}/handle-reaction")
    public ResponseEntity<String> handleReaction(
            @PathVariable("child-id") Long childId,
            @PathVariable("id") Long injectionId,
            @RequestBody Map<String,String> requestBody
            ) {
        injectionService.handleReaction(childId, injectionId, requestBody.get("reaction"), requestBody.get("advice"));
        return ResponseEntity.ok("Advice for reaction sent successfully");
    }
}