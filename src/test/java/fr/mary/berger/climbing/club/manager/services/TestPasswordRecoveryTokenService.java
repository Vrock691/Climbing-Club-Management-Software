package fr.mary.berger.climbing.club.manager.services;

import fr.mary.berger.climbing.club.manager.dao.PasswordRecoveryTokenDAO;
import fr.mary.berger.climbing.club.manager.models.Member;
import fr.mary.berger.climbing.club.manager.models.PasswordRecoveryToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestPasswordRecoveryTokenService {

    @InjectMocks
    private PasswordRecoveryTokenService passwordRecoveryTokenService;

    @Mock
    private PasswordRecoveryTokenDAO passwordRecoveryTokenDAO;

    private Member member1;
    private Member member2;
    private PasswordRecoveryToken token1;
    private PasswordRecoveryToken token2;
    private PasswordRecoveryToken expired_token;

    @BeforeEach
    public void setUp() {
        member1 = new Member();
        member1.setUsername("member1");
        member2 = new Member();
        member2.setUsername("member2");
        token1 = new PasswordRecoveryToken(member1, "token1");
        token2 = new PasswordRecoveryToken(member2, "token2");
        expired_token = new PasswordRecoveryToken(member1, "expired_token");
        expired_token.setExpiryDate(new Date(System.currentTimeMillis() - 5000));

        when(passwordRecoveryTokenDAO.findByToken(anyString())).thenAnswer(iom -> {
            String token = iom.getArgument(0);
            return switch (token)  {
                case "token1" -> Optional.of(token1);
                case "token2" -> Optional.of(token2);
                case "expired_token" -> Optional.of(expired_token);
                default -> Optional.empty();
            };
        });
    }

    @Test
    public void testCheckPasswordResetToken() {
        assertTrue(passwordRecoveryTokenService.checkPasswordResetToken(member1, "token1"));
        assertFalse(passwordRecoveryTokenService.checkPasswordResetToken(member1, "invalid_token"));
        assertFalse(passwordRecoveryTokenService.checkPasswordResetToken(member2, "token1"));
        assertFalse(passwordRecoveryTokenService.checkPasswordResetToken(member1, "expired_token"));
    }
}
