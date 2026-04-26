package fr.mary.berger.climbing.club.manager.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
public class Member {
    @Id
    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String encodedPassword;
}
