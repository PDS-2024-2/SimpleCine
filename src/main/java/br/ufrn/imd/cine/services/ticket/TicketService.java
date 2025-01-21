package br.ufrn.imd.cine.services.ticket;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufrn.imd.cine.model.ticket.BuyTicketRecord;
import br.ufrn.imd.cine.model.ticket.SimpleTicket;
import br.ufrn.imd.cine.model.ticket.SimpleTransferData;
import br.ufrn.imd.cine.repositories.SimpleTicketRepository;
import br.ufrn.imd.cine.repositories.TicketTypeRepository;
import br.ufrn.imd.cineframework.models.session.Session;
import br.ufrn.imd.cineframework.models.user.User;
import br.ufrn.imd.cineframework.repositories.GenericRepository;
import br.ufrn.imd.cineframework.repositories.session.SessionRepository;
import br.ufrn.imd.cineframework.repositories.user.UserRepository;
import br.ufrn.imd.cineframework.services.GenericService;

@Service
public class TicketService extends GenericService<SimpleTicket> {
	private static final long serialVersionUID = 1L;

	@Override
	public GenericRepository<SimpleTicket> getRepository() {
		return ticketRepository;
	}

	@Autowired
	private SimpleTicketRepository ticketRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SessionRepository sessionRepository;

	@Autowired
	private TicketTypeRepository ticketTypeRepository;

	@Autowired
	private SimpleTransferStrategy simpleTransferStrategy;

	public List<SimpleTicket> findByOwner(User userLogged) {
		if (userRepository.findById(userLogged.getId()).isEmpty()) {
			throw new IllegalArgumentException("User not found");
		} else {
			return ticketRepository.findByOwner(userRepository.findById(userLogged.getId()).get());
		}
	}

	// buy
	public SimpleTicket buyTicket(BuyTicketRecord buyTicket, User userLogged) {

		Session s = sessionRepository.findById(buyTicket.idSession())
				.orElseThrow(() -> new IllegalArgumentException("Session not found"));

		if (s.getAvailableSeats() == 0) {
			throw new IllegalArgumentException("Session full");
		}

		SimpleTicket t = new SimpleTicket();

		t.setSession(s);
		t.setTicketTypes(Set.of(ticketTypeRepository.findById(buyTicket.type()).get()));
		t.setSeat(buyTicket.seat());
		t.setPrice(buyTicket.price());
		t.setOwner(userLogged);
		t.setToken(UUID.randomUUID().toString());
		
		s.setAvailableSeats(s.getAvailableSeats() - 1);

		sessionRepository.save(s);

		return insert(t);
	}

	public SimpleTicket transferTicket(SimpleTransferData transferTicket, User userLogged) {

		SimpleTicket existingTicket = ticketRepository.findById(transferTicket.idTicket).orElseThrow(
				() -> new IllegalArgumentException("Ticket not found with id: " + transferTicket.idTicket));

		if (!existingTicket.getOwner().equals(userLogged)) {
			throw new IllegalArgumentException(
					"Logged user are not owner from the ticket with id " + transferTicket.idTicket);
		}

		existingTicket.setTranferStrategy(simpleTransferStrategy);
		return update((SimpleTicket) existingTicket.transfer(transferTicket));
	}

	public List<SimpleTicket> findBySession(Long sessionId) {
		Session s = sessionRepository.findById(sessionId)
				.orElseThrow(() -> new IllegalArgumentException("Session not found"));

		return ticketRepository.findBySession(s);
	}

}
