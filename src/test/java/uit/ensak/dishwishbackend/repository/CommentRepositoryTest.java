package uit.ensak.dishwishbackend.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import uit.ensak.dishwishbackend.model.Chef;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.model.Comment;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CommentRepositoryTest {
    private final CommentRepository commentRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public CommentRepositoryTest (CommentRepository commentRepository, ClientRepository clientRepository) {
        this.commentRepository = commentRepository;
        this.clientRepository = clientRepository;
    }

    @Test
    public void CommentRepository_FindByReceiverId_ReturnListOfComments(){
        Long receiver_id = 2L;
        Client client = Client.builder().id(1L).build();
        Chef chef = new Chef();
        chef.setId(2L);
        Comment comment1 = Comment.builder().id(1L).sender(client).receiver(chef).build();
        Comment comment2 = Comment.builder().id(2L).sender(client).receiver(chef).build();

        clientRepository.save(client);
        clientRepository.save(chef);
        commentRepository.save(comment1);
        commentRepository.save(comment2);

        List<Comment> returnComments = commentRepository.findByReceiverId(receiver_id);

        Assertions.assertNotNull(returnComments);
        Assertions.assertEquals(2, returnComments.size());
    }
}
