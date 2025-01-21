package br.ufrn.imd.cine.services.feedback;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufrn.imd.cineframework.models.feedback.Feedback;
import br.ufrn.imd.cineframework.repositories.GenericRepository;
import br.ufrn.imd.cineframework.repositories.feedback.FeedbackRepository;
import br.ufrn.imd.cineframework.services.GenericService;

@Service
public class FeedbackService extends GenericService<Feedback> {
	private static final long serialVersionUID = 1L;
	@Autowired
	private FeedbackRepository feedbackRepository;

	@Override
	public GenericRepository<Feedback> getRepository() {
		return this.feedbackRepository;
	}

	public List<Feedback> findFeedbacksByMovie(Long movieId) {
		return feedbackRepository.findByMovieId(movieId);
	}

	public List<Feedback> findFeedbacksByUser(Long userId) {
		return feedbackRepository.findByUserId(userId);
	}

	public Feedback findFeedbackByTicket(Long ticketId) {
		return feedbackRepository.findByTicketId(ticketId);
	}

	public Map<String, List<Feedback>> getAverageRatingsByMovie() {
		List<Feedback> feedbacks = feedbackRepository.findAll();

		return feedbacks.stream().collect(Collectors.groupingBy(feedback -> feedback.getMovie().getTitle()));
	}

}
