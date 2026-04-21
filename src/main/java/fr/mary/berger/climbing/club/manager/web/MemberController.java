package fr.mary.berger.climbing.club.manager.web;

import fr.mary.berger.climbing.club.manager.models.Outing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private Outing outingService; // en attente d'implementation

    // Formulaire de création
    @GetMapping("/sortie/new")
    public String showCreateForm(Model model) {
        model.addAttribute("sortie", new Outing());
        return "form_sortie";
    }

    // Enregistrement (Création ou Modification)
    @PostMapping("/sortie/save")
    public String saveSortie(@ModelAttribute("sortie") Outing sortie,
                             BindingResult result,
                             Principal principal) { // --> Obj injecté par spring qui représente l'utilisateur connecté
        if (result.hasErrors()) {
            return "form_sortie";
        }

        String email = principal.getName();
        outingService.save(sortie, email); // utilisation méthode générique todo : adapter
        
        return "redirect:/public/categories";
    }

    // Suppression sécurisée
    @GetMapping("/sortie/delete/{id}")
    public String deleteSortie(@PathVariable Long id, Principal principal) {
        outingService.delete(id, principal.getName()); // utilisation méthode générique todo : adapter
        return "redirect:/public/categories";
    }
}