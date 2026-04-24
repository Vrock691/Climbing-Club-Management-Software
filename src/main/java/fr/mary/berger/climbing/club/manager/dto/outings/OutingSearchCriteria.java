package fr.mary.berger.climbing.club.manager.dto.outings;

import java.util.Date;
import java.util.List;

public record OutingSearchCriteria(
        String name,
        List<Long> categoryIds,
        List<Long> ownerIds,
        Date dateFrom,
        Date dateTo
) {
}
