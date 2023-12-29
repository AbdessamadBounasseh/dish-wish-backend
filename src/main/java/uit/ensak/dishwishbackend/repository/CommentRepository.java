package uit.ensak.dishwishbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uit.ensak.dishwishbackend.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long > {
    List<Comment> findByReceiverId(Long id);
}
