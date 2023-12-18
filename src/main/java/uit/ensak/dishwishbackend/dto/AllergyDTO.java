package uit.ensak.dishwishbackend.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllergyDTO {
    private Long id;
    private String title;
}
