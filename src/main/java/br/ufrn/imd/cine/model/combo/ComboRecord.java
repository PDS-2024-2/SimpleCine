package br.ufrn.imd.cine.model.combo;

import java.math.BigDecimal;
import java.util.List;

import br.ufrn.imd.cineframework.models.combo.ComboItem;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ComboRecord(
		@NotBlank(message = "O título não pode estar em branco.")
		@Size(max = 100, message = "O título pode ter no máximo 100 caracteres.")
		String title,
		@NotBlank(message = "A descrição não pode estar em branco.")
		String description,
		@NotNull(message = "O preço não pode ser nulo.")
		@DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que zero.")
		BigDecimal price,
		@NotNull
		List<ComboItem> items) {
}
