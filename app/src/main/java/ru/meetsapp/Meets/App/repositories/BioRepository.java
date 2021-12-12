package ru.meetsapp.Meets.App.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.meetsapp.Meets.App.entity.Bio;
import ru.meetsapp.Meets.App.entity.User;

import java.util.Optional;

@Repository
public interface BioRepository extends JpaRepository<Bio, Long> {
    Optional<Bio> findBioById(Long id);
    Optional<Bio> findBioByUser(User user);
}
