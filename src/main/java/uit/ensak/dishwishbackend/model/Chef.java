package uit.ensak.dishwishbackend.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("CHEF")
public class Chef extends Client {
    private String bio;

    private String idCard;

    private String certificate;

    @OneToMany(mappedBy = "chef", cascade = CascadeType.ALL)
    private List<Rating> ratings;
}
