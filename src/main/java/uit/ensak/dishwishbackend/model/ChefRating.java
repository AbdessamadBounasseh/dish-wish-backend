package uit.ensak.dishwishbackend.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CHEF_RATING")
public class ChefRating extends Rating {

}