package br.ufrn.imd.cine.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.imd.cine.model.combo.ComboRecord;
import br.ufrn.imd.cine.services.combo.ComboService;
import br.ufrn.imd.cineframework.models.combo.Combo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/combo")
@Validated
public class ComboController {

	@Autowired
	private ComboService comboService;
	
	@Operation(summary = "Create a new combo")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Combo created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Combo.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")) })
	@PostMapping
	public ResponseEntity<?> createCombo(@RequestBody ComboRecord comboRecord) {
		try {
			Combo combo = comboService.createCombo(comboRecord);
			return ResponseEntity.status(HttpStatus.CREATED).body(combo);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An unexpected error occurred: " + e.getMessage());
		}
	}

	@Operation(summary = "Get all combos")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Combos retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Combo.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")) })
	@GetMapping
	public ResponseEntity<?> getAllCombos() {
		try {
			List<Combo> combos = comboService.findAll();
			return ResponseEntity.ok(combos);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An unexpected error occurred: " + e.getMessage());
		}
	}

	@Operation(summary = "Get combo by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Combo found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Combo.class))),
			@ApiResponse(responseCode = "404", description = "Combo not found", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")) })
	@GetMapping("/{id}")
	public ResponseEntity<?> getComboById(@PathVariable Long id) {
		try {
			Combo combo = comboService.findById(id);
			if (combo == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Combo not found");
			}
			return ResponseEntity.ok(combo);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An unexpected error occurred: " + e.getMessage());
		}
	}

	@Operation(summary = "Update a combo by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Combo updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Combo.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "Combo not found", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")) })
	@PostMapping("/{id}")
	public ResponseEntity<?> updateCombo(@PathVariable Long id, @RequestBody ComboRecord comboRecord) {
		try {
			Combo updatedCombo = comboService.updateCombo(id, comboRecord);
			return ResponseEntity.ok(updatedCombo);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Combo not found or invalid input: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An unexpected error occurred: " + e.getMessage());
		}
	}

	@Operation(summary = "Delete a combo by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Combo deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Combo not found", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")) })
	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteCombo(@PathVariable Long id) {
		try {
			comboService.delete(id);
			return ResponseEntity.ok("Combo deleted successfully");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Combo not found: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An unexpected error occurred: " + e.getMessage());
		}
	}

}
