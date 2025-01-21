package br.ufrn.imd.cine.model.ticket;

import java.util.Set;

import br.ufrn.imd.cineframework.models.combo.Combo;
import br.ufrn.imd.cineframework.models.session.Session;
import br.ufrn.imd.cineframework.models.ticket.AbstractTicket;
import br.ufrn.imd.cineframework.models.ticket.TicketType;
import br.ufrn.imd.cineframework.models.ticket.TransferStrategy;
import br.ufrn.imd.cineframework.models.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "simple_ticket")
public class SimpleTicket extends AbstractTicket {
	@Column(name = "seat")
	private String seat;
	@Column(name = "id_owner")
	private User owner;

	@Column(name = "is_session")
	private Session session;

	public SimpleTicket() {
	}

	public SimpleTicket(Double price, String token, Set<TicketType> ticketTypes, Set<Combo> combos,
			TransferStrategy tranferStrategy, String seat, User owner, Session session) {
		super(price, token, ticketTypes, combos, tranferStrategy);
		this.seat = seat;
		this.owner = owner;
		this.session = session;
	}

	public String getSeat() {
		return seat;
	}

	public void setSeat(String seat) {
		this.seat = seat;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
}
