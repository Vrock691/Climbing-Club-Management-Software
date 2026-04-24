package fr.mary.berger.climbing.club.manager.dto.outings;

import fr.mary.berger.climbing.club.manager.dto.PaginatedResponse;

public record OutingsResponseDTO(
    PaginatedResponse<OutingDTO> content,
    String error
) {}
