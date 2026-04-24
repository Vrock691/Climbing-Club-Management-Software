package fr.mary.berger.climbing.club.manager.controllers;

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

import java.util.Optional;

@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoriesController {

    private final CategoryService categoryService;
    private final OutingService outingService;

    // TODO: réflechir si un DTO est nécéssaire ou pas, je pense pas perso, ya rien dans l'object category mais bon
    // TODO: Faire marcher la vue aussi
    @GetMapping
    public String categories(@PageableDefault(size = 20) Pageable pageable, Model model) {
        model.addAttribute("categories", categoryService.getAllCategories(pageable));
        return "home";
    }

    // TODO: Créer des DTO pour la transmission des Outings, en fonction certaines infos ne doivent pas partir genre le site web ou l'organisateur
    // TODO: Implémenter un controle d'identité
    // TODO: gérer les erreurs
    @GetMapping("/{id}")
    public String categories(@PathVariable Long id, @PageableDefault(size = 20) Pageable pageable, Model model) {
        Optional<Category> category = categoryService.findCategoryById(id);
        if (category.isEmpty()) {}

        model.addAttribute("pageSorties", outingService.findOutingByCategory(category.get(), pageable));
        model.addAttribute("categorie", categoryService.findCategoryById(id));
        return "outingsScreen";
    }

}
