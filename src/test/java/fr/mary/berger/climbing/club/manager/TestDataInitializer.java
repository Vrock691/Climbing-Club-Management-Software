package fr.mary.berger.climbing.club.manager;

import fr.mary.berger.climbing.club.manager.dao.CategoryDAO;
import fr.mary.berger.climbing.club.manager.dao.MemberDAO;
import fr.mary.berger.climbing.club.manager.dao.OutingDAO;
import fr.mary.berger.climbing.club.manager.models.Category;
import fr.mary.berger.climbing.club.manager.models.Member;
import fr.mary.berger.climbing.club.manager.models.Outing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@Profile("test")
@RequiredArgsConstructor
public class TestDataInitializer implements CommandLineRunner {

    private final CategoryDAO categoryDAO;
    private final MemberDAO memberDAO;
    private final OutingDAO outingDAO;

    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing test data...");

        // Categories
        List<Category> categories = new ArrayList<>(List.of());
        for (int i = 0; i < 50; i++) {
            Category newCategory = new Category();
            newCategory.setName("test" + i);
            categories.add(newCategory);
        }
        categoryDAO.saveAll(categories);

        // Members
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            Member newMember = new Member();
            newMember.setFirstName("member-" + i);
            newMember.setLastName("member-" + i);
            newMember.setEmail("member-" + i + "@test.com");
            members.add(newMember);
        }
        memberDAO.saveAll(members);

        // Outings
        List<Outing> outings = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            for (int j = 0; j < 100; j++) {
                Outing newAssociatedOuting = new Outing();
                newAssociatedOuting.setName("outing-" + i + "-" + j);
                newAssociatedOuting.setDate(new Date());
                newAssociatedOuting.setOwner(members.get(i));
                newAssociatedOuting.setDescription("test-outing-" + i + "-" + j);
                newAssociatedOuting.setWebsite("outing-" + i + "-" + j + "-website.com");
                newAssociatedOuting.setCategory(categories.get(j/2));
                outings.add(newAssociatedOuting);
            }
        }
        outingDAO.saveAll(outings);

        log.info("Initializing test data finished");
    }
}
