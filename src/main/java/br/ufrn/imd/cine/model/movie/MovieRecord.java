package br.ufrn.imd.cine.model.movie;

import java.time.LocalDate;

import br.ufrn.imd.cineframework.models.movie.AgeRating;
import br.ufrn.imd.cineframework.models.movie.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MovieRecord(
		@NotNull 
		@NotBlank(message = "O título não pode estar em branco") 
		@Size(min = 2, max = 200, message = "O título deve ter entre 2 e 100 caracteres")
		String title,
		String synopsis,
		Integer duration,
		String imagePath,
		AgeRating ageRating,
		LocalDate releaseDate,
		Genre genre) {
}
