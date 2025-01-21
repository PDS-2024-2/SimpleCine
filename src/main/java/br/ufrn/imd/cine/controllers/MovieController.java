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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import br.ufrn.imd.cine.model.movie.MovieRecord;
import br.ufrn.imd.cine.services.MovieService;
import br.ufrn.imd.cineframework.models.movie.Genre;
import br.ufrn.imd.cineframework.models.movie.Movie;
import br.ufrn.imd.cineframework.themoviedb.TheMovieDBAPI;
import br.ufrn.imd.cineframework.themoviedb.models.MovieResponseAPI;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/movie")
@Validated
public class MovieController {

	@Autowired
	private MovieService movieService;

	@Autowired
	private TheMovieDBAPI theMovieDBAPI;

	@Operation(summary = "Filter movies by genre or title")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Movies filtered successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Movie.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")) })
	@GetMapping("/")
	public ResponseEntity<?> filterMovie(@RequestParam(required = false) Genre genre,
			@RequestParam(required = false) String title) {
		try {
			List<Movie> movies = movieService.findMovies(genre, title);
			return new ResponseEntity<>(movies, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An unexpected error occurred: " + e.getMessage());
		}
	}

	@Operation(summary = "Create a new movie")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Movie created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Movie.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")) })
	@PostMapping("/create")
	public ResponseEntity<?> createMovie(@RequestBody MovieRecord movieRecord) {
		try {
			Movie createdMovie = movieService.createMovie(movieRecord);
			return new ResponseEntity<>(createdMovie, HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An unexpected error occurred: " + e.getMessage());
		}
	}

	@Operation(summary = "Get all movies")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Movies retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Movie.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")) })
	@GetMapping
	public ResponseEntity<?> getAllMovies() {
		try {
			List<Movie> movies = movieService.findAll();
			return new ResponseEntity<>(movies, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An unexpected error occurred: " + e.getMessage());
		}
	}

	@Operation(summary = "Get movie by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Movie found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Movie.class))),
			@ApiResponse(responseCode = "404", description = "Movie not found", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")) })
	@GetMapping("/{id}")
	public ResponseEntity<?> getMovieById(@PathVariable Long id) {
		try {
			Movie movie = movieService.findById(id);
			return new ResponseEntity<>(movie, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>("Movie not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Update a movie by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Movie updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Movie.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "Movie not found", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")) })
	@PutMapping("/{id}")
	public ResponseEntity<?> updateMovie(@PathVariable Long id, @Valid @RequestBody MovieRecord movieRecord) {
		try {
			Movie updatedMovie = movieService.updateMovie(id, movieRecord);
			return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>("Movie not found or invalid input: " + e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Delete a movie by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Movie deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Movie not found", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")) })
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteMovie(@PathVariable Long id) {
		try {
			movieService.delete(id);
			return new ResponseEntity<>("Movie deleted successfully", HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>("Movie not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Search for movies by query", description = "This endpoint allows you to search for movies by a specific query.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Movies found successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MovieResponseAPI.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input, query parameter is missing or invalid", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json")) })
	@GetMapping("/search")
	public ResponseEntity<?> searchMovies(@RequestParam String query, @RequestParam Integer page) {
		try {
			MovieResponseAPI response = theMovieDBAPI.filter(query, page);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (IllegalArgumentException | HttpClientErrorException.BadRequest e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
