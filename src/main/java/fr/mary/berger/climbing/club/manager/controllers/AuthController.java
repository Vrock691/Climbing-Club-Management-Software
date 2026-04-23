package fr.mary.berger.climbing.club.manager.controllers;

import fr.mary.berger.climbing.club.manager.models.Member;
import fr.mary.berger.climbing.club.manager.services.MemberService;
import fr.mary.berger.climbing.club.manager.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor()
public class AuthController {

    private final EmailService emailService;
    private final MemberService memberService;
   // private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/forgot-password")
    public String showForgotForm() {
        return "passwordRecoveryScreen";
    }

    @PostMapping("/forgot-password")
    public String processForgot(@RequestParam String email, Model model) {
        try {
            model.addAttribute("message", "Un mail a été envoyé si le compte existe.");
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de l'envoi.");
        }
        return "passwordRecoveryScreen";
    }

    // TODO: Renommer la route
    @PostMapping("/forgot-password2")
    public String traiterRecuperation(@RequestParam("email") String email, Model model) {
        Optional<Member> membreOpt = memberService.findMemberByEmail(email);  // utilisation méthode générique todo : adapter

        if (membreOpt.isPresent()) {
            Member membre = membreOpt.get();

            // Génération mdp temporaire
            String mdpTemporaire = "Escalade-" + (int)(Math.random() * 10000);

            // Mise à jour du membre sur la bdd
         //   membre.setPassword(passwordEncoder.encode(mdpTemporaire));
         //   membreService.sauvegarder(membre);                              // utilisation méthode générique todo : adapter

            // Envoie du mail
            try {
                emailService.envoyerMailRecuperation(email, mdpTemporaire);
                model.addAttribute("success", "Un e-mail contenant votre nouveau mot de passe a été envoyé.");
            } catch (Exception e) {
                model.addAttribute("error", "Erreur lors de l'envoi de l'e-mail.");
            }
        } else {
            model.addAttribute("error", "Aucun compte n'existe avec cette adresse e-mail.");
        }

        return "passwordRecoveryScreen";
    }
}