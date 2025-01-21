package br.ufrn.imd.cine.model.seat;

import br.ufrn.imd.cineframework.models.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "seat_type")
public class SeatType extends AbstractEntity {
	@Column(name = "price_increase")
	private Double priceIncrease;

	@Column(name = "code")
	private String code;

	@Column(name = "description")
	private String description;

	public SeatType() {
	}

	public SeatType(Double priceIncrease, String code, String description) {
		super();
		this.priceIncrease = priceIncrease;
		this.code = code;
		this.description = description;
	}

	public Double getPriceIncrease() {
		return priceIncrease;
	}

	public void setPriceIncrease(Double priceIncrease) {
		this.priceIncrease = priceIncrease;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
