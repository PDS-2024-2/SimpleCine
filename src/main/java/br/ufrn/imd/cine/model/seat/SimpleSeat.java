package br.ufrn.imd.cine.model.seat;

import br.ufrn.imd.cineframework.models.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "simple_seat")
public class SimpleSeat extends AbstractEntity {
	@NotNull
	@NotBlank
	@Column(name = "seat_code")
	private String code;
	@NotNull
	@Min(1)
	@Max(50)
	@Column(name = "seat_row")
	private Integer row;
	@NotNull
	@Min(1)
	@Max(50)
	@Column(name = "seat_column")
	private Integer column;

	@NotNull
	@Column(name = "seat_type_id")
	private SeatType seatType;

	public SimpleSeat() {

	}

	public SimpleSeat(@NotNull @NotBlank String code, @NotNull @Min(1) @Max(50) Integer row,
			@NotNull @Min(1) @Max(50) Integer column, @NotNull SeatType seatType) {
		super();
		this.code = code;
		this.row = row;
		this.column = column;
		this.seatType = seatType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getRow() {
		return row;
	}

	public void setRow(Integer row) {
		this.row = row;
	}

	public Integer getColumn() {
		return column;
	}

	public void setColumn(Integer column) {
		this.column = column;
	}

	public SeatType getSeatType() {
		return seatType;
	}

	public void setSeatType(SeatType seatType) {
		this.seatType = seatType;
	}

}
