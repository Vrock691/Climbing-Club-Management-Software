package fr.mary.berger.climbing.club.manager.services;

import fr.mary.berger.climbing.club.manager.TestDataInitializer;
import fr.mary.berger.climbing.club.manager.models.Category;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class TestCategoryService {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void testCreateCategory() {
        Category category = new Category();
        category.setName("test");

        categoryService.createCategory(category);
        assertNotNull(category.getId());
        assertEquals("test", category.getName());
    }

}
