package br.ufrn.imd.cine.model.ai;

import java.util.List;

import br.ufrn.imd.cineframework.models.movie.Movie;
import jakarta.validation.constraints.NotNull;

public record PromptMovieRecomendationRecord(@NotNull List<Movie> moviesAlreadyBought,
		@NotNull List<Movie> moviesAvailable) {
}