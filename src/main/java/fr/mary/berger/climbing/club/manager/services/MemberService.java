package fr.mary.berger.climbing.club.manager.services;

import fr.mary.berger.climbing.club.manager.dao.MemberDAO;
import fr.mary.berger.climbing.club.manager.models.Member;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberDAO memberDAO;

    public Page<Member> findAllMembers(Pageable pageable) {
        return memberDAO.findAll(pageable);
    }

    public Optional<Member> findMemberByUsername(String username) {
        return memberDAO.findMemberByUsername(username);
    }

    public void createMember(Member member) {
        memberDAO.save(member);
    }

    public Optional<Member> findMemberByEmail(String email) {
        return memberDAO.findMemberByEmail(email);
    }

    public void changePassword(Member member, String newEncodedPassword) {
        member.setEncodedPassword(newEncodedPassword);
        memberDAO.save(member);
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        Member member = memberDAO.findMemberByUsername(username)//
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return User.withUsername(member.getUsername())
                .password(member.getEncodedPassword())
                .disabled(false)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .build();
    }
}
