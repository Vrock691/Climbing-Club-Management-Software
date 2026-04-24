package fr.mary.berger.climbing.club.manager.dto.categories;

import fr.mary.berger.climbing.club.manager.dto.PaginatedResponse;

public record CategoriesResponseDTO(
        PaginatedResponse<CategoryDTO> categories
) {}
