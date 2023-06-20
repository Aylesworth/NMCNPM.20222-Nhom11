package com.aylesw.mch.backend.controller;

import com.aylesw.mch.backend.dto.ChildDto;
import com.aylesw.mch.backend.service.ChildService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/child")
@RequiredArgsConstructor
public class ChildController {
    private final ChildService childService;

    @PostMapping("/approve-registration")
    public ResponseEntity<String> approveChildRegistration(@RequestParam(value = "id", required = true) Long childRegistrationId) {
        childService.approveChildRegistration(childRegistrationId);
        return ResponseEntity.ok("Child profile registration approved");
    }

    @PostMapping("/approve-change")
    public ResponseEntity<String> approveChildChange(@RequestParam(value = "id", required = true) Long childChangeId) {
        childService.approveChildChange(childChangeId);
        return ResponseEntity.ok("Child profile change approved");
    }

    @GetMapping("/search")
    public ResponseEntity<List<ChildDto>> search(@RequestParam(value = "q", required = true) String keyword) {
        return ResponseEntity.ok(childService.search(keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChildDto> getChild(@PathVariable("id") Long id) {
        return ResponseEntity.ok(childService.getChild(id));
    }

    @PostMapping("/add")
    public ResponseEntity<String> addChild(@Valid @RequestBody ChildDto childDto,
                                           @RequestParam("parent-id") Long parentId) {
        childService.addChild(childDto, parentId);
        return ResponseEntity.ok("Child profile added successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateChild(@PathVariable Long id,
                                              @Valid @RequestBody ChildDto childDto) {
        childService.updateChild(id, childDto);
        return ResponseEntity.ok("Child profile updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteChild(@PathVariable("id") Long id) {
        childService.deleteChild(id);
        return ResponseEntity.ok("Child profile deleted successfully");
    }
}
