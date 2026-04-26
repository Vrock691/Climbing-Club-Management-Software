package fr.mary.berger.climbing.club.manager.dto.outings;

import fr.mary.berger.climbing.club.manager.dto.categories.CategoryDTO;
import fr.mary.berger.climbing.club.manager.dto.member.MemberDTO;

import java.util.Date;

public record OutingDTO(
        Long id,
        String name,
        String description,
        Date date,
        String website,
        Long categoryId,
        String categoryName,
        String organizerName
) {}
