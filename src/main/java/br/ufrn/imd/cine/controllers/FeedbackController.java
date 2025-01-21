package br.ufrn.imd.cine.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.imd.cine.model.ticket.SimpleTicket;
import br.ufrn.imd.cine.services.feedback.FeedbackService;
import br.ufrn.imd.cine.services.ticket.TicketService;
import br.ufrn.imd.cine.services.user.UserService;
import br.ufrn.imd.cineframework.models.feedback.Feedback;
import br.ufrn.imd.cineframework.models.feedback.FeedbackRequest;
import br.ufrn.imd.cineframework.models.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

	@Autowired
	private FeedbackService feedbackService;

	@Autowired
	private TicketService ticketService;

	@Autowired
	private UserService userService;

	@Operation(summary = "Create feedback for a ticket")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Feedback created successfully", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")) })
	@PostMapping
	public ResponseEntity<?> createFeedback(@Valid @RequestBody FeedbackRequest request,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
		try {
			SimpleTicket ticket = ticketService.findById(request.getTicketId());
			if (ticket == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket não encontrado.");
			}

			User user = userService.findUserFromToken(authorizationHeader);
			if (user == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autorizado.");
			}

			Feedback feedback = new Feedback();
			feedback.setTicket(ticket);
			feedback.setMovie(ticket.getSession().getMovie());
			feedback.setUser(user);
			feedback.setRating(request.getRating());
			feedback.setComment(request.getComment());

			feedbackService.insert(feedback);

			return ResponseEntity.ok("Feedback criado com sucesso");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao criar o feedback: " + e.getMessage());
		}
	}

	@Operation(summary = "Get feedback by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Feedback found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Feedback.class))),
			@ApiResponse(responseCode = "404", description = "Feedback not found", content = @Content(mediaType = "application/json")) })
	@GetMapping("/{id}")
	public ResponseEntity<Feedback> getFeedbackById(@PathVariable Long id) {
		try {
			Feedback feedback = feedbackService.findById(id);
			if (feedback == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			return ResponseEntity.ok(feedback);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@Operation(summary = "Get feedbacks for a specific movie")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Feedbacks retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Feedback.class))),
			@ApiResponse(responseCode = "404", description = "No feedbacks found for the movie", content = @Content(mediaType = "application/json")) })
	@GetMapping("/movie/{movieId}")
	public ResponseEntity<List<Feedback>> getFeedbacksByMovie(@PathVariable Long movieId) {
		try {
			List<Feedback> feedbacks = feedbackService.findFeedbacksByMovie(movieId);
			if (feedbacks.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			return ResponseEntity.ok(feedbacks);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@Operation(summary = "Get average ratings for all movies")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Average ratings retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
			@ApiResponse(responseCode = "204", description = "No ratings available", content = @Content(mediaType = "application/json")) })
	@GetMapping("/movies")
	public ResponseEntity<Map<String, List<Feedback>>> getAverageRatings() {
		try {
			Map<String, List<Feedback>> averageRatings = feedbackService.getAverageRatingsByMovie();
			if (averageRatings.isEmpty()) {
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.ok(averageRatings);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@Operation(summary = "Get feedbacks for a specific user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Feedbacks retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Feedback.class))),
			@ApiResponse(responseCode = "404", description = "No feedbacks found for the user", content = @Content(mediaType = "application/json")) })
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<Feedback>> getFeedbacksByUser(@PathVariable Long userId) {
		try {
			List<Feedback> feedbacks = feedbackService.findFeedbacksByUser(userId);
			if (feedbacks.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			return ResponseEntity.ok(feedbacks);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@Operation(summary = "Delete feedback by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Feedback deleted successfully", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "Feedback not found", content = @Content(mediaType = "application/json")) })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
		try {
			feedbackService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Operation(summary = "Get feedback for a specific ticket")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Feedback retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Feedback.class))),
			@ApiResponse(responseCode = "404", description = "Feedback not found for the ticket", content = @Content(mediaType = "application/json")) })
	@GetMapping("/ticket/{ticketId}")
	public ResponseEntity<Feedback> getFeedbackByTicket(@PathVariable Long ticketId) {
		try {
			Feedback feedback = feedbackService.findFeedbackByTicket(ticketId);
			if (feedback == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			return ResponseEntity.ok(feedback);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@Operation(summary = "Update feedback by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Feedback updated successfully", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "Feedback not found", content = @Content(mediaType = "application/json")) })
	@PostMapping("/{id}")
	public ResponseEntity<?> updateFeedback(@PathVariable Long id, @Valid @RequestBody FeedbackRequest request) {
		try {
			Feedback feedback = feedbackService.findById(id);

			if (feedback == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Feedback não encontrado.");
			}

			feedback.setRating(request.getRating());
			feedback.setComment(request.getComment());
			feedbackService.update(feedback);

			return ResponseEntity.ok("Feedback atualizado com sucesso");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao atualizar o feedback: " + e.getMessage());
		}
	}
}
