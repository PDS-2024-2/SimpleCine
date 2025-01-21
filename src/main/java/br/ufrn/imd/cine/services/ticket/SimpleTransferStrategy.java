package br.ufrn.imd.cine.services.ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.ufrn.imd.cine.model.ticket.SimpleTicket;
import br.ufrn.imd.cine.model.ticket.SimpleTransferData;
import br.ufrn.imd.cine.repositories.SimpleTicketRepository;
import br.ufrn.imd.cineframework.models.ticket.AbstractTicket;
import br.ufrn.imd.cineframework.models.ticket.TransferData;
import br.ufrn.imd.cineframework.models.ticket.TransferStrategy;
import br.ufrn.imd.cineframework.models.user.User;
import br.ufrn.imd.cineframework.repositories.user.UserRepository;

@Component
public class SimpleTransferStrategy implements TransferStrategy {

	@Autowired
	private SimpleTicketRepository ticketRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public AbstractTicket transfer(TransferData data) {
		SimpleTransferData simpleTransferData = (SimpleTransferData) data;

		SimpleTicket existingTicket = ticketRepository.findById(simpleTransferData.idTicket).orElseThrow(
				() -> new IllegalArgumentException("Ticket not found with id: " + simpleTransferData.idTicket));

		User userToTransfer = userRepository.findByUsername(simpleTransferData.userToTransfer).orElseThrow(
				() -> new IllegalArgumentException("User not found with id: " + simpleTransferData.idTicket));

		existingTicket.setOwner(userToTransfer);

		return existingTicket;
	}

}
