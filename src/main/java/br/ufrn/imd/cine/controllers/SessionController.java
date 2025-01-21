package br.ufrn.imd.cine.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.imd.cine.model.session.SessionRecord;
import br.ufrn.imd.cine.services.SessionService;
import br.ufrn.imd.cine.services.user.UserService;
import br.ufrn.imd.cineframework.models.session.Session;
import br.ufrn.imd.cineframework.models.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/session")
public class SessionController {

	@Autowired
	private SessionService sessionService;

	@Autowired
	private UserService userService;

	@Operation(summary = "Find sessions by movie ID and date")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sessions found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Session.class))),
			@ApiResponse(responseCode = "404", description = "Sessions not found", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")) })
	@GetMapping
	public ResponseEntity<List<Session>> findByMovieIdAndDate(@RequestParam Integer movieId,
			@RequestParam LocalDate date) {
		try {
			List<Session> sessions = sessionService.findByMovieIdAndDate(movieId, date);
			if (sessions.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(sessions, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@Operation(summary = "Create a new session")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Session created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Session.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")) })
	@PostMapping
	public ResponseEntity<?> createSession(@Valid @RequestBody SessionRecord sessionRecord) {
		try {
			Session session = sessionService.createSession(sessionRecord);
			return new ResponseEntity<>(session, HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Get all sessions")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sessions retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Session.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")) })
	@GetMapping("/all")
	public ResponseEntity<List<Session>> getAllSessions() {
		try {
			List<Session> sessions = sessionService.findAll();
			return new ResponseEntity<>(sessions, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Get session by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Session found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Session.class))),
			@ApiResponse(responseCode = "404", description = "Session not found", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")) })
	@GetMapping("/{id}")
	public ResponseEntity<?> getSessionById(@PathVariable Long id) {
		try {
			Session session = sessionService.findById(id);
			if (session == null) {
				return new ResponseEntity<>("Session not found", HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(session, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Update a session")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Session updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Session.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "Session not found", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")) })
	@PutMapping("/{id}")
	public ResponseEntity<?> updateSession(@PathVariable Long id, @Valid @RequestBody SessionRecord sessionRecord) {
		try {
			Session updatedSession = sessionService.updateSession(id, sessionRecord);
			return new ResponseEntity<>(updatedSession, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>("Session not found or invalid input: " + e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Delete a session")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Session deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Session not found", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")) })
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteSession(@PathVariable Long id) {
		try {
			sessionService.delete(id);
			return new ResponseEntity<>("Session deleted successfully", HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>("Session not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Get next Sessions")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sessions retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Session.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")) })
	@GetMapping("/next")
	public ResponseEntity<List<Session>> getNextSessions() {
		try {
			List<Session> sessions = sessionService.findSessionsForNextFiveDays();
			return new ResponseEntity<>(sessions, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/recommendated")
	public ResponseEntity<?> getRecommendatedSessions(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
		try {
			User userLogged = userService.findUserFromToken(authorizationHeader);
			List<Session> sessions = sessionService.findSessionsRecommendated(userLogged);
			return new ResponseEntity<>(sessions, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
