package br.ufrn.imd.cine.model.room;

import br.ufrn.imd.cine.model.seat.SeatRecord;
import br.ufrn.imd.cineframework.models.room.RoomType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateRoomRecord(
		@NotNull
		@NotBlank
		String code,
		@NotNull
		RoomType type,
		@Min(1)
		@Max(50)
		Integer rows,
		@Min(1)
		@Max(50)
		Integer columns,
		@NotNull
		SeatRecord[][] seatingLayout
		) {
}