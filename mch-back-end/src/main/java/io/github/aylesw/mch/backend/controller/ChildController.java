package io.github.aylesw.mch.backend.controller;

import io.github.aylesw.mch.backend.dto.ChildDto;
import io.github.aylesw.mch.backend.entity.ChildChange;
import io.github.aylesw.mch.backend.entity.ChildRegistration;
import io.github.aylesw.mch.backend.service.ChildService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/children")
@RequiredArgsConstructor
public class ChildController {
    private final ChildService childService;

    @GetMapping("/find-by-parent")
    public ResponseEntity<List<ChildDto>> getByParentId(@RequestParam(value = "id", required = true) Long parentId) {
        return ResponseEntity.ok(childService.getByParentId(parentId));
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerChild(@Valid @RequestBody ChildDto childDto,
                                                @RequestParam("parent-id") Long parentId) {
        childService.registerChild(childDto, parentId);
        return ResponseEntity.ok("Child profile waiting for approval");
    }

    @PostMapping("/{id}/request-change")
    public ResponseEntity<String> requestChildChange(@PathVariable("id") Long childId,
                                                     @Valid @RequestBody ChildDto childDto) {
        childService.requestChildChange(childId, childDto);
        return ResponseEntity.ok("Child profile change waiting for approval");
    }

    @GetMapping("/pending-registrations")
    public ResponseEntity<List<ChildRegistration>> getAllPendingRegistrations() {
        return ResponseEntity.ok(childService.getAllPendingRegistrations());
    }

    @GetMapping("/pending-changes")
    public ResponseEntity<List<ChildChange>> getAllPendingChanges() {
        return ResponseEntity.ok(childService.getAllPendingChanges());
    }

    @PostMapping("/approve-registration")
    public ResponseEntity<String> approveChildRegistration(@RequestParam(value = "id", required = true) Long childRegistrationId) {
        childService.approveChildRegistration(childRegistrationId);
        return ResponseEntity.ok("Child profile registration approved");
    }

    @PostMapping("/reject-registration")
    public ResponseEntity<String> rejectChildRegistration(
            @RequestParam(value = "id", required = true) Long childRegistrationId,
            @RequestParam(value = "reason", required = true) String reason) {
        childService.rejectChildRegistration(childRegistrationId, reason);
        return ResponseEntity.ok("Child profile registration rejected");
    }

    @PostMapping("/approve-change")
    public ResponseEntity<String> approveChildChange(@RequestParam(value = "id", required = true) Long childChangeId) {
        childService.approveChildChange(childChangeId);
        return ResponseEntity.ok("Child profile change approved");
    }

    @PostMapping("/reject-change")
    public ResponseEntity<String> rejectChildChange(
            @RequestParam(value = "id", required = true) Long childChangeId,
            @RequestParam(value = "reason", required = true) String reason) {
        childService.rejectChildChange(childChangeId, reason);
        return ResponseEntity.ok("Child profile change rejected");
    }

    @GetMapping("/search")
    public ResponseEntity<List<ChildDto>> search(@RequestParam(value = "q", required = true) String keyword) {
        return ResponseEntity.ok(childService.search(keyword));
    }

    @GetMapping("")
    public ResponseEntity<List<ChildDto>> getAllChildren() {
        return ResponseEntity.ok(childService.getAllChildren());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChildDto> getChild(@PathVariable("id") Long id) {
        return ResponseEntity.ok(childService.getChild(id));
    }

    @PostMapping("")
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
