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
@RequestMapping("/outings")
@RequiredArgsConstructor
public class OutingsController {

    private final OutingService outingService;

    // TODO: Utiliser un DTO pour passer la sortie, certaines informations ne doivent pas être transmise comme le site internet et l'organisateur
    // TODO: Implémenter une vérification d'identité
    @GetMapping("/{id}")
    public String showOutingById(@PathVariable String id, Principal principal, Model model) {
        model.addAttribute("sortie", outingService.findOutingById(Long.valueOf(id)));
        return "outingDetailsScreen";
    }

    @GetMapping("/new")
    public String showCreateOutingForm(Model model) {
        return "form_outing";
    }

    // TODO: Créer deux DTO pour la requete et la réponse et créer l'objet Outing localement, puis rediriger avec un message de succès ou d'erreur
    @PostMapping("/new")
    public String createOuting(@ModelAttribute("sortie") Outing outing,
                               BindingResult result,
                               Principal principal) { // --> Obj injecté par spring qui représente l'utilisateur connecté
        if (result.hasErrors()) {
            return "form_outing";
        }

        String email = principal.getName();
        outingService.createOuting(outing);

        return "redirect:/public/categories";
    }

    // TODO: faire la page jsp de modification (qui peut etre la même que celle de proposition mais en passant par un DTO)
    @GetMapping("/{id}/update")
    public String showUpdateOutingForm(Model model, @PathVariable String id) {
        return "form_outing";
    }

    // TODO: implémenter
    @PostMapping("/{id}/update")
    public String updateOuting(@PathVariable String id, Principal principal, Model model) {
        return "outing";
    }

    // TODO: Faire la vérification d'identité au sein du controller, renvoyer un message de succès ou d'erreur, vérifier aussi que la sortie existe
    @DeleteMapping("/{id}/delete")
    public String deleteOuting(@PathVariable String id, Principal principal, Model model) {
        outingService.deleteOuting(Long.valueOf(id));
        return "redirect:/public/categories";
    }

}
