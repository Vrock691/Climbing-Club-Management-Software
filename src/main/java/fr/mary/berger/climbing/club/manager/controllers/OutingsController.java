package fr.mary.berger.climbing.club.manager.controllers;

import fr.mary.berger.climbing.club.manager.dto.outings.OutingRequestDTO;
import fr.mary.berger.climbing.club.manager.dto.outings.OutingResponseDTO;
import fr.mary.berger.climbing.club.manager.dto.outings.OutingDTO;
import fr.mary.berger.climbing.club.manager.models.Outing;
import fr.mary.berger.climbing.club.manager.services.OutingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
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
    public ModelAndView showOutingById(@PathVariable Long id, Principal principal, Model model) {
        Outing outing = outingService.findOutingById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sortie introuvable"));

        OutingResponseDTO showingByIdDto = new OutingResponseDTO();
        showingByIdDto.setId(outing.getId());
        showingByIdDto.setName(outing.getName());
        showingByIdDto.setDescription(outing.getDescription());
        showingByIdDto.setDateOuting(outing.getDate());

        // Verification d'identité
        if (principal != null) { // Membre
            showingByIdDto.setWebSite(outing.getWebsite());
            showingByIdDto.setOwnerName(outing.getOwner().getFirstName() + " " + outing.getOwner().getLastName());
        } else { // Visiteur
            showingByIdDto.setWebSite("Connectez-vous pour voir le site");
            showingByIdDto.setOwnerName("Masqué pour les non-membres");
        }

        model.addAttribute("sortie", showingByIdDto);
        return new ModelAndView("outingDetailsScreen", "sortie", showingByIdDto);
    }

    @GetMapping("/new")
    public String showCreateOutingForm(Model model) {

        /*
        TODO: T'as codé ça, mais je sais pas si c'est utile vu qu'on renvoi un html avec tout les champs vide pour la création, à moins que des infos doivent apparaitre sur l'écran
        (À toi de voir si ça vaut le coup de mettre ça, sinon un renvoi en string simple suffit, pas besoin de DTO)

        ModelAndView localMaV = new ModelAndView("form_outing");
        localMaV.addObject("sortie", new OutingRequestDTO());

        Pageable allStyles = (Pageable) PageRequest.of(0, Integer.MAX_VALUE);
        localMaV.addObject("categories", categoryService.getAllCategories((org.springframework.data.domain.Pageable) allStyles).getContent());
        return localMaV;
         */

        return "newOutingFormScreen";
    }

    // TODO: À voir si réutiliser OutingRequestDTO est judidicieux, ou s'il faut en utiliser/créer un spécifique
    @PostMapping("/new")
    public ModelAndView createOuting(@ModelAttribute("sortie") OutingRequestDTO outingDto,
                                     BindingResult result,
                                     Principal principal,
                                     RedirectAttributes redirectAttributes) { // À voir si on ne peut pas réduire le nombre de params ici
        if (result.hasErrors()) {
            // TODO: passer l'erreur -> Changer/créer le dto pour prévoir le handle
            return new ModelAndView("form_outing", "sortie", outingDto);
        }

        try {
            // TODO: mieux gérer la création, à voir si on peut pas rajouter des validateurs, mais c'est pas la prio
            Outing creationOuting = new Outing();
            creationOuting.setName(outingDto.getName());
            creationOuting.setDescription(outingDto.getDescription());
            creationOuting.setDate(outingDto.getDateOuting());
            creationOuting.setWebsite(outingDto.getWebSite());
            outingService.createOuting(creationOuting);

            // TODO: Vérifier si ça vaudrait le coup de passer ça en ModelAndView, mais pas sur
            redirectAttributes.addFlashAttribute("success", "Votre sortie a été créée avec succès !");
            return new ModelAndView("redirect:/categories");

        } catch (Exception e) {
            // TODO: Complexe: peut-etre que le dto peut contenir cette erreur est celle d'au dessus
            ModelAndView localMaV = new ModelAndView("form_outing", "sortie", outingDto);
            localMaV.addObject("error", "Une erreur est survenue : " + e.getMessage());
            return localMaV;
        }
    }

    // TODO: faire la page jsp de modification (qui peut etre la même que celle de proposition mais en passant par un DTO)
    @GetMapping("/{id}/update")
    public ModelAndView showUpdateOutingForm(@PathVariable Long id, Principal principal, Model model) {

        // TODO: Utiliser l'utilitaire OutingModificationRightChecker dans security
        Outing outing = outingService.findOutingById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sortie introuvable"));
        // Vérification id
        if (!outing.getOwner().getEmail().equals(principal.getName())) { // Ça marche avec name du coup ??
            return new ModelAndView("redirect:/outings/" + id + "?error=unauthorized");
        }

        // TODO: améliorer les noms des variables, et changer le DTO pour OutingDTO -> plus simple et évite la redondance, tu n'as qu'un seul resultat
        // (à moins que tu veuilles transmettre une erreur
        OutingRequestDTO updateDto = new OutingRequestDTO();
        updateDto.setName(outing.getName());
        updateDto.setDescription(outing.getDescription());
        updateDto.setDateOuting(outing.getDate());
        updateDto.setWebSite(outing.getWebsite());
        updateDto.setIdCategory(outing.getCategory().getId());

        // Répétition, l'id est déjà dans updateDto
        ModelAndView localMaV = new ModelAndView();
        localMaV.addObject("sortie", updateDto);
        localMaV.addObject("outingId", id);
        return localMaV;
    }

    // TODO: Simplifier, tu as trois retour d'erreur avec trois syntaxes différentes
    @PostMapping("/{id}/update")
    public ModelAndView updateOuting(@PathVariable Long id,
                                     @ModelAttribute("sortie") OutingRequestDTO updateDto,
                                     BindingResult result,
                                     Principal principal,
                                     RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView("form_outing");
            mav.addObject("outingId", id); // Pourquoi pas passer OutingDTO ?

            /*  Je commente ça parce qu'il va falloir l'expliquer à Massat ou changer, requête négligeable avec 200 éléments
                mais clairement pas maintenable sur beaucoup d'éléments, faut également passer le DTO pas l'entité Category
                TODO: Réfléchir à implémenter le changement de catégorie autrement, une petite recherche par nom pourrait suffir
                (D'autant plus si tu gardes les modifs de formulaire en session ou attribut)

            Pageable allStyles = (Pageable) PageRequest.of(0, Integer.MAX_VALUE);
            mav.addObject("categories", categoryService.getAllCategories((org.springframework.data.domain.Pageable) allStyles).getContent());
            */

            return mav;
        }

        // TODO: Utiliser l'utilitaire de checking comme au dessus
        Outing existingOuting = outingService.findOutingById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!existingOuting.getOwner().getEmail().equals(principal.getName())) {
            return new ModelAndView("redirect:/outings/" + id + "?error=unauthorized");
        }

        try {
            existingOuting.setName(updateDto.getName());
            existingOuting.setDescription(updateDto.getDescription());
            existingOuting.setDate(updateDto.getDateOuting());
            existingOuting.setWebsite(updateDto.getWebSite());

            outingService.updateOuting(existingOuting);

            redirectAttributes.addFlashAttribute("success", "Sortie mise à jour avec succès !");
            return new ModelAndView("redirect:/outings/" + id);

        } catch (Exception e) {
            ModelAndView localMaV = new ModelAndView("form_outing");
            localMaV.addObject("error", "Erreur lors de la mise à jour : " + e.getMessage());
            return localMaV;
        }
    }


    @PostMapping("/{id}/delete")
    public ModelAndView deleteOuting(@PathVariable Long id,
                                     Principal principal,
                                     RedirectAttributes redirectAttributes) {

        // TODO: Utiliser l'utilitaire de check des droits comme au dessus
        Outing outing = outingService.findOutingById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sortie introuvable"));

        if (!outing.getOwner().getEmail().equals(principal.getName())) {
            redirectAttributes.addFlashAttribute("error", "Vous n'avez pas l'autorisation de supprimer cette sortie.");
            return new ModelAndView("redirect:/outings/" + id);
        }

        try {
            outingService.deleteOuting(id);

            redirectAttributes.addFlashAttribute("success", "La sortie a été supprimée avec succès.");
            return new ModelAndView("redirect:/categories");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression : " + e.getMessage());
            return new ModelAndView("redirect:/outings/" + id);
        }
    }

}
