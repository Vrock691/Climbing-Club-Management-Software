package fr.mary.berger.climbing.club.manager.services;

import fr.mary.berger.climbing.club.manager.dao.MemberDAO;
import fr.mary.berger.climbing.club.manager.models.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private MemberDAO memberDAO;

    public Page<Member> findAllMembers(Pageable pageable) {
        return memberDAO.findAll(pageable);
    }

    public Optional<Member> findMemberById(Long id) {
        return memberDAO.findById(id);
    }

    public void createMember(Member member) {
        memberDAO.save(member);
    }

    public void deleteMember(Member member) {
        memberDAO.delete(member);
    }

    public void updateMember(Member member) {
        memberDAO.save(member);
    }

}
