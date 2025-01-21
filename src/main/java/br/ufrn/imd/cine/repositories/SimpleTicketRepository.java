package br.ufrn.imd.cine.repositories;

import java.util.List;

import br.ufrn.imd.cine.model.ticket.SimpleTicket;
import br.ufrn.imd.cineframework.models.session.Session;
import br.ufrn.imd.cineframework.models.user.User;
import br.ufrn.imd.cineframework.repositories.GenericRepository;

public interface SimpleTicketRepository extends GenericRepository<SimpleTicket> {
	List<SimpleTicket> findByOwner(User owner);

	List<SimpleTicket> findBySession(Session session);
}
