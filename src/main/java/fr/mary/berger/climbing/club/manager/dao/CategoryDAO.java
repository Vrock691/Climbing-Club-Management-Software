package fr.mary.berger.climbing.club.manager.dao;

import fr.mary.berger.climbing.club.manager.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDAO extends JpaRepository<Category, Long> {
}
