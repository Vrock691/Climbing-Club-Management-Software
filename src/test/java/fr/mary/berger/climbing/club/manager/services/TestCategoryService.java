package fr.mary.berger.climbing.club.manager.services;

import fr.mary.berger.climbing.club.manager.models.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class TestCategoryService {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void testFindAllCategories() {
        Pageable pageable = PageRequest.of(0, 50);
        Page<Category> categoryList = categoryService.getAllCategories(pageable);

        assertEquals(50, categoryList.getTotalElements());
        for (int i = 0; i < categoryList.getNumberOfElements(); i++) {
            Category category = categoryList.getContent().get(i);
            assertNotNull(category);
            assertEquals("category-" + i, category.getName());
        }
    }

    @Test
    public void testCreateCategory() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<Category> categoryList = categoryService.getAllCategories(pageable);
        long categoryCount = categoryList.getTotalElements();

        Category category = new Category();
        category.setName("test");

        categoryService.createCategory(category);
        assertNotNull(category.getId());
        assertEquals("test", category.getName());

        categoryList = categoryService.getAllCategories(pageable);
        assertEquals(categoryCount + 1, categoryList.getTotalElements());
    }

    @Test
    public void testUpdateCategory() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<Category> categoryList = categoryService.getAllCategories(pageable);
        long categoryCount = categoryList.getTotalElements();

        Optional<Category> category = categoryService.getCategoryById(1);
        assertTrue(category.isPresent());

        Category updatedCategory = category.get();
        updatedCategory.setName("updated");
        categoryService.updateCategory(updatedCategory);

        assertNotNull(category.get());
        assertEquals("updated", category.get().getName());
        categoryList = categoryService.getAllCategories(pageable);
        assertEquals(categoryCount, categoryList.getTotalElements());

        updatedCategory.setName("category-1");
        categoryService.updateCategory(updatedCategory);
    }

}
