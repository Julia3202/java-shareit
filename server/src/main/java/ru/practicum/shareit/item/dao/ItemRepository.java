package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwnerId(long userId);

    @Query("select i " +
            "from Item as i " +
            "where i.available = true and (upper(i.name) " +
            " like upper(concat('%', ?1, '%')) or upper(i.description) " +
            " like upper(concat('%', ?1, '%'))) ")
    List<Item> findByNameOrDescription(String text);

    void deleteByOwnerIdAndId(long userId, long id);

    Item findByRequestIdOrderById(long requestId);

    List<Item> findAllByRequestIdInOrderById(List<Long> requestIdList);

    List<Item> findAllByRequestRequestorIdNotAndRequestIdInOrderById(long userId, List<Long> requestIdList);

    List<Item> findAllByOwnerIdOrderById(long userId);
}
