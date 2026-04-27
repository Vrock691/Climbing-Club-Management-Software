package fr.mary.berger.climbing.club.manager.security;

import fr.mary.berger.climbing.club.manager.models.Member;
import fr.mary.berger.climbing.club.manager.services.MemberService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.DispatcherType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SpringSecurity {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> {
            web.ignoring().requestMatchers("/webjars/**");
        };
    }

    @PostConstruct
    public void init() {
        Member newMember = new Member();
        newMember.setUsername("JLM");
        newMember.setFirstName("Massat");
        newMember.setLastName("Jean-Luc");
        newMember.setEmail("jean-luc.massat@univ-amu.fr");
        newMember.setEncodedPassword(passwordEncoder.encode("JLMpassword"));
        memberService.createMember(newMember);

        log.info("New member created (JLM)");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {

        String[] anonymousRequests = {
                "/",
                "/auth/**",
                "/home",
                "/search",
                "/search/**",
                "/categories",
                "/categories/**",
                "/outings/*",
                "/css/**",
                "/js/**",
                "/images/**",
                "/error",
                "/error/**"
        };

        String[] authenticatedRequests = {
                "/outings/new",
                "/outings/*/update",
                "/outings/*/delete"
        };

        http.authorizeHttpRequests(config -> {
            config.requestMatchers(authenticatedRequests).authenticated();
            config.requestMatchers(anonymousRequests).permitAll();
            config.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll();
            config.anyRequest().authenticated();
        });

        http.formLogin(config -> {      
            config.loginPage("/auth/login");
            config.usernameParameter("username");
            config.passwordParameter("password");
            config.defaultSuccessUrl("/");
            config.permitAll();
        });

        http.logout(config -> {
            config.permitAll();
            config.logoutSuccessUrl("/");
        });

        http.csrf(config -> {
            config.ignoringRequestMatchers(anonymousRequests);
        });

        return http.build();
    }
}