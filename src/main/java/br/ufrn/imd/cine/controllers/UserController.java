package br.ufrn.imd.cine.controllers;

import java.net.URI;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.imd.cine.services.user.UserService;
import br.ufrn.imd.cineframework.models.records.RecoveryJWTRecord;
import br.ufrn.imd.cineframework.models.records.SigninRecord;
import br.ufrn.imd.cineframework.models.records.SignonRecord;
import br.ufrn.imd.cineframework.models.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService service;

    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User registered successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "500", description = "Error on registering user", content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<?> signon(@RequestBody(required = true) @Valid SignonRecord signonRecord) {
        try {
            User userCreated = service.register(signonRecord);
            URI uri = URI.create("/user/" + userCreated.getId());
            return ResponseEntity.created(uri).build();
        } catch (NoSuchAlgorithmException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error on registering user: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @Operation(summary = "User login")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User logged in successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecoveryJWTRecord.class))),
        @ApiResponse(responseCode = "500", description = "Error on login", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/login")
    public ResponseEntity<?> signin(@RequestBody(required = true) @Valid SigninRecord signinRecord) {
        try {
            RecoveryJWTRecord token = service.login(signinRecord);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error on login: " + e.getMessage());
        }
    }
}
