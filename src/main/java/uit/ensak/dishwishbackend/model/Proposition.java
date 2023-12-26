package uit.ensak.dishwishbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Proposition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float lastClientProposition;

    private float lastChefProposition;

    @ManyToOne
    @JoinColumn(name="client_id", nullable=false)
    private Client client;

    @ManyToOne
    @JoinColumn(name="chef_id", nullable=false)
    private Chef chef;

    @ManyToOne
    @JoinColumn(name="command_id",nullable=false)
    private Command command;
}
