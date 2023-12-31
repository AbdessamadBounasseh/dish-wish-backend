package uit.ensak.dishwishbackend.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import uit.ensak.dishwishbackend.model.*;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RatingRepositoryTest {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ChefRepository chefRepository;

    @Test
    public void shouldFindByClientAndType() {
        Client client = Client.builder()
                            .id(1L).firstName("meryem").lastName("elhassouni")
                            .build();
        clientRepository.save(client);

        Chef chef = new Chef();
        chef.setId(2L);
        chef.setFirstName("johnny");
        chef.setLastName("depp");
        chefRepository.save(chef);

        ClientRating rating1 = new ClientRating();
        rating1.setClient(client);
        rating1.setChef(chef);
        rating1.setRating(5.0);

        ClientRating rating2 = new ClientRating();
        rating2.setClient(client);
        rating2.setChef(chef);
        rating2.setRating(3.0);

        ChefRating rating3 = new ChefRating();
        rating3.setClient(client);
        rating3.setChef(chef);
        rating3.setRating(2.0);

        ratingRepository.saveAll(List.of(rating1, rating2, rating3));

        List<Rating> result = ratingRepository.findByClientAndType(client, "CLIENT_RATING");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
    }

    @Test
    public void shouldFindByChefAndType() {
        Client client = Client.builder()
                .id(4L).firstName("jane").lastName("doe")
                .build();
        clientRepository.save(client);

        Chef chef = new Chef();
        chef.setId(2L);
        chef.setFirstName("john");
        chef.setLastName("doe");
        chefRepository.save(chef);

        ChefRating rating1 = new ChefRating();
        rating1.setClient(client);
        rating1.setChef(chef);
        rating1.setRating(5.0);

        ChefRating rating2 = new ChefRating();
        rating2.setClient(client);
        rating2.setChef(chef);
        rating2.setRating(3.0);

        ClientRating rating3 = new ClientRating();
        rating3.setClient(client);
        rating3.setChef(chef);
        rating3.setRating(2.0);

        ratingRepository.saveAll(List.of(rating1, rating2, rating3));

        List<Rating> result = ratingRepository.findByClientAndType(client, "CHEF_RATING");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());

    }
}