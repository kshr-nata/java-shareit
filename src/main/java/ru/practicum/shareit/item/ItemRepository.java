package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    @Query("SELECT it FROM Item it " +
            "WHERE it.available = true " +
            "AND ((UPPER(it.name) LIKE UPPER(CONCAT('%', :textPart, '%')) " +
            "OR UPPER(it.description) LIKE UPPER(CONCAT('%', :textPart, '%'))))")
    List<Item> searchItems(@Param("textPart") String textPart);

    @Query("SELECT it " +
            "FROM Item it " +
            "LEFT JOIN FETCH it.comments " +
            "WHERE it.owner.id = :ownerId")
    List<Item> findAllWithCommentsByOwner(@Param("ownerId") Integer ownerId);

    @Query("SELECT it " +
            "FROM Item it " +
            "LEFT JOIN FETCH it.comments " +
            "WHERE it.id = :itemId")  // Добавляем условие для поиска по ID
    Optional<Item> findOneWithComments(@Param("itemId") Integer itemId);

}