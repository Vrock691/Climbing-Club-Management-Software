package fr.mary.berger.climbing.club.manager.services;

import fr.mary.berger.climbing.club.manager.dto.outings.OutingSearchCriteria;
import fr.mary.berger.climbing.club.manager.models.Category;
import fr.mary.berger.climbing.club.manager.models.Outing;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TestOutingService {

    @Autowired
    private OutingService outingService;

    private final PageRequest pageRequest = PageRequest.of(0, 10);
    private static Category category = new Category();

    @BeforeAll
    static void beforeAll() {
        category.setName("category-0");
        category.setId(1L);
    }

    @Test
    public void testBlankSearch() {
        OutingSearchCriteria criteria1 = new OutingSearchCriteria(
                null,
                List.of(category.getId()),
                null,
                null,
                null
        );

        List<Outing> results = outingService.searchOuting(criteria1, pageRequest).stream().toList();
        List<Outing> expected = outingService.findOutingByCategory(category, pageRequest).stream().toList();
        assertEquals(expected, results);
    }

    @Test
    public void testNamePatternSearch() {
        OutingSearchCriteria criteria2 = new OutingSearchCriteria(
                "outing-1",
                List.of(category.getId()),
                null,
                null,
                null
        );
        List<Outing> results = outingService.searchOuting(criteria2, pageRequest).stream().toList();
        assertTrue(results.stream().map(Outing::getName).allMatch(name -> name.startsWith("outing-1")));
    }

    @Test
    public void testOwnerSearch() {
        List<Outing> results;

        OutingSearchCriteria criteria3 = new OutingSearchCriteria(
                null,
                List.of(category.getId()),
                List.of("member-1"),
                null,
                null
        );
        results = outingService.searchOuting(criteria3, pageRequest).stream().toList();
        assertTrue(results.stream().map(Outing::getOwner).allMatch(owner -> owner.getUsername().equals("member-1")));

        OutingSearchCriteria criteria4 = new OutingSearchCriteria(
                null,
                List.of(category.getId()),
                List.of("member-1", "member-2"),
                null,
                null
        );
        results = outingService.searchOuting(criteria4, pageRequest).stream().toList();
        assertTrue(results.stream().map(Outing::getOwner).allMatch(owner ->
                owner.getUsername().equals("member-1") || owner.getUsername().equals("member-2")));
    }

    @Test
    public void testDateSearch() {
        Date fromDate = new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
        Date toDate = new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000));

        OutingSearchCriteria criteria5 = new OutingSearchCriteria(
                null,
                List.of(category.getId()),
                null,
                fromDate,
                toDate
        );

        List<Outing> results = outingService.searchOuting(criteria5, pageRequest).stream().toList();
        assertTrue(results.stream().map(Outing::getDate).allMatch(date ->
                date.after(fromDate) && date.before(toDate)));
    }

    @Test
    public void testAllParametersSearch() {
        Date fromDate = new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
        Date toDate = new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000));

        OutingSearchCriteria criteria6 = new OutingSearchCriteria(
                "outing-1",
                List.of(category.getId()),
                List.of("member-1", "member-2"),
                fromDate,
                toDate
        );

        List<Outing> results = outingService.searchOuting(criteria6, pageRequest).stream().toList();
        assertTrue(results.stream().map(Outing::getName).allMatch(name -> name.startsWith("outing-1")));
        assertTrue(results.stream().map(Outing::getOwner).allMatch(owner ->
                owner.getUsername().equals("member-1") || owner.getUsername().equals("member-2")));
        assertTrue(results.stream().map(Outing::getDate).allMatch(date ->
                date.after(fromDate) && date.before(toDate)));
        assertTrue(results.stream().map(Outing::getCategory).allMatch(
                _category -> _category.getId().equals(category.getId())
        ));
    }

}
