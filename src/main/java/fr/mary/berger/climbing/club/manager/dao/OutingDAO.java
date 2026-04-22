package fr.mary.berger.climbing.club.manager.dao;

import fr.mary.berger.climbing.club.manager.models.Category;
import fr.mary.berger.climbing.club.manager.models.Outing;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.awt.print.Pageable;
import java.util.List;

public interface OutingDAO extends JpaRepository<Outing, Long>, JpaSpecificationExecutor<Outing> {

    List<Outing> findAll(Pageable pageable);

    Page<Outing> findOutingByCategory(Category category, org.springframework.data.domain.Pageable pageable);

    Outing findOutingById(Long id);
}
