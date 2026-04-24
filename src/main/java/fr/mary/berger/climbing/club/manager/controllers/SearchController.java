package fr.mary.berger.climbing.club.manager.controllers;

import fr.mary.berger.climbing.club.manager.dto.OutingSearchCriteria;
import fr.mary.berger.climbing.club.manager.services.OutingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final OutingService outingService;

    @GetMapping
    public String showSearchForm(Principal principal, Model model) {
        return "searchForm";
    }

    // TODO: mieux implémenter ça, en créant un dto possiblement
    @PostMapping
    public String search(@RequestParam(required = false) String name,
                         @RequestParam(required = false) List<Long> categoriesIds,
                         @RequestParam(required = false) List<Long> ownersIds,
                         @RequestParam(required = false) Date dateFrom,
                         @RequestParam(required = false) Date dateTo,
                         @PageableDefault(size = 20) Pageable pageable,
                         Model model) {

        // TODO: Mieux utiliser les critères de recherche
        OutingSearchCriteria outingSearchCriteria = new OutingSearchCriteria(
                name,
                categoriesIds,
                ownersIds,
                dateFrom,
                dateTo
        );

        // TODO: Créer un DTO pour le modèle, ne pas renvoyer les informations directement sortie du service
        model.addAttribute("pageSorties", outingService.searchOuting(outingSearchCriteria, pageable)); // utilisation méthode générique todo : adapter
        return "outingsScreen";
    }

}
