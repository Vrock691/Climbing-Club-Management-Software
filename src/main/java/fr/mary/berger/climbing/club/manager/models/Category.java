package fr.mary.berger.climbing.club.manager.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@RequiredArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String name;
}
