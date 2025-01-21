package br.ufrn.imd.cine.model.seat;

import jakarta.validation.constraints.NotNull;

public record SeatRecord(@NotNull SeatType seatType) {
}
