package fr.mary.berger.climbing.club.manager.controllers;

import fr.mary.berger.climbing.club.manager.dto.outings.OutingRequestDTO;
import fr.mary.berger.climbing.club.manager.dto.outings.OutingResponseDTO;
import fr.mary.berger.climbing.club.manager.dto.outings.OutingDTO;
import fr.mary.berger.climbing.club.manager.models.Outing;
import fr.mary.berger.climbing.club.manager.services.OutingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

// TODO: Utiliser ModelAndView si nécéssaire dans les retour des modèles plutot que de simple string/attributs

@Controller
@RequestMapping("/outings")
@RequiredArgsConstructor
public class OutingsController {

    private final OutingService outingService;

    // TODO: Utiliser le nouveau DTO PaginatedRessource dans OutingResponseDTO, voir todo dans outingResponseDTO
    // TODO: Modifier les anciens DTO pour séparer les responsabilités et supprimer les données/méthodes non essentielles
    @GetMapping("/{id}")
    public String showOutingById(@PathVariable Long id, Principal principal, Model model) {
        Outing outing = outingService.findOutingById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sortie introuvable"));

        OutingResponseDTO dto = new OutingResponseDTO();
        dto.setId(outing.getId());
        dto.setName(outing.getName());
        dto.setDescription(outing.getDescription());
        dto.setDateOuting(outing.getDate());

        // Verification d'identité
        if (principal != null) { // Membre
            dto.setWebSite(outing.getWebsite());
            dto.setOwnerName(outing.getOwner().getFirstName() + " " + outing.getOwner().getLastName());
        } else { // Visiteur
            dto.setWebSite("Connectez-vous pour voir le site");
            dto.setOwnerName("Masqué pour les non-membres");
        }

        model.addAttribute("sortie", dto);
        return "outingDetailsScreen";
    }

    @GetMapping("/new")
    public String showCreateOutingForm(Model model) {
        return "newOutingFormScreen";
    }

    // TODO: Ne pas utiliser Outing en attribut "sortie" (à passer en anglais d'ailleurs), créer un DTO de création d'outing, avec seulement les informations connues
    // (t'as pas l'owner depuis la page web par exemple) ex: OutingCreationRequestDTO
    @PostMapping("/new")
    public String createOuting(@ModelAttribute("sortie") Outing outingDto,
                               BindingResult result,
                               Principal principal,
                               RedirectAttributes redirectAttributes) // À voir si on ne peut pas réduire le nombre de params ici
    {
        if (result.hasErrors()) {
            return "newOutingFormScreen";
        }

        try {
            // TODO: mieux gérer la création, à voir si on peut pas rajouter des validateurs, mais c'est pas la prio
            Outing outing = new Outing();
            outing.setName(outingDto.getName());
            outing.setDescription(outingDto.getDescription());
            outing.setDate(outingDto.getDateOuting()); // ??
            outing.setWebsite(outingDto.getWebsite());
            outingService.createOuting(outing);

            // TODO: Vérifier si ça vaudrait le coup de passer ça en ModelAndView, mais pas sur
            redirectAttributes.addFlashAttribute("success", "Votre sortie a été créée avec succès !");
            return "redirect:/categories";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la création : " + e.getMessage());
            return "redirect:/outings/new";
        }
    }

    // TODO: faire la page jsp de modification (qui peut etre la même que celle de proposition mais en passant par un DTO)
    @GetMapping("/{id}/update")
    public String showUpdateOutingForm(@PathVariable Long id, Principal principal, Model model) {

        Outing outing = outingService.findOutingById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sortie introuvable"));
        // Vérification id
        if (!outing.getOwner().getEmail().equals(principal.getName())) { // Ça marche avec name du coup ??
            return "redirect:/outings/" + id + "?error=unauthorized";
        }

        // TODO: améliorer les noms des variables, et changer le DTO pour OutingDTO -> plus simple et évite la redondance, tu n'as qu'un seul resultat
        // (à moins que tu veuilles transmettre une erreur

        OutingRequestDTO dto = new OutingRequestDTO();
        dto.setName(outing.getName());
        dto.setDescription(outing.getDescription());
        dto.setDateOuting(outing.getDate());
        dto.setWebSite(outing.getWebsite());
        dto.setIdCategory(outing.getCategory().getId());

        // ModelAndView serait bien ici par exemple plutôt que de l'attribut/string
        model.addAttribute("sortie", dto);
        model.addAttribute("outingId", id);

        return "form_outing";
    }

    // TODO: implémenter la mise à jour
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
