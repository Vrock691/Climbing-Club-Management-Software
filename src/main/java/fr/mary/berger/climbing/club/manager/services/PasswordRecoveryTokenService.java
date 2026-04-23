package fr.mary.berger.climbing.club.manager.services;

import fr.mary.berger.climbing.club.manager.dao.PasswordRecoveryTokenDAO;
import fr.mary.berger.climbing.club.manager.models.Member;
import fr.mary.berger.climbing.club.manager.models.PasswordRecoveryToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordRecoveryTokenService {

    private final PasswordRecoveryTokenDAO passwordRecoveryTokenDAO;

    public void createPasswordResetTokenForMember(Member member, String token) {
        PasswordRecoveryToken passwordRecoveryToken = new PasswordRecoveryToken(member, token);
        passwordRecoveryTokenDAO.save(passwordRecoveryToken);
    }

    public boolean checkPasswordResetToken(Member member, String token) {
        Optional<PasswordRecoveryToken> recordedToken = passwordRecoveryTokenDAO.findPasswordRecoveryTokenByTokenAndMember(token, member);
        if (recordedToken.isPresent()) {
            PasswordRecoveryToken passwordRecoveryToken = recordedToken.get();
            if (passwordRecoveryToken.getToken().equals(token) && passwordRecoveryToken.getExpiryDate().after(new Date())) {
                return true;
            }
        }
        return false;
    }

    public void deletePasswordRecoveryToken(Member member, String token) {
        Optional<PasswordRecoveryToken> recordedToken = passwordRecoveryTokenDAO.findPasswordRecoveryTokenByTokenAndMember(token, member);
        recordedToken.ifPresent(passwordRecoveryTokenDAO::delete);
    }

}
