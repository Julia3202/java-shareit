package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DateStatus {
    ALL("all"),
    CURRENT("current"),
    PAST("past"),
    FUTURE("future"),
    WAITING("waiting"),
    REJECTED("rejected");

    private final String nameStatus;
}
