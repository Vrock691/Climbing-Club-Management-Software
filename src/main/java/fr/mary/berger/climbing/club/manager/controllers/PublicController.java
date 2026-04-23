package fr.mary.berger.climbing.club.manager.controllers;

import fr.mary.berger.climbing.club.manager.dto.OutingSearchCriteria;
import fr.mary.berger.climbing.club.manager.models.Category;
import fr.mary.berger.climbing.club.manager.services.CategoryService;
import fr.mary.berger.climbing.club.manager.services.OutingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicController {

    private final OutingService outingService;
    private final CategoryService categoryService;

    // Liste des catégories
    @GetMapping("/categories")
    public String listCategories(@PageableDefault(size = 20) Pageable pageable, Model model) {
        model.addAttribute("categories", categoryService.getAllCategories(pageable));
        return "home"; 
    }

    // Liste des sorties d'une catégorie avec pagination
    @GetMapping("/categories/{id}")
    public String listByCategorie(@PathVariable Long id, @PageableDefault(size = 20) Pageable pageable, Model model) {
        Optional<Category> category = categoryService.findCategoryById(id);
        if (category.isEmpty()) {
            // TODO: gérer l'erreur
        }

        model.addAttribute("pageSorties", outingService.findOutingByCategory(category.get(), pageable));
        model.addAttribute("categorie", categoryService.findCategoryById(id));
        return "outingScreen";
    }

    // Vue détaillée d'une sortie
    @GetMapping("/outings/{id}")
    public String detailSortie(@PathVariable Long id, Model model) {
        model.addAttribute("sortie", outingService.findOutingById(id));
        return "outingDetailScreen";
    }

    // Recherche multi-critères
    @GetMapping("/outings/search")
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