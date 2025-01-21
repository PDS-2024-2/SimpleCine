package br.ufrn.imd.cine.model.session;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import br.ufrn.imd.cineframework.models.session.SessionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SessionRecord(
		@NotNull(message = "O filme da sessão deve ser informado.")
		Long idMovie,
		@NotNull(message = "A sala da sessão deve ser informada.")
		Long idRoom,
		@NotNull(message = "A data da sessão deve ser informada.")
		LocalDate date,
		@NotNull(message = "A hora da sessão deve ser informada.")
		LocalTime time,
		@NotBlank(message = "O idioma da sessão não pode estar vazio.")
		String language,
		@Positive(message = "O número de assentos disponíveis deve ser maior que zero.")
		Integer availableSeats,
		@NotNull(message = "O preço do ingresso deve ser informado.")
		@DecimalMin(value = "0.01", message = "O preço do ingresso deve ser maior que zero.")
		BigDecimal ticketPrice,
		SessionType sessionType) {
}
