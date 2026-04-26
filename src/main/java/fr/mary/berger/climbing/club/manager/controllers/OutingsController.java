package fr.mary.berger.climbing.club.manager.controllers;

import fr.mary.berger.climbing.club.manager.dto.categories.CategoryDTO;
import fr.mary.berger.climbing.club.manager.dto.member.MemberDTO;
import fr.mary.berger.climbing.club.manager.dto.outings.*;
import fr.mary.berger.climbing.club.manager.models.Category;
import fr.mary.berger.climbing.club.manager.models.Member;
import fr.mary.berger.climbing.club.manager.models.Outing;
import fr.mary.berger.climbing.club.manager.services.CategoryService;
import fr.mary.berger.climbing.club.manager.services.MemberService;
import fr.mary.berger.climbing.club.manager.services.OutingService;
import fr.mary.berger.climbing.club.manager.security.validators.OutingModificationRightsChecker;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/outings")
@RequiredArgsConstructor
public class OutingsController {

    private final OutingService outingService;
    private final MemberService memberService;
    private final OutingModificationRightsChecker rightsChecker;
    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public ModelAndView showOutingById(@PathVariable Long id, Principal principal) {
        Outing outing = outingService.findOutingById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sortie introuvable"));

        boolean authenticated = (principal != null);

        OutingDTO outingDTO = new OutingDTO(
                outing.getId(),
                new CategoryDTO(
                        outing.getCategory().getId(),
                        outing.getCategory().getName()
                ),
                authenticated ? (new MemberDTO(
                        outing.getOwner().getUsername(),
                        outing.getOwner().getFirstName(),
                        outing.getOwner().getLastName()
                )) : null,
                outing.getName(),
                outing.getDescription(),
                authenticated ? outing.getWebsite() : null,
                outing.getDate()
        );

        return new ModelAndView("outingDetailsScreen", "outing", outingDTO);
    }

    @GetMapping("/new")
    public ModelAndView showCreateOutingForm(@RequestParam @Nullable String searchCategory) {
        List<CategoryDTO> suggestedCategoryList;
        Pageable pageable = PageRequest.of(0, 10);
        if (searchCategory == null) {
            suggestedCategoryList = categoryService.getAllCategories(pageable)
                    .stream()
                    .map(category -> new CategoryDTO(category.getId(), category.getName()))
                    .toList();
        } else {
            suggestedCategoryList = categoryService.findCategoryByNamePattern(searchCategory, pageable)
                    .stream()
                    .map(category -> new CategoryDTO(category.getId(), category.getName()))
                    .toList();
        }

        ModelAndView response = new ModelAndView("newOutingFormScreen");
        response.addObject("suggestedCategoryList", suggestedCategoryList);
        response.addObject("action", "create");
        response.addObject("outing", new OutingFormDTO());
        return response;
    }

    @PostMapping("/new")
    public ModelAndView createOuting(@ModelAttribute("outing") OutingFormDTO outingFormDTO,
                                     BindingResult result,
                                     RedirectAttributes redirectAttributes,
                                     Principal principal) {
        if (result.hasErrors()) {
            return new ModelAndView("newOutingFormScreen", "outingForm", outingFormDTO);
        }

        Outing newOuting = new Outing();
        newOuting.setName(outingFormDTO.getName());
        newOuting.setDescription(outingFormDTO.getDescription());
        newOuting.setWebsite(outingFormDTO.getWebsite());
        newOuting.setDate(outingFormDTO.getDate());

        Optional<Member> owner = memberService.findMemberByUsername(principal.getName());
        if (owner.isPresent()) {
            newOuting.setOwner(owner.get());
        } else {
            ModelAndView response = new ModelAndView("newOutingFormScreen", "outingForm", outingFormDTO);
            response.addObject("error", "Veuillez vous reconnecter");
            return response;
        }

        Optional<Category> category = categoryService.findCategoryById(outingFormDTO.getCategoryId());
        if (category.isPresent()) {
            newOuting.setCategory(category.get());
        } else {
            ModelAndView response = new ModelAndView("newOutingFormScreen", "outingForm", outingFormDTO);
            response.addObject("error", "Catégorie invalide");
            return response;
        }

        outingService.createOuting(newOuting);
        return new ModelAndView("redirect:/outings/" + newOuting.getId());
    }

    @GetMapping("/{id}/update")
    public ModelAndView showUpdateOutingForm(
            @PathVariable Long id,
            @RequestParam @Nullable String searchCategory,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {

        Outing outing = outingService.findOutingById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sortie introuvable"));

        if (!rightsChecker.isModificationPermitted(principal.getName(),outing.getId())) {
            redirectAttributes.addFlashAttribute("error", "Vous ne possdez pas l'authorisation requise");
            return new ModelAndView("redirect:/outings/");
        }

        List<CategoryDTO> suggestedCategoryList = new ArrayList<>();
        suggestedCategoryList.add(new CategoryDTO(
            outing.getCategory().getId(),
            outing.getCategory().getName()
        ));

        Pageable pageable = PageRequest.of(0, 10);
        if (searchCategory != null) {
            suggestedCategoryList.addAll(categoryService.findCategoryByNamePattern(searchCategory, pageable)
                    .stream()
                    .map(category -> new CategoryDTO(category.getId(), category.getName()))
                    .toList()
            );
        }

        OutingFormDTO outingDTO = new OutingFormDTO(
                 outing.getId(),
                 outing.getName(),
                 outing.getDescription(),
                 outing.getDate(),
                 outing.getWebsite(),
                 outing.getCategory().getId()
        );

        ModelAndView response = new ModelAndView("newOutingFormScreen");
        response.addObject("suggestedCategoryList", suggestedCategoryList);
        response.addObject("action", "edit");
        response.addObject("outing", outingDTO);
        return response;
    }

    // TODO: Simplifier, tu as trois retour d'erreur avec trois syntaxes différentes
    @PostMapping("/{id}/update")
    public ModelAndView updateOuting(@PathVariable Long id,
                                     @ModelAttribute("outing") OutingFormDTO updateDto,
                                     BindingResult result,
                                     Principal principal,
                                     RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView("newOutingFormScreen");
            mav.addObject("action", "edit");
            mav.addObject("outing", updateDto);

            /*  Je commente ça parce qu'il va falloir l'expliquer à Massat ou changer, requête négligeable avec 200 éléments
                mais clairement pas maintenable sur beaucoup d'éléments, faut également passer le DTO pas l'entité Category
                TODO: Réfléchir à implémenter le changement de catégorie autrement, une petite recherche par nom pourrait suffir
                (D'autant plus si tu gardes les modifs de formulaire en session ou attribut)

            Pageable allStyles = (Pageable) PageRequest.of(0, Integer.MAX_VALUE);
            mav.addObject("categories", categoryService.getAllCategories((org.springframework.data.domain.Pageable) allStyles).getContent());
            */

            return mav;
        }


        Outing existingOuting = outingService.findOutingById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        // Vérification id
        if (!rightsChecker.isModificationPermitted(principal.getName(),existingOuting.getId())) {
            redirectAttributes.addFlashAttribute("error", "Vous ne possdez pas l'authorisation requise");
            return new ModelAndView("redirect:/outings/");
        }

        try {
            existingOuting.setName(updateDto.getName());
            existingOuting.setDescription(updateDto.getDescription());
            existingOuting.setDate(updateDto.getDate());
            existingOuting.setWebsite(updateDto.getWebsite());
            if (updateDto.getCategoryId() != null) {
                categoryService.findCategoryById(updateDto.getCategoryId())
                        .ifPresent(existingOuting::setCategory);
            }

            outingService.updateOuting(existingOuting);

            redirectAttributes.addFlashAttribute("success", "Sortie mise à jour avec succès !");
            return new ModelAndView("redirect:/outings/" + id);

        } catch (Exception e) {
            ModelAndView localMaV = new ModelAndView("newOutingFormScreen");
            localMaV.addObject("action", "edit");
            localMaV.addObject("outing", updateDto);
            localMaV.addObject("error", "Erreur lors de la mise à jour : " + e.getMessage());
            return localMaV;
        }
    }


    @PostMapping("/{id}/delete")
    public ModelAndView deleteOuting(@PathVariable Long id,
                                     Principal principal,
                                     RedirectAttributes redirectAttributes) {

        Outing outing = outingService.findOutingById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sortie introuvable"));
        // Vérification id
        if (!rightsChecker.isModificationPermitted(principal.getName(),outing.getId())) {
            redirectAttributes.addFlashAttribute("error", "Vous ne possdez pas l'authorisation requise");
            return new ModelAndView("redirect:/outings/");
        }

        try {
            outingService.deleteOuting(id);

            redirectAttributes.addFlashAttribute("success", "La sortie a été supprimée avec succès.");
            return new ModelAndView("redirect:/categories");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression : " + e.getMessage());
            return new ModelAndView("redirect:/outings/" + id);
        }
    }

}
