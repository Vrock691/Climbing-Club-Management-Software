package fr.mary.berger.climbing.club.manager.controllers;

import fr.mary.berger.climbing.club.manager.models.Member;
import fr.mary.berger.climbing.club.manager.services.MemberService;
import fr.mary.berger.climbing.club.manager.services.EmailService;
import fr.mary.berger.climbing.club.manager.services.PasswordRecoveryTokenService;
import fr.mary.berger.climbing.club.manager.utils.UrlConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor()
public class AuthController {

    private final EmailService emailService;
    private final MemberService memberService;
    private final PasswordRecoveryTokenService passwordRecoveryTokenService;
    private final UrlConfig urlConfig;

    // TODO: Implémenter un password encoder
    // private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/forgot-password")
    public String showForgotForm() {
        return "forgotPassword";
    }

    @PostMapping("/forgot-password")
    public String traiterRecuperation(@RequestParam("email") String email, Model model) {
        Optional<Member> member = memberService.findMemberByEmail(email);
        if (member.isPresent()) {
            Member membre = member.get();

            // Génération d'un token de réinitialisation
            String token = UUID.randomUUID().toString();
            passwordRecoveryTokenService.createPasswordResetTokenForMember(membre, token);

            // Envoie du mail
            try {
                emailService.sendPasswordRecoveryEmail(urlConfig.getBaseUrl(), email, token);
                model.addAttribute("message", "Un e-mail contenant un lien a été envoyé si le compte existe.");
            } catch (Exception e) {
                model.addAttribute("error", "Erreur lors de l'envoi de l'e-mail.");
            }
        } else {
            model.addAttribute("message", "Un e-mail contenant un lien a été envoyé si le compte existe.");
        }

        return "forgotPassword";
    }

    @GetMapping("/change-password")
    public String showChangePasswordPage(Model model, Principal principal, @RequestParam("token") String token) {
        if (principal != null) {
            return "redirect:/";
        }

        if (token == null || token.isBlank()) {
            model.addAttribute("error", "Lien de réinitialisation invalide.");
            return "forgotPassword";
        }

        model.addAttribute("token", token);
        return "changePassword";
    }

    @PostMapping("/change-password")
    public String changePasswordWithToken(
            Principal principal,
            @RequestParam("token") String token,
            @RequestParam("password") String password)
    {
        if (token == null || token.isBlank()) {
            return "redirect:/auth/forgot-password";
        }

        // TODO: Implémenter une meilleure logique de gestion de mot de passe
        if (password == null || password.isBlank()) {
            return "redirect:/auth/forgot-password";
        }

        // TODO/ Implémenter le changement de mot de passe

        return "redirect:/auth/login";
    }
}