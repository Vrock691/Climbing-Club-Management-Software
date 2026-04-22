package fr.mary.berger.climbing.club.manager.services;

import fr.mary.berger.climbing.club.manager.dao.CategoryDAO;
import fr.mary.berger.climbing.club.manager.models.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    CategoryDAO categoryDAO;

    public Page<Category> getAllCategories(Pageable pageable) {
        return categoryDAO.findAll(pageable);
    }

    public void createCategory(Category category) {
        categoryDAO.save(category);
    }

}
