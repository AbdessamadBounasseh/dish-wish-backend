package uit.ensak.dishwishbackend.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum City {
    CASABLANCA("Casablanca"),
    RABAT("Rabat"),
    TANGER("Tanger");

    @Getter
    private final String city;
}
