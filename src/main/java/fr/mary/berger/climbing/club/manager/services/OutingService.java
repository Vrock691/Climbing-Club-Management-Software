package fr.mary.berger.climbing.club.manager.services;

import fr.mary.berger.climbing.club.manager.dao.OutingDAO;
import fr.mary.berger.climbing.club.manager.dto.outings.OutingSearchCriteria;
import fr.mary.berger.climbing.club.manager.models.Category;
import fr.mary.berger.climbing.club.manager.models.Outing;
import fr.mary.berger.climbing.club.manager.specifications.OutingSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OutingService {

    private final OutingDAO outingDAO;

    public Page<Outing> findOutingByCategory(Category category, Pageable pageable) {
        return outingDAO.findOutingsByCategory(category, pageable);
    }

    public Optional<Outing> findOutingById(Long id) {
        return outingDAO.findById(id);
    }

    public Page<Outing> searchOuting(OutingSearchCriteria outingSearchCriteria, Pageable pageable) {
        Specification<Outing> spec = OutingSpecification.withCriteria(outingSearchCriteria);
        return outingDAO.findAll(spec, pageable);
    }

    public void createOuting(Outing outing) {
        outingDAO.save(outing);
    }

    public void deleteOuting(Long id) {
        outingDAO.deleteById(id);
    }

    public void updateOuting(Outing outing) {
        outingDAO.save(outing);
    }

}
