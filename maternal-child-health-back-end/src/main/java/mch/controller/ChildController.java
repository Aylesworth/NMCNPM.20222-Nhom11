package mch.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mch.dto.ChildDto;
import mch.dto.SimpleResponse;
import mch.service.ChildService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ChildController {

	private final ChildService childService;

	@GetMapping("/children")
	public ResponseEntity<List<ChildDto>> getAllChildren() {
		return ResponseEntity.ok(childService.getAllChildren());
	}

	@GetMapping("/children/{id}")
	public ResponseEntity<ChildDto> getChildById(@PathVariable Long id) {
		return ResponseEntity.ok(childService.getChildById(id));
	}

	@GetMapping("/children/findByParent")
	public ResponseEntity<List<ChildDto>> getChildrenOfUser(@RequestParam(name = "id", required = true) Long userId) {
		return ResponseEntity.ok(childService.getChildrenOfUser(userId));
	}

	@PostMapping("/children")
	public ResponseEntity<SimpleResponse> addChild(@Valid @RequestBody ChildDto childDto) {
		childService.addChild(childDto);
		return ResponseEntity.ok(new SimpleResponse("Child added successfully"));
	}

	@PutMapping("/children")
	public ResponseEntity<SimpleResponse> updateChild(@Valid @RequestBody ChildDto childDto) {
		childService.updateChild(childDto);
		return ResponseEntity.ok(new SimpleResponse("Child updated successfully"));
	}
}
