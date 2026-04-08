package fr.mary.berger.climbing.club.manager.dao;

import fr.mary.berger.climbing.club.manager.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryDAO extends JpaRepository<Category, Long> {
}
