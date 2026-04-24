package fr.mary.berger.climbing.club.manager.dao;

import fr.mary.berger.climbing.club.manager.models.Category;
import fr.mary.berger.climbing.club.manager.models.Outing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OutingDAO extends JpaRepository<Outing, Long>, JpaSpecificationExecutor<Outing> {
    Page<Outing> findOutingsByCategory(Category category, Pageable pageable);

    Page<Outing> findByOwner_Username(String ownerUsername, Pageable pageable);
}
