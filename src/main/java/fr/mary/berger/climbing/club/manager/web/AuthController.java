package fr.mary.berger.climbing.club.manager.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/forgot-password")
    public String showForgotForm() {
        return "password_recovery";
    }

    @PostMapping("/forgot-password")
    public String processForgot(@RequestParam String email, Model model) {
        try {
            model.addAttribute("message", "Un mail a été envoyé si le compte existe.");
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de l'envoi.");
        }
        return "password_recovery";
    }
}