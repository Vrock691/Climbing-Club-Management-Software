package fr.mary.berger.climbing.club.manager.controllers;

import fr.mary.berger.climbing.club.manager.dto.HomePageDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomePageController {

    @GetMapping("/home")
    public ModelAndView homepage(HttpServletRequest request,
                                 HttpServletResponse response) {
        HomePageDTO homePageDTO = new HomePageDTO();
        return new ModelAndView("homepage", "dto", homePageDTO);
    }

}
