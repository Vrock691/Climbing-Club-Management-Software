package fr.mary.berger.climbing.club.manager.dto.outings;

import fr.mary.berger.climbing.club.manager.dto.PaginatedResponse;
import fr.mary.berger.climbing.club.manager.dto.member.MemberDTO;

import java.util.List;

public record OutingsListResponseDTO(
    PaginatedResponse<OutingDTO> outings,
    List<MemberDTO> organizers,
    String error
) {}
