package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Enumerated;

@Getter
@AllArgsConstructor
public enum Status {
    WAITING("Новое бронирование. Ожидает подтверждения."),
    APPROVED("Бронирование подтверждено."),
    REJECTED("Бронирование отклонено владельцем."),
    CANCELED("Бронирование отменено создателем.");

    private String nameStatus;
}
