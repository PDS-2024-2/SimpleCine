package br.ufrn.imd.cine.services.promotion;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufrn.imd.cine.services.SessionService;
import br.ufrn.imd.cineframework.models.promotion.Promotion;
import br.ufrn.imd.cineframework.models.session.Session;
import br.ufrn.imd.cineframework.repositories.GenericRepository;
import br.ufrn.imd.cineframework.repositories.promotion.PromotionRepository;
import br.ufrn.imd.cineframework.services.GenericService;

@Service
public class PromotionService extends GenericService<Promotion> {
	private static final long serialVersionUID = 1L;

	@Autowired
	private PromotionRepository promotionRepository;

	@Autowired
	private SessionService sessionRepository;

	public Promotion createPromotion(Long sessionId, Double discountPercentage, LocalDateTime startDate,
			LocalDateTime endDate) {
		Session session = sessionRepository.findById(sessionId);

		List<Promotion> proms = promotionRepository.findBySession(session);

		if (proms.size() > 0) {
			for (Promotion promotion : proms) {
				promotionRepository.delete(promotion);
			}
		}

		Promotion promotion = new Promotion();
		promotion.setSession(session);
		promotion.setDiscountPercentage(discountPercentage);
		promotion.setStartDate(startDate);
		promotion.setEndDate(endDate);

		return promotionRepository.save(promotion);
	}

	public List<Promotion> getActivePromotions(Long sessionId) {
		LocalDateTime now = LocalDateTime.now();
		Session session = sessionRepository.findById(sessionId);

		return promotionRepository.findBySessionAndStartDateBeforeAndEndDateAfter(session, now, now);
	}

	@Override
	public GenericRepository<Promotion> getRepository() {
		return this.promotionRepository;
	}
}
