package fr.mary.berger.climbing.club.manager.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter @Setter
public class Outing {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Member owner;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String name;

    private String description;

    private String website;

    private Date date;
}
