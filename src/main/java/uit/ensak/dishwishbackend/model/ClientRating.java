package uit.ensak.dishwishbackend.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@DiscriminatorValue("CLIENT_RATING")
public class ClientRating extends Rating {

}