package fr.mary.berger.climbing.club.manager.security;

import fr.mary.berger.climbing.club.manager.models.Member;
import fr.mary.berger.climbing.club.manager.models.Outing;
import fr.mary.berger.climbing.club.manager.services.MemberService;
import fr.mary.berger.climbing.club.manager.services.OutingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestOutingModificationRightsChecker {

    @InjectMocks
    private OutingModificationRightsChecker testOutingModificationRightsChecker;

    @Mock
    private OutingService outingService;

    @Mock
    private MemberService memberService;

    private Outing outing;
    private Member member, member2;

    @BeforeEach
    public void setUp() {

        member = new Member();
        member.setUsername("username1");

        member2 = new Member();
        member2.setUsername("username2");

        outing = new Outing();
        outing.setId(1L);
        outing.setOwner(member);

        when(outingService.findOutingById(anyLong())).thenAnswer(iom -> {
            long id = iom.getArgument(0);
            if (id == 1) return Optional.of(outing);
            return Optional.empty();
        });

        when(memberService.findMemberByUsername(anyString())).thenAnswer(iom -> {
            String id = iom.getArgument(0);
            return switch (id) {
                case "username1" -> Optional.of(member);
                case "username2" -> Optional.of(member2);
                default -> Optional.empty();
            };
        });
    }

    @Test
    public void testInvalidArguments() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> testOutingModificationRightsChecker.isModificationPermitted("username999", 1L));
        Assertions.assertThrows(NoSuchElementException.class,
                () -> testOutingModificationRightsChecker.isModificationPermitted("username1", 0L));
    }

    @Test
    public void testChecker() {
        Assertions.assertTrue(testOutingModificationRightsChecker.isModificationPermitted("username1", 1L));
        Assertions.assertFalse(testOutingModificationRightsChecker.isModificationPermitted("username2", 1L));
    }

}
