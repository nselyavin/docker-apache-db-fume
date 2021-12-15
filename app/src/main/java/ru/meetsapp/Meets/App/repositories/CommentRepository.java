package ru.meetsapp.Meets.App.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.meetsapp.Meets.App.entity.Comment;
import ru.meetsapp.Meets.App.entity.Meet;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findCommentsById(Long id);

    List<Comment> findAllByUsernameOrderByCreatedDate(String username);
    List<Comment> findAllByUserIdOrderByCreatedDate(Long id);
    List<Comment> findAllByMeetOrderByCreatedDate(Meet meet);


}
