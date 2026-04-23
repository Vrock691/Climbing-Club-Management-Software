package fr.mary.berger.climbing.club.manager.dao;

import fr.mary.berger.climbing.club.manager.models.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberDAO extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByEmail(String email);
}
