package fr.mary.berger.climbing.club.manager.security.validators;

import fr.mary.berger.climbing.club.manager.models.Member;
import fr.mary.berger.climbing.club.manager.models.Outing;
import fr.mary.berger.climbing.club.manager.services.MemberService;
import fr.mary.berger.climbing.club.manager.services.OutingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OutingModificationRightsChecker {

    private final OutingService outingService;
    private final MemberService memberService;

    // La modification ou la suppression d'une sortie est permise uniquement à son organisateur
    public boolean isModificationPermitted(String username, long outingId) {
        Optional<Member> member = memberService.findMemberByUsername(username);
        if (member.isEmpty()) throw new NoSuchElementException("Member with id " + username + " not found");

        Optional<Outing> outing = outingService.findOutingById(outingId);
        if (outing.isEmpty()) throw new NoSuchElementException("Outing with id " + outingId + " not found");

        return outing.get().getOwner().getUsername().equals(member.get().getUsername());
    }

}
