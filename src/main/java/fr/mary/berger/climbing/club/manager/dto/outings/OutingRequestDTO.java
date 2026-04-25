package fr.mary.berger.climbing.club.manager.dto.outings;

import lombok.Data;

import java.util.Date;

// TODO: Transformer ce DTO sur le modèle de OutingsListResponse (record + PaginatedResponse)
// TODO: Donner un nom plus précis, à voir à l'usage
// TODO: Passer en record
@Data
public class OutingRequestDTO {
    private String name;
    private String description;
    private Date dateOuting;
    private String webSite;
    private Long idCategory;
}