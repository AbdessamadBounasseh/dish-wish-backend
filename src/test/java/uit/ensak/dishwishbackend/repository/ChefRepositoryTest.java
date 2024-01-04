package uit.ensak.dishwishbackend.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import uit.ensak.dishwishbackend.model.Chef;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ChefRepositoryTest {
    private final ChefRepository chefRepository;

    @Autowired
    public  ChefRepositoryTest (ChefRepository chefRepository) {
        this.chefRepository = chefRepository;
    }
    @Test
    public void ChefRepository_FindByFirstNameContaining_ReturnListOfChef(){
        Chef chef1 = new Chef();
        Chef chef2 = new Chef();
        chef1.setId(1L);
        chef2.setId(2L);
        chef1.setFirstName("abel");
        chef2.setFirstName("abella");

        chefRepository.save(chef1);
        chefRepository.save(chef2);


        List<Chef> returnChefs = chefRepository.findByFirstNameContaining("abel");
        System.out.println(returnChefs);

        Assertions.assertNotNull(returnChefs);
        Assertions.assertEquals(2, returnChefs.size());
    }

    @Test
    public void ChefRepository_FindByLastNameContaining_ReturnListOfChef(){
        Chef chef1 = new Chef();
        Chef chef2 = new Chef();
        chef1.setId(1L);
        chef2.setId(2L);
        chef1.setLastName("fay");
        chef2.setLastName("faycal");

        chefRepository.save(chef1);
        chefRepository.save(chef2);


        List<Chef> returnChefs = chefRepository.findByLastNameContaining("fay");
        System.out.println(returnChefs);

        Assertions.assertNotNull(returnChefs);
        Assertions.assertEquals(2, returnChefs.size());
    }

    @Test
    public void ChefRepository_FindByAddressContaining_ReturnListOfChef(){
        Chef chef1 = new Chef();
        Chef chef2 = new Chef();
        chef1.setId(1L);
        chef2.setId(2L);
        chef1.setAddress("Bir Rami Sud/ Kenitra/Maroc");
        chef2.setAddress("Centre Ville/ Kenitra/Maroc");

        chefRepository.save(chef1);
        chefRepository.save(chef2);

        List<Chef> returnChefs = chefRepository.findByAddressContaining("Keni");
        System.out.println(returnChefs);

        Assertions.assertNotNull(returnChefs);
        Assertions.assertEquals(2, returnChefs.size());
    }
}
