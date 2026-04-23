package fr.mary.berger.climbing.club.manager.controllers;

import fr.mary.berger.climbing.club.manager.models.Outing;
import fr.mary.berger.climbing.club.manager.services.OutingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/myprofile")
@RequiredArgsConstructor
public class MemberController {

    private final OutingService outingService;

    @GetMapping("/outings/new")
    public String showCreateForm(Model model) {
        model.addAttribute("sortie", new Outing()); // Pourquoi tu transmets un object vide ?
        return "form_sortie";
    }

    @PostMapping("/outings/new")
    public String saveSortie(@ModelAttribute("sortie") Outing sortie,
                             BindingResult result,
                             Principal principal) { // --> Obj injecté par spring qui représente l'utilisateur connecté
        if (result.hasErrors()) {
            return "form_sortie";
        }

        String email = principal.getName();
       // outingService.save(sortie, email); // utilisation méthode générique todo : adapter
        
        return "redirect:/public/categories";
    }

    // Suppression sécurisée
    @GetMapping("/outings/delete/{id}")
    public String deleteSortie(@PathVariable Long id, Principal principal) {
       // outingService.delete(id, principal.getName()); // utilisation méthode générique todo : adapter
        return "redirect:/public/categories";
    }
}