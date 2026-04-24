package fr.mary.berger.climbing.club.manager.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendPasswordRecoveryEmail(String contextPath, String recipient, String token) {
        String url = contextPath + "/auth/change-password?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ne-pas-repondre@club-escalade.fr");
        message.setTo(recipient);
        message.setSubject("Réinitialisation de votre mot de passe - Club Escalade");
        message.setText("Bonjour,\n\n"
                + "Vous avez demandé la réinitialisation de votre mot de passe.\n"
                + "Veuillez cliquer sur ce lien pour le changer : " + url + "\n"
                + "Sportivement,\nL'équipe du Club.");
        mailSender.send(message);
        log.info("Email sent to " + recipient + ", with link : " + url);
    }
}