package br.ufrn.imd.cine.model.layout;

import java.util.List;

import br.ufrn.imd.cine.model.seat.SimpleSeat;
import br.ufrn.imd.cineframework.models.layout.AbstractLayout;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "simple_layout")
public class SimpleLayout extends AbstractLayout {

	@Column(name = "rows")
	private Integer rows;
	@Column(name = "columns")
	private Integer columns;

	@NotNull
	@NotEmpty
	@ManyToMany
	@JoinTable(name = "layout_has_seat", joinColumns = @JoinColumn(name = "layout_id"), inverseJoinColumns = @JoinColumn(name = "seat_id"))
	private List<SimpleSeat> seats;

	public SimpleLayout() {
	}

	public SimpleLayout(Double size, Integer rows, Integer columns, @NotNull @NotEmpty List<SimpleSeat> seats) {
		super(size);
		this.rows = rows;
		this.columns = columns;
		this.seats = seats;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Integer getColumns() {
		return columns;
	}

	public void setColumns(Integer columns) {
		this.columns = columns;
	}

	public List<SimpleSeat> getSeats() {
		return seats;
	}

	public void setSeats(List<SimpleSeat> seats) {
		this.seats = seats;
	}

}
