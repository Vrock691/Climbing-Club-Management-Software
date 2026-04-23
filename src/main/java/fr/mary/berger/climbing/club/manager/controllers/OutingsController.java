package fr.mary.berger.climbing.club.manager.controllers;

import fr.mary.berger.climbing.club.manager.models.Outing;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/outings")
@RequiredArgsConstructor
public class OutingsController {

    @GetMapping("/{id}")
    public String showOutingById(@PathVariable String id, Principal principal, Model model) {
        return "outing";
    }

    @GetMapping("/{id}/new")
    public String showCreateOutingForm(Model model, @PathVariable String id) {
        model.addAttribute("outing", new Outing());
        return "form_outing";
    }

    @PostMapping("/{id}/new")
    public String createOuting(@PathVariable String id, Principal principal, Model model) {
        return "outing";
    }

    @GetMapping("/{id}/update")
    public String showUpdateOutingForm(Model model, @PathVariable String id) {
        return "form_outing";
    }

    @PostMapping("/{id}/update")
    public String updateOuting(@PathVariable String id, Principal principal, Model model) {
        return "outing";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteOuting(@PathVariable String id, Principal principal, Model model) {
        return "outing";
    }

}
