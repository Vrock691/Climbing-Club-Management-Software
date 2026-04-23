package fr.mary.berger.climbing.club.manager.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/categories")
public class CategoriesController {

    @GetMapping
    public String categories(Principal principal, Model model) {
        return "categories";
    }

    @GetMapping("/{id}")
    public String categories(@PathVariable Long id, Model model, Principal principal) {
        return "categories";
    }

}
