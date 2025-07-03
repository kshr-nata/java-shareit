package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findByUserId(int userId);

   @Query("select item from Item as it "+
           "where it.is_available = true " +
           "AND (upper(it.name) like upper(concat('%', ?1, '%')) " +
           "OR upper(it.description) like upper(concat('%', ?1, '%')))")
    List<Item> searchItems(String textPart);

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