package br.ufrn.imd.cine.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.imd.cine.model.ticket.BuyTicketRecord;
import br.ufrn.imd.cine.model.ticket.SimpleTicket;
import br.ufrn.imd.cine.model.ticket.SimpleTransferData;
import br.ufrn.imd.cine.services.ticket.TicketService;
import br.ufrn.imd.cine.services.user.UserService;
import br.ufrn.imd.cineframework.models.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/ticket")
public class TicketController {

	@Autowired
	private TicketService ticketService;

	@Autowired
	private UserService userService;

	@Operation(summary = "Get tickets by logged user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Tickets found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleTicket.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")) })
	@GetMapping
	public ResponseEntity<?> getTicketsByOwner(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
		try {
			User userLogged = userService.findUserFromToken(authorizationHeader);

			List<SimpleTicket> tickets = ticketService.findByOwner(userLogged);
			return new ResponseEntity<>(tickets, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Buy a ticket")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Ticket purchased successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleTicket.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")) })
	@PostMapping("/buy")
	public ResponseEntity<?> buyTicket(@RequestBody BuyTicketRecord buyTicket,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
		User userLogged = userService.findUserFromToken(authorizationHeader);

		try {
			SimpleTicket createdTicket = ticketService.buyTicket(buyTicket, userLogged);
			return new ResponseEntity<>(createdTicket, HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Transfer a ticket")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Ticket transferred successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleTicket.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")) })
	@PostMapping("/transfer")
	public ResponseEntity<?> transferTicket(@RequestBody SimpleTransferData transferTicket,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
		User userLogged = userService.findUserFromToken(authorizationHeader);

		try {
			SimpleTicket createdTicket = ticketService.transferTicket(transferTicket, userLogged);
			return new ResponseEntity<>(createdTicket, HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Get all tickets from session")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Tickets retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleTicket.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")) })
	@GetMapping("/session/{sessionId}")
	public ResponseEntity<?> getTicketsFromSession(@PathVariable Long sessionId) {
		try {
			List<SimpleTicket> tickets = ticketService.findBySession(sessionId);
			return new ResponseEntity<>(tickets, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
