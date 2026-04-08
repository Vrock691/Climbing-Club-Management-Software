package fr.mary.berger.climbing.club.manager.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
public class Member {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;
}
