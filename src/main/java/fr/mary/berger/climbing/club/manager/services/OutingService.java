package fr.mary.berger.climbing.club.manager.services;

import fr.mary.berger.climbing.club.manager.dao.OutingDAO;
import fr.mary.berger.climbing.club.manager.dto.OutingSearchCriteria;
import fr.mary.berger.climbing.club.manager.models.Category;
import fr.mary.berger.climbing.club.manager.models.Outing;
import fr.mary.berger.climbing.club.manager.specifications.OutingSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OutingService {

    private OutingDAO outingDAO;

    public Page<Outing> findOutingByCategory(Category category, Pageable pageable) {
        return outingDAO.findOutingByCategory(category, pageable);
    }

    public Outing findOutingById(Long id) {
        return outingDAO.findOutingById(id);
    }

    public Page<Outing> searchOuting(OutingSearchCriteria outingSearchCriteria, Pageable pageable) {
        Specification<Outing> spec = OutingSpecification.withCriteria(outingSearchCriteria);
        return outingDAO.findAll(spec, pageable);
    }

}
