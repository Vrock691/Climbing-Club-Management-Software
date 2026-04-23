package fr.mary.berger.climbing.club.manager.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void envoyerMailRecuperation(String destinataire, String nouveauMotDePasse) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("ne-pas-repondre@club-escalade.fr");
        message.setTo(destinataire);
        message.setSubject("Réinitialisation de votre mot de passe - Club Escalade");
        message.setText("Bonjour,\n\n"
                + "Vous avez demandé la réinitialisation de votre mot de passe.\n"
                + "Votre nouveau mot de passe temporaire est : " + nouveauMotDePasse + "\n\n"
                + "Nous vous conseillons de le modifier dès votre prochaine connexion.\n"
                + "Sportivement,\nL'équipe du Club.");

        mailSender.send(message);
    }
}