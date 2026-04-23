package fr.mary.berger.climbing.club.manager.services;

import fr.mary.berger.climbing.club.manager.models.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class TestMemberService {

    @Autowired
    private MemberService memberService;

    @Test
    public void testFindAllMembers() {
        Pageable pageable = PageRequest.of(0, 200);
        Page<Member> memberList = memberService.findAllMembers(pageable);

        assertEquals(200, memberList.getTotalElements());
        for (int i = 0; i < memberList.getNumberOfElements(); i++) {
            Member member = memberList.getContent().get(i);
            assertNotNull(member);
            assertEquals("member-" + i, member.getFirstName());
        }
    }



}