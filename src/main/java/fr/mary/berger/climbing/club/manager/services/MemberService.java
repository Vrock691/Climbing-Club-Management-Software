package fr.mary.berger.climbing.club.manager.services;

import fr.mary.berger.climbing.club.manager.dao.MemberDAO;
import fr.mary.berger.climbing.club.manager.models.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    MemberDAO memberDAO;

    Page<Member> findAllMembers(Pageable pageable) {
        return memberDAO.findAll(pageable);
    }

    Member findMemberById(Long id) {
        return memberDAO.findMemberById(id);
    }



}
