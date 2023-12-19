package uit.ensak.dishwishbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import uit.ensak.dishwishbackend.model.Chef;

@Getter
@Setter
public class CommandDTO {
    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("serving")
    private String serving;

    @JsonProperty("address")
    private String address;

    @JsonProperty("deadline")
    private String deadline;

    @JsonProperty("price")
    private String price;

    @JsonProperty("status")
    private String status;

    @JsonProperty("chef")
    private Chef chef;

}

