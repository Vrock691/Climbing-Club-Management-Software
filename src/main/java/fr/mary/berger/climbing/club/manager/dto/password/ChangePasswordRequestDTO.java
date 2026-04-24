package fr.mary.berger.climbing.club.manager.dto.password;

public record ChangePasswordRequestDTO(
   String token,
   String password
) {}
