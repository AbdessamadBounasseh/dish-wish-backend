package uit.ensak.dishwishbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @CreationTimestamp(source = SourceType.DB)
    private Instant createdOn;


    @ManyToOne
    @JoinColumn(name="sender_id", nullable=false)
    private Client sender;

    @ManyToOne
    @JoinColumn(name="receiver_id", nullable=false)
    private Client receiver;
}
