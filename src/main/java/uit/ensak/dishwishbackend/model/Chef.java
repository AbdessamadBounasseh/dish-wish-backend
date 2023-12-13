package uit.ensak.dishwishbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @OneToMany
            (mappedBy = "chef", cascade = CascadeType.ALL)
    private List<Rating> ratings;
}
