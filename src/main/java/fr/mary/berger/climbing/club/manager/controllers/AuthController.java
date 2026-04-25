package fr.mary.berger.climbing.club.manager.controllers;

import fr.mary.berger.climbing.club.manager.models.Member;
import fr.mary.berger.climbing.club.manager.services.MemberService;
import fr.mary.berger.climbing.club.manager.services.EmailService;
import fr.mary.berger.climbing.club.manager.services.PasswordRecoveryTokenService;
import fr.mary.berger.climbing.club.manager.configurations.UrlConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor()
public class AuthController {

    private final EmailService emailService;
    private final MemberService memberService;
    private final PasswordRecoveryTokenService passwordRecoveryTokenService;
    private final UrlConfig urlConfig;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "loginScreen";
    }

    @GetMapping("/forgot-password")
    public String showForgotForm() {
        return "forgotPasswordScreen";
    }

    @PostMapping("/forgot-password")
    public String requestRecovery(@RequestParam("email") String email, Model model) {
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

        return "forgotPasswordScreen";
    }

    @GetMapping("/change-password")
    public String showChangePasswordPage(Model model, Principal principal, @RequestParam("token") String token) {
        if (principal != null) {
            return "redirect:/";
        }

        if (token == null || token.isBlank()) {
            model.addAttribute("error", "Lien de réinitialisation invalide.");
            return "forgotPasswordScreen";
        }

        model.addAttribute("token", token);
        return "changePasswordScreen";
    }

    @PostMapping(value = "/change-password", consumes = "application/x-www-form-urlencoded")
    public String changePasswordWithToken(
            @RequestParam("token") String token,
            @RequestParam("password") String password,
            Model model) {

        if (token == null || token.isBlank()) {
            model.addAttribute("error", "Lien de réinitialisation invalide.");
            return "forgotPasswordScreen";
        }

        if (password == null || password.isBlank()) {
            model.addAttribute("error", "Le mot de passe ne peut pas être vide.");
            model.addAttribute("token", token);
            return "changePasswordScreen";
        }

        // Chercher le member associé au token
        Optional<Member> member = passwordRecoveryTokenService.findMemberByToken(token);
        if (member.isEmpty()) {
            model.addAttribute("error", "Lien de réinitialisation invalide ou expiré.");
            return "forgotPasswordScreen";
        }
        Member memberFound = member.get();

        // Vérifier et mettre à jour en conséquence
        if (!passwordRecoveryTokenService.checkPasswordResetToken(memberFound, token)) {
            model.addAttribute("error", "Lien de réinitialisation expiré.");
            return "forgotPasswordScreen";
        }
        String encodedPassword = passwordEncoder.encode(password);
        memberService.changePassword(memberFound, encodedPassword);
        passwordRecoveryTokenService.deletePasswordRecoveryToken(memberFound, token);

        model.addAttribute("message", "Mot de passe modifié");
        return "redirect:/auth/login";
    }
}