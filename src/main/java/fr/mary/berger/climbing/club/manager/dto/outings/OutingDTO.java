package fr.mary.berger.climbing.club.manager.dto.outings;

import fr.mary.berger.climbing.club.manager.dto.categories.CategoryDTO;
import fr.mary.berger.climbing.club.manager.dto.member.MemberDTO;

import java.util.Date;

public record OutingDTO(
        Long id,
        CategoryDTO category,
        MemberDTO member,
        String name,
        String description,
        String website,
        Date date
) {}
