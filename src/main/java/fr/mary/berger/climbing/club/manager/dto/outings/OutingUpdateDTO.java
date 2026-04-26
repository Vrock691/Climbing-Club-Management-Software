package fr.mary.berger.climbing.club.manager.dto.outings;

import lombok.Data;

import java.util.Date;

public record OutingUpdateDTO (
     Long id,
     String name,
     String description,
     Date date,
     String website,
     Long categoryId
) {}