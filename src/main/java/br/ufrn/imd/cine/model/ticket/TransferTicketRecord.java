package br.ufrn.imd.cine.model.ticket;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransferTicketRecord(
		@NotNull
		@Positive
		Long idTicket,
		@NotNull
		@NotBlank
		String userToTransfer
		) {

}
