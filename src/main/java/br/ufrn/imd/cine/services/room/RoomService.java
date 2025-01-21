package br.ufrn.imd.cine.services.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufrn.imd.cine.model.layout.SimpleLayout;
import br.ufrn.imd.cine.model.room.CreateRoomRecord;
import br.ufrn.imd.cine.services.layout.SimpleLayoutService;
import br.ufrn.imd.cineframework.models.room.Room;
import br.ufrn.imd.cineframework.repositories.GenericRepository;
import br.ufrn.imd.cineframework.repositories.room.RoomRepository;
import br.ufrn.imd.cineframework.services.GenericService;
import jakarta.validation.Valid;

@Service
public class RoomService extends GenericService<Room> {
	private static final long serialVersionUID = 1L;

	@Autowired
	private RoomRepository repository;

	@Autowired
	private SimpleLayoutService layoutService;

	@Override
	public GenericRepository<Room> getRepository() {
		return repository;
	}

	public Room createRoom(@Valid CreateRoomRecord roomRecord) {
		Room r = new Room();
		r.setCode(roomRecord.code());
		r.setRoomType(roomRecord.type());

		SimpleLayout layout = layoutService.createLayout(roomRecord.seatingLayout());

		r.setCapacity(layout.getSeats().size());
		r.setLayout(layout);

		return insert(r);
	}

}
