package uit.ensak.dishwishbackend.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RatingDTO {
    private Long chefId;
    private Long clientId;
    private double rating;
}
