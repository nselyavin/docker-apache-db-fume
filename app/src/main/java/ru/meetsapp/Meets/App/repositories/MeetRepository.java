package ru.meetsapp.Meets.App.repositories;

import org.hibernate.annotations.Where;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.meetsapp.Meets.App.entity.Meet;
import ru.meetsapp.Meets.App.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeetRepository extends JpaRepository<Meet, Long> {
    Optional<Meet> findMeetById(Long id);
    List<Meet> findAllByMeetUsersContainingOrderByCreatedDateDesc(String username);
    List<Meet> findAllByMeetUsersLikeOrderByCreatedDateDesc(String username);
    List<Meet> findAllByCreatorOrderByCreatedDateDesc(User user);
    List<Meet> findAllByOpenOrderByCreatedDateDesc(int open);
    List<Meet> findAllByOrderByCreatedDateDesc();

    void deleteMeetById(Long id);
}

