package uit.ensak.dishwishbackend.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import uit.ensak.dishwishbackend.model.Diet;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class DietRepositoryTest {
    private final DietRepository dietRepository;

    @Autowired
    public DietRepositoryTest (DietRepository dietRepository) {
        this.dietRepository = dietRepository;
    }

    @Test
    public void DietRepository_FindByTitle_ReturnOptionalDiet(){
        Diet diet = Diet.builder().id(1L).title("Oeuf").build();
        dietRepository.save(diet);

        Diet returnDiet = dietRepository.findByTitle("Oeuf").orElseThrow();
        Assertions.assertNotNull(returnDiet);
        Assertions.assertEquals(diet.getId(),returnDiet.getId());
        Assertions.assertEquals(diet.getTitle(),returnDiet.getTitle());
    }
}
