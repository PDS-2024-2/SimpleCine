package br.ufrn.imd.cine.services.layout;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.ufrn.imd.cine.model.layout.SimpleLayout;
import br.ufrn.imd.cine.model.seat.SeatRecord;
import br.ufrn.imd.cine.model.seat.SeatType;
import br.ufrn.imd.cine.model.seat.SimpleSeat;
import br.ufrn.imd.cine.repositories.SimpleLayoutRepository;
import br.ufrn.imd.cine.repositories.SimpleSeatRepository;
import br.ufrn.imd.cineframework.models.layout.AbstractLayout;
import br.ufrn.imd.cineframework.services.layout.LayoutCreator;
import jakarta.validation.constraints.NotNull;

@Component
public class SimpleLayoutCreator extends LayoutCreator {

	@Autowired
	private SimpleSeatRepository seatRepository;

	@Autowired
	private SimpleLayoutRepository layoutRepository;

	@Override
	public AbstractLayout createLayout(Object data) {

		SeatRecord[][] seatingLayout = (SeatRecord[][]) data;

		SimpleLayout simpleLayout = new SimpleLayout();

		simpleLayout.setColumns(seatingLayout[0].length);
		simpleLayout.setRows(seatingLayout.length);
		simpleLayout.setSeats(extractSeats(seatingLayout));
		simpleLayout.onCreate();

		SimpleLayout savedLayout = layoutRepository.save(simpleLayout);

		return savedLayout;
	}

	private List<SimpleSeat> extractSeats(@NotNull SeatRecord[][] seatingLayout) {

		List<SimpleSeat> seats = new ArrayList<>();

		for (int rowi = 1; rowi <= seatingLayout.length; rowi++) {
			SeatRecord[] seatRow = seatingLayout[rowi - 1];

			for (int columnj = 1; columnj <= seatRow.length; columnj++) {
				SeatType st = seatRow[columnj - 1].seatType();

				if (st == null)
					continue;

				SimpleSeat s = new SimpleSeat();
				s.setRow(rowi);
				s.setColumn(columnj);
				s.setSeatType(st);

				String code = numberToLetters(rowi) + columnj;

				s.setCode(code);
				s.onCreate();

				SimpleSeat savedSeat = seatRepository.save(s);
				seats.add(savedSeat);
			}
		}

		return seats;
	}

	private static String numberToLetters(int n) {
		StringBuilder sb = new StringBuilder();
		while (n > 0) {
			n--;
			char letter = (char) ('A' + (n % 26));
			sb.append(letter);
			n /= 26;
		}
		return sb.reverse().toString();
	}

}
