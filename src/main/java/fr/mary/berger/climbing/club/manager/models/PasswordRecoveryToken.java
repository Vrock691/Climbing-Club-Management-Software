package fr.mary.berger.climbing.club.manager.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
public class PasswordRecoveryToken {
    private static final int EXPIRATION = 60 * 24;

    public PasswordRecoveryToken(
            Member member,
            String token
    ) {
        this.member = member;
        this.token = token;
        this.expiryDate = new Date(System.currentTimeMillis() + EXPIRATION);
    }

    @Id
    private Long id;

    private String token;

    @OneToOne(targetEntity = Member.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "member_id")
    private Member member;

    private Date expiryDate;
}
