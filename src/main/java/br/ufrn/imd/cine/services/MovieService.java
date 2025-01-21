package br.ufrn.imd.cine.services;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufrn.imd.cine.model.movie.MovieRecord;
import br.ufrn.imd.cineframework.models.movie.Genre;
import br.ufrn.imd.cineframework.models.movie.Movie;
import br.ufrn.imd.cineframework.repositories.GenericRepository;
import br.ufrn.imd.cineframework.repositories.movie.MovieRepository;
import br.ufrn.imd.cineframework.services.GenericService;
import jakarta.validation.Valid;

@Service
public class MovieService extends GenericService<Movie> {
	private static final long serialVersionUID = 1L;

	@Autowired
	private MovieRepository movieRepository;

	@Override
	public GenericRepository<Movie> getRepository() {
		return movieRepository;
	}

	public List<Movie> findMovies(Genre genre, String title) {
		if (Objects.nonNull(genre)) {
			return movieRepository.findByGenre(String.valueOf(genre));
		}

		if (Objects.nonNull(title) && !title.trim().isEmpty()) {
			return movieRepository.findByTitleLike(title);
		}

		return movieRepository.findAll();
	}

	public Movie createMovie(MovieRecord movieRecord) {
		Movie movie = new Movie();

		movie.setTitle(movieRecord.title());
		movie.setSynopsis(movieRecord.synopsis());
		movie.setDuration(movieRecord.duration());
		movie.setAgeRating(movieRecord.ageRating());
		movie.setReleaseDate(movieRecord.releaseDate());
		movie.setGenre(movieRecord.genre());
		movie.setImagePath(movieRecord.imagePath());
		return insert(movie);
	}

	public Movie updateMovie(Long id, @Valid MovieRecord movieRecord) {
		Movie existingMovie = movieRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Movie not found with id: " + id));

		existingMovie.setTitle(movieRecord.title());
		existingMovie.setSynopsis(movieRecord.synopsis());
		existingMovie.setDuration(movieRecord.duration());
		existingMovie.setAgeRating(movieRecord.ageRating());
		existingMovie.setReleaseDate(movieRecord.releaseDate());
		existingMovie.setGenre(movieRecord.genre());

		return update(existingMovie);
	}
}
