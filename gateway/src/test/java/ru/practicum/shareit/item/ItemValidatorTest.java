package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.ItemDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemValidatorTest {
    private final ItemValidator itemValidator = new ItemValidator();
    private ItemDto itemDto;

    @BeforeEach
    void beforeEach() {
        itemDto = new ItemDto(1, "itemName", "itemDescription", true, 1L);
    }

    @Test
    void validName() {
        Boolean count = itemValidator.validName(itemDto);
        assertEquals(true, count);
        itemDto.setName("");
        try {
            itemValidator.validName(itemDto);
        } catch (ValidationException exception) {
            assertEquals("Заполните поле с названием.", exception.getMessage());
        }
    }

    @Test
    void validDescription() {
        Boolean count = itemValidator.validDescription(itemDto);
        assertEquals(true, count);
        itemDto.setDescription("");
        try {
            itemValidator.validDescription(itemDto);
        } catch (ValidationException exception) {
            assertEquals("Заполните поле - описание.", exception.getMessage());
        }
    }

    @Test
    void validAvailable() {
        Boolean count = itemValidator.validAvailable(itemDto);
        assertEquals(true, count);
        itemDto.setAvailable(null);
        try {
            itemValidator.validAvailable(itemDto);
        } catch (ValidationException exception) {
            assertEquals("Не заполнено поле со статусом о доступности вещи.", exception.getMessage());
        }
    }

    @Test
    void validItem() {
        Boolean count = itemValidator.validItem(itemDto);
        assertEquals(true, count);
        itemDto.setName("");
        try {
            itemValidator.validItem(itemDto);
        } catch (ValidationException exception) {
            assertEquals("Заполните поле с названием.", exception.getMessage());
        }
    }
}