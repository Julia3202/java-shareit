package ru.practicum.shareit.booking.model;

import java.util.Optional;

public enum DateStatus {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static Optional<DateStatus> stateFrom(String state) {
        for (DateStatus status : values()) {
            if (status.equals(DateStatus.valueOf(state))) {
                return Optional.of(status);
            }
        }
        return Optional.empty();
    }
}
