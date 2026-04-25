package fr.mary.berger.climbing.club.manager.controllers;

import fr.mary.berger.climbing.club.manager.dao.MemberDAO;
import fr.mary.berger.climbing.club.manager.dto.PaginatedResponse;
import fr.mary.berger.climbing.club.manager.dto.categories.CategoryDTO;
import fr.mary.berger.climbing.club.manager.dto.member.MemberDTO;
import fr.mary.berger.climbing.club.manager.dto.outings.OutingDTO;
import fr.mary.berger.climbing.club.manager.dto.outings.OutingSearchCriteria;
import fr.mary.berger.climbing.club.manager.dto.outings.OutingsListResponseDTO;
import fr.mary.berger.climbing.club.manager.models.Category;
import fr.mary.berger.climbing.club.manager.models.Member;
import fr.mary.berger.climbing.club.manager.models.Outing;
import fr.mary.berger.climbing.club.manager.services.CategoryService;
import fr.mary.berger.climbing.club.manager.services.OutingService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
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

    @GetMapping("/{id}")
    public ModelAndView categories(@PathVariable Long id, @ModelAttribute @Nullable OutingSearchCriteria search, Principal principal, @PageableDefault(size = 20) Pageable pageable) {
        Optional<Category> category = categoryService.findCategoryById(id);
        if (category.isEmpty()) {
            String error = "Catégorie inexistante";
            OutingsListResponseDTO response = new OutingsListResponseDTO(null, null, error);
            return new ModelAndView("outingListScreen", "paginatedResponse", response);
        }

        Page<Outing> results;
        if (search == null) {
            results = outingService.findOutingByCategory(category.get(), pageable);
        } else {
            OutingSearchCriteria criteria = new OutingSearchCriteria(
                    search.name(),
                    List.of(category.get().getId()),
                    search.ownerIds(),
                    search.dateFrom(),
                    search.dateTo()
            );
            results = outingService.searchOuting(criteria, pageable);
        }

        List<OutingDTO> outingDTOs = results.map(cat ->
                new OutingDTO(
                        cat.getId(),
                        new CategoryDTO(cat.getCategory().getId(), cat.getCategory().getName()),
                        (principal != null)
                                ? new MemberDTO(
                                    cat.getOwner().getUsername(),
                                    cat.getOwner().getFirstName(),
                                    cat.getOwner().getLastName())
                                : null,
                        cat.getName(),
                        cat.getDescription(),
                        (principal != null) ? cat.getWebsite() : null,
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
        List<MemberDTO> organizers = (principal != null)
                ? outingDTOs.stream()
                    .map(OutingDTO::member)
                    .distinct()
                    .toList()
                : null;

        OutingsListResponseDTO response = new OutingsListResponseDTO(paginatedResponse, organizers, null);
        return new ModelAndView("outingListScreen", "paginatedResponse", response);
    }

}
