package br.ufrn.imd.cine.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufrn.imd.cine.model.ai.PromptMovieRecomendationRecord;
import br.ufrn.imd.cine.model.session.SessionRecord;
import br.ufrn.imd.cine.repositories.SimpleTicketRepository;
import br.ufrn.imd.cine.services.ai.SimpleAIService;
import br.ufrn.imd.cineframework.models.movie.Movie;
import br.ufrn.imd.cineframework.models.room.Room;
import br.ufrn.imd.cineframework.models.session.Session;
import br.ufrn.imd.cineframework.models.user.User;
import br.ufrn.imd.cineframework.repositories.GenericRepository;
import br.ufrn.imd.cineframework.repositories.movie.MovieRepository;
import br.ufrn.imd.cineframework.repositories.room.RoomRepository;
import br.ufrn.imd.cineframework.repositories.session.SessionRepository;
import br.ufrn.imd.cineframework.services.GenericService;
import jakarta.validation.Valid;

@Service
public class SessionService extends GenericService<Session> {
	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionRepository sessionRepository;

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private SimpleTicketRepository ticketRepository;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private SimpleAIService oaiService;

	@Override
	public GenericRepository<Session> getRepository() {
		return sessionRepository;
	}

	public List<Session> findByMovieIdAndDate(Integer movieId, LocalDate date) throws IllegalArgumentException {
		if (movieId == null || date == null) {
			throw new IllegalArgumentException("Movie ID and date must not be null");
		}

		return sessionRepository.findByMovieIdAndDate(movieId, date);
	}

	public Session createSession(@Valid SessionRecord sessionRecord) {
		Session session = new Session();

		Movie movie = movieRepository.findById(sessionRecord.idMovie())
				.orElseThrow(() -> new IllegalArgumentException("Movie not found"));
		session.setMovie(movie);

		Room room = roomRepository.findById(sessionRecord.idRoom())
				.orElseThrow(() -> new IllegalArgumentException("Room not found"));
		session.setRoom(room);

		session.setDate(sessionRecord.date());

		session.setTime(sessionRecord.time());

		session.setLanguage(sessionRecord.language());

		session.setAvailableSeats(room.getCapacity());

		session.setTicketPrice(sessionRecord.ticketPrice());

		session.setSessionType(sessionRecord.sessionType());

		return insert(session);
	}

	public Session updateSession(Long id, @Valid SessionRecord sessionRecord) {
		Session existingSession = sessionRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Session not found with id: " + id));

		existingSession.setMovie(movieRepository.findById(sessionRecord.idMovie())
				.orElseThrow(() -> new IllegalArgumentException("Movie not found")));

		existingSession.setRoom(roomRepository.findById(sessionRecord.idRoom())
				.orElseThrow(() -> new IllegalArgumentException("Room not found")));

		existingSession.setDate(sessionRecord.date());

		existingSession.setTime(sessionRecord.time());

		existingSession.setLanguage(sessionRecord.language());

		existingSession.setAvailableSeats(sessionRecord.availableSeats());

		existingSession.setTicketPrice(sessionRecord.ticketPrice());

		existingSession.setSessionType(sessionRecord.sessionType());

		return update(existingSession);
	}

	public List<Session> findSessionsForNextFiveDays() {
		return sessionRepository.findSessionsForNextFiveDays(LocalDate.now());
	}

	public List<Session> findSessionsRecommendated(User userLogged) throws Exception {
		List<Movie> moviesWatched = ticketRepository.findByOwner(userLogged).stream()
				.map(t -> t.getSession().getMovie()).collect(Collectors.toList());

		List<Movie> moviesAvailable = findSessionsForNextFiveDays().stream().map(t -> t.getMovie())
				.collect(Collectors.toList());

		PromptMovieRecomendationRecord r = new PromptMovieRecomendationRecord(moviesWatched, moviesAvailable);

		List<Long> idsMovie = Arrays.asList(oaiService.analysis(r).split(", ")).stream().map(i -> Long.parseLong(i))
				.collect(Collectors.toList());

		List<Session> recommendatedSessions = new ArrayList<>();

		for (Long idMovie : idsMovie) {
			try {
				Movie m = movieRepository.findById(idMovie).get();
				List<Session> sessions = sessionRepository.findByMovie(m);
				recommendatedSessions.addAll(sessions);
			} catch (java.util.NoSuchElementException e) {
			}
		}

		return recommendatedSessions;
	}
}
