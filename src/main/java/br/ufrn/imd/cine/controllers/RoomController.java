package br.ufrn.imd.cine.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.imd.cine.model.room.CreateRoomRecord;
import br.ufrn.imd.cine.services.room.RoomService;
import br.ufrn.imd.cineframework.models.room.Room;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/room")
@Validated
public class RoomController {

	@Autowired
	private RoomService service;

	@Operation(summary = "Create a new room")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Room created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Room.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")) })
	@PostMapping
	public ResponseEntity<?> createRoom(@Valid @RequestBody CreateRoomRecord roomRecord) {
		try {
			Room room = service.createRoom(roomRecord);
			return new ResponseEntity<>(room, HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>("Invalid input: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Get room by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Room found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Room.class))),
			@ApiResponse(responseCode = "404", description = "Room not found", content = @Content(mediaType = "application/json")) })
	@GetMapping("/{id}")
	public ResponseEntity<?> getRoomById(@PathVariable Long id) {
		try {
			Room room = service.findById(id);
			return new ResponseEntity<>(room, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>("Room not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Get all rooms")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Rooms retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Room.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")) })
	@GetMapping
	public ResponseEntity<?> getAllRooms() {
		try {
			List<Room> rooms = service.findAll();
			return new ResponseEntity<>(rooms, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Update a room by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Room updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Room.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "Room not found", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")) })
	@PutMapping("/{id}")
	public ResponseEntity<?> updateRoom(@PathVariable Long id, @Valid @RequestBody Room roomRecord) {
		try {
			Room updatedRoom = service.update(roomRecord);
			return new ResponseEntity<>(updatedRoom, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>("Room not found or invalid input: " + e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Delete a room by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Room deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Room not found", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")) })
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
		try {
			service.delete(id);
			return new ResponseEntity<>("Room deleted successfully", HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>("Room not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
