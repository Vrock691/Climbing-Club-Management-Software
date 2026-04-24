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
        Optional<PasswordRecoveryToken> recordedToken = passwordRecoveryTokenDAO.findByToken(token);
        if (recordedToken.isPresent()) {
            PasswordRecoveryToken passwordRecoveryToken = recordedToken.get();
            if (!member.equals(passwordRecoveryToken.getMember())) return false;
            return passwordRecoveryToken.getToken().equals(token) && passwordRecoveryToken.getExpiryDate().after(new Date());
        }
        return false;
    }

    public void deletePasswordRecoveryToken(Member member, String token) {
        Optional<PasswordRecoveryToken> recordedToken = passwordRecoveryTokenDAO.findByToken(token);
        recordedToken.ifPresent(passwordRecoveryTokenDAO::delete);
    }

    public Optional<Member> findMemberByToken(String token) {
        Optional<PasswordRecoveryToken> passwordRecoveryToken = passwordRecoveryTokenDAO.findByToken(token);
        return passwordRecoveryToken.map(PasswordRecoveryToken::getMember);
    }

}
