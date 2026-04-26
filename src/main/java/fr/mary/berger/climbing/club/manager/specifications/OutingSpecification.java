package fr.mary.berger.climbing.club.manager.specifications;

import fr.mary.berger.climbing.club.manager.dto.outings.OutingSearchCriteria;
import fr.mary.berger.climbing.club.manager.models.Outing;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class OutingSpecification {

    public static Specification<Outing> withCriteria(OutingSearchCriteria outingSearchCriteria) {
        return (root, query, criteriaBuilder) ->  {
            List<Predicate> predicates = new ArrayList<>();

            if (outingSearchCriteria.name() != null && !outingSearchCriteria.name().isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                        "%" + outingSearchCriteria.name().toLowerCase() + "%"));
            }

            if (outingSearchCriteria.categoryIds() != null && !outingSearchCriteria.categoryIds().isEmpty()) {
                predicates.add(root.get("category").get("id").in(outingSearchCriteria.categoryIds()));
            }

            if (outingSearchCriteria.ownerIds() != null && !outingSearchCriteria.ownerIds().isEmpty()) {
                predicates.add(root.get("owner").get("id").in(outingSearchCriteria.ownerIds()));
            }

            if (outingSearchCriteria.dateFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), outingSearchCriteria.dateFrom()));
            }

            if (outingSearchCriteria.dateTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), outingSearchCriteria.dateTo()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
