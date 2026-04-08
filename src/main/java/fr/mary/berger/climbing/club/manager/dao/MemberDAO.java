package fr.mary.berger.climbing.club.manager.dao;

import fr.mary.berger.climbing.club.manager.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberDAO extends JpaRepository<Member, Long> {
}
