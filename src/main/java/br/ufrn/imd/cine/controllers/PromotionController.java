package br.ufrn.imd.cine.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.imd.cine.services.promotion.PromotionService;
import br.ufrn.imd.cineframework.models.promotion.Promotion;
import br.ufrn.imd.cineframework.models.promotion.PromotionCreateRecord;

@RestController
@RequestMapping("/api/promotion")
public class PromotionController {

	@Autowired
	private PromotionService promotionService;

	@PostMapping("/create/{sessionId}")
	public ResponseEntity<?> createPromotion(@PathVariable Long sessionId, @RequestBody PromotionCreateRecord pcr) {
		try {
			String startDateStr = pcr.startDate();
			String endDateStr = pcr.endDate();

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime start = LocalDateTime.parse(startDateStr, formatter);
			LocalDateTime end = LocalDateTime.parse(endDateStr, formatter);

			if (start.isAfter(end)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("A data de início não pode ser posterior à data de término.");
			}

			Promotion promotion = promotionService.createPromotion(sessionId, pcr.discountPercentage(), start, end);
			if (promotion == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sessão não encontrada para a promoção.");
			}

			return ResponseEntity.ok(promotion);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao criar a promoção: " + e.getMessage());
		}
	}

	@GetMapping("/active/{sessionId}")
	public ResponseEntity<?> getActivePromotions(@PathVariable Long sessionId) {
		try {
			List<Promotion> activePromotions = promotionService.getActivePromotions(sessionId);
			if (activePromotions == null || activePromotions.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Nenhuma promoção ativa encontrada para esta sessão.");
			}

			return ResponseEntity.ok(activePromotions);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao recuperar as promoções: " + e.getMessage());
		}
	}
}
