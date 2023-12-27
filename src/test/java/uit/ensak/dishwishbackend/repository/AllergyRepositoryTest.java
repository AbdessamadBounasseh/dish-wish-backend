package uit.ensak.dishwishbackend.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import uit.ensak.dishwishbackend.model.Allergy;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AllergyRepositoryTest {

    private final AllergyRepository allergyRepository;

    @Autowired
    public AllergyRepositoryTest(AllergyRepository allergyRepository) {
        this.allergyRepository = allergyRepository;
    }

    @Test
    public void AllergyRepository_FindByTitle_ReturnOptionalAllergy(){
        Allergy allergy = Allergy.builder().id(1L).title("Oeuf").build();
        allergyRepository.save(allergy);

        Allergy returnAllergy = allergyRepository.findByTitle("Oeuf").orElseThrow();

        Assertions.assertNotNull(returnAllergy);
        Assertions.assertEquals(allergy.getId(),returnAllergy.getId());
        Assertions.assertEquals(allergy.getTitle(),returnAllergy.getTitle());
    }
}
