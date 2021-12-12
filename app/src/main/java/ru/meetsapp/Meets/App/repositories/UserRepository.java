package ru.meetsapp.Meets.App.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.meetsapp.Meets.App.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String mail);

    Optional<User> findUserById(Long id);

    List<User> findAllByOrderByCreatedDateDesc();
}
