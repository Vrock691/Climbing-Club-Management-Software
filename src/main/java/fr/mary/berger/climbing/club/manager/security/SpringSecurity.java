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
        newMember.setUsername("VM");
        newMember.setFirstName("Mary");
        newMember.setLastName("Valentin");
        newMember.setEmail("valentin.mary@proton.me");
        newMember.setEncodedPassword(passwordEncoder.encode("VMpassword"));
        memberService.createMember(newMember);

        log.info("New member created (VM)");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {

        String[] anonymousRequests = {
                "/",
                "/auth/**",
                "/home",
                "/search",
                "/categories",
                "/categories/**",
                "/outings/*",
                "/css/**",
                "/js/**",
                "/images/**"
        };

        String[] authenticatedRequests = {
                "/outings/new",
                "/outings/*/update",
                "/outings/*/delete"
        };

        http.authorizeHttpRequests(config -> {
            config.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll();
            config.requestMatchers(anonymousRequests).permitAll();
            config.requestMatchers(authenticatedRequests).authenticated();
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