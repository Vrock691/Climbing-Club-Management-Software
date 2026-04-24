package fr.mary.berger.climbing.club.manager.controllers;

import fr.mary.berger.climbing.club.manager.dto.PaginatedResponse;
import fr.mary.berger.climbing.club.manager.dto.categories.CategoriesResponseDTO;
import fr.mary.berger.climbing.club.manager.dto.categories.CategoryDTO;
import fr.mary.berger.climbing.club.manager.dto.member.MemberDTO;
import fr.mary.berger.climbing.club.manager.dto.outings.OutingDTO;
import fr.mary.berger.climbing.club.manager.dto.outings.OutingsResponseDTO;
import fr.mary.berger.climbing.club.manager.models.Category;
import fr.mary.berger.climbing.club.manager.models.Outing;
import fr.mary.berger.climbing.club.manager.services.CategoryService;
import fr.mary.berger.climbing.club.manager.services.OutingService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoriesController {

    private final CategoryService categoryService;
    private final OutingService outingService;

    @GetMapping
    public ModelAndView categories(@RequestParam @Nullable String categoryName, @PageableDefault(size = 20) Pageable pageable) {
        Page<Category> results = (categoryName != null)
                ? categoryService.findCategoryByNamePattern(categoryName, pageable)
                : categoryService.getAllCategories(pageable);

        List<CategoryDTO> categoryDTOs = results.map(cat ->
                new CategoryDTO(cat.getId(), cat.getName())
        ).getContent();

        PaginatedResponse<CategoryDTO> paginatedResponse = new PaginatedResponse<>(
                categoryDTOs,
                results.getNumber(),
                results.getSize(),
                results.getTotalElements(),
                results.getTotalPages(),
                results.isFirst(),
                results.isLast()
        );

        return new ModelAndView("categoriesScreen", "paginatedResponse", paginatedResponse);
    }

    // TODO: Implémenter un controle d'identité
    @GetMapping("/{id}")
    public ModelAndView categories(@PathVariable Long id, @PageableDefault(size = 20) Pageable pageable) {
        Optional<Category> category = categoryService.findCategoryById(id);
        if (category.isEmpty()) {
            String error = "Category not found";
            OutingsResponseDTO response = new OutingsResponseDTO(null, error);
            return new ModelAndView("outingListScreen", "paginatedResponse", response);
        }

        Page<Outing> results = outingService.findOutingByCategory(category.get(), pageable);
        List<OutingDTO> outingDTOs = results.map(cat ->
                new OutingDTO(
                        cat.getId(),
                        new CategoryDTO(cat.getCategory().getId(), cat.getCategory().getName()),
                        null,
                        cat.getName(),
                        cat.getDescription(),
                        null,
                        cat.getDate()
                )
        ).getContent();
        PaginatedResponse<OutingDTO> paginatedResponse = new PaginatedResponse<>(
                outingDTOs,
                results.getNumber(),
                results.getSize(),
                results.getTotalElements(),
                results.getTotalPages(),
                results.isFirst(),
                results.isLast()
        );
        OutingsResponseDTO response = new OutingsResponseDTO(paginatedResponse, null);
        return new ModelAndView("outingListScreen", "paginatedResponse", response);
    }

}
