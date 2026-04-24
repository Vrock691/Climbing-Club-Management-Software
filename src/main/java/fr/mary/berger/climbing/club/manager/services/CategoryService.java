package fr.mary.berger.climbing.club.manager.services;

import fr.mary.berger.climbing.club.manager.dao.CategoryDAO;
import fr.mary.berger.climbing.club.manager.models.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryDAO categoryDAO;

    public Page<Category> getAllCategories(Pageable pageable) {
        return categoryDAO.findAll(pageable);
    }

    public void createCategory(Category category) {
        categoryDAO.save(category);
    }

    public Optional<Category> findCategoryById(long id) {
        return categoryDAO.findById(id);
    }

    public void updateCategory(Category updatedCategory) {
        categoryDAO.save(updatedCategory);
    }

    public Page<Category> findCategoryByNamePattern(String categoryNameQuery, Pageable pageable) {
        return categoryDAO.findCategoriesByNameContaining(categoryNameQuery, pageable);
    }
}
