package fr.mary.berger.climbing.club.manager.dao;

import fr.mary.berger.climbing.club.manager.models.Category;
import fr.mary.berger.climbing.club.manager.models.Member;
import fr.mary.berger.climbing.club.manager.models.Outing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutingDAO extends JpaRepository<Outing, Long> {

    List<Outing> findAllOutingsByCategory(Category category);

    List<Outing> findAllOutingsByOwner(Member owner);

}
