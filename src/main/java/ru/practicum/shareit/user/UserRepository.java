package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findByEmail(String email);

    List<User> findByEmailIsAndIdNot(String email, int id);

    void deleteById(int id);
}
