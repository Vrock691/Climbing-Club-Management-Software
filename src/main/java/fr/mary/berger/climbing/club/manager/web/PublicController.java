package fr.mary.berger.climbing.club.manager.web;

import fr.mary.berger.climbing.club.manager.models.Category;
import fr.mary.berger.climbing.club.manager.models.Outing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private Outing outingService; // en attente d'implementation

    @Autowired
    private Category categoryService; // en attente d'implementation

    // Liste des catégories
    @GetMapping("/categories")
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.findAll()); // utilisation méthode générique todo : adapter
        return "home"; 
    }

    // Liste des sorties d'une catégorie avec pagination
    @GetMapping("/categorie/{id}")
    public String listByCategorie(@PathVariable Long id, 
                                 @PageableDefault(size = 20) Pageable pageable, 
                                 Model model) {
        model.addAttribute("pageSorties", outingService.findByCategorie(id, pageable)); // utilisation méthode générique todo : adapter
        model.addAttribute("categorie", categoryService.findById(id)); // utilisation méthode générique todo : adapter
        return "sorties_liste";
    }

    // Vue détaillée d'une sortie
    @GetMapping("/sortie/{id}")
    public String detailSortie(@PathVariable Long id, Model model) {
        model.addAttribute("sortie", outingService.findById(id)); // utilisation méthode générique todo : adapter
        return "sortie_detail";
    }

    // Recherche multi-critères
    @GetMapping("/search")
    public String search(@RequestParam(required = false) String nom,
                         @RequestParam(required = false) Long idCategorie,
                         @PageableDefault(size = 20) Pageable pageable,
                         Model model) {
        model.addAttribute("pageSorties", outingService.search(nom, idCategorie, pageable)); // utilisation méthode générique todo : adapter
        return "sorties_liste";
    }
}