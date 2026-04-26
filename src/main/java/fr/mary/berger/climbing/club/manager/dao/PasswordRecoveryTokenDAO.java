package fr.mary.berger.climbing.club.manager.dao;

import fr.mary.berger.climbing.club.manager.models.PasswordRecoveryToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordRecoveryTokenDAO extends JpaRepository<PasswordRecoveryToken, Long> {
    Optional<PasswordRecoveryToken> findByToken(String token);
}
