package ru.practicum.shareit.booking.dto;

import java.util.Optional;

public enum BookingState {
	/**
	 * Все бронирования
	 */
	ALL,
	/**
	 * Текущие бронирования (активные на данный момент)
	 */
	CURRENT,
	/**
	 * Будущие бронирования
	 */
	FUTURE,
	/**
	 * Завершенные бронирования
	 */
	PAST,
	/**
	 * Отклоненные бронирования
	 */
	REJECTED,
	/**
	 * Бронирования, ожидающие подтверждения
	 */
	WAITING;

	public static Optional<BookingState> from(String stringState) {
		for (BookingState state : values()) {
			if (state.name().equalsIgnoreCase(stringState)) {
				return Optional.of(state);
			}
		}
		return Optional.empty();
	}
}
