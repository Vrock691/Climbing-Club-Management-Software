package fr.mary.berger.climbing.club.manager.dto.outings;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public record OutingSearchCriteria(
        String name,
        List<Long> categoryIds,
        List<String> ownerIds,
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        Date dateFrom,
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        Date dateTo
) {
}
