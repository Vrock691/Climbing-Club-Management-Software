package fr.mary.berger.climbing.club.manager.controllers;

import fr.mary.berger.climbing.club.manager.dto.outings.OutingResponseDTO;
import fr.mary.berger.climbing.club.manager.dto.outings.OutingSearchCriteria;
import fr.mary.berger.climbing.club.manager.dao.CategoryDAO;
import fr.mary.berger.climbing.club.manager.dao.MemberDAO;
import fr.mary.berger.climbing.club.manager.models.Outing;
import fr.mary.berger.climbing.club.manager.services.OutingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
public class SearchController {

    private final OutingService outingService;
    private final CategoryDAO categoryDAO;
    private final MemberDAO memberDAO;

    @GetMapping
    public String showSearchForm(Model model) {
        model.addAttribute("categories", categoryDAO.findAll());
        model.addAttribute("members", memberDAO.findAll());
        return "searchFormScreen";
    }

    @PostMapping
    public ModelAndView search(@RequestParam(required = false) String name,
                               @RequestParam(required = false) List<Long> categoriesIds,
                               @RequestParam(required = false) List<String> ownersIds,
                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFrom,
                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateTo,
                               @PageableDefault(size = 20) Pageable pageable,
                               Principal principal) {

        OutingSearchCriteria outingSearchCriteria = new OutingSearchCriteria(
                name,
                categoriesIds,
                ownersIds,
                dateFrom,
                dateTo
        );

        Page<Outing> outingPage = outingService.searchOuting(outingSearchCriteria, pageable);

        Page<OutingResponseDTO> dtoPage = outingPage.map(outing -> {
            OutingResponseDTO dto = new OutingResponseDTO();
            dto.setId(outing.getId());
            dto.setName(outing.getName());
            dto.setDescription(outing.getDescription());
            dto.setDateOuting(outing.getDate());

            if (principal != null) {
                dto.setWebSite(outing.getWebsite());
                dto.setOwnerName(outing.getOwner().getFirstName() + " " + outing.getOwner().getLastName());
            }
            return dto;
        });

        ModelAndView localMaV = new ModelAndView("outingsScreen");
        localMaV.addObject("pageSorties", dtoPage);
        localMaV.addObject("criteria", outingSearchCriteria);
        return localMaV;
    }

}
