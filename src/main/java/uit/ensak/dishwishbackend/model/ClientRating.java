package uit.ensak.dishwishbackend.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CLIENT_RATING")
public class ClientRating extends Rating {

}