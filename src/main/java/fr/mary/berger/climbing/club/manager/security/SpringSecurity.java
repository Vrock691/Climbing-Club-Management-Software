package fr.mary.berger.climbing.club.manager.security;

import fr.mary.berger.climbing.club.manager.models.Member;
import fr.mary.berger.climbing.club.manager.services.MemberService;
import jakarta.annotation.PostConstruct;
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

import java.util.List;

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
        newMember.setAuthorities(List.of("MEMBER"));
        newMember.setEncodedPassword(passwordEncoder.encode("VMpassword"));
        memberService.createMember(newMember);

        log.info("New member created (VM)");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        String[] anonymousRequests = {
                "/",
                "/auth/**",
                "/home",
                "/search",
                "/categories",
                "/categories/**",
                "/outings/**",
                "/css/**",
                "/js/**",
                "/images/**"
        };

        String[] memberRequests = {
                "/outings/*/new",
                "/outings/*/update",
                "/outings/*/delete"
        };

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(anonymousRequests).permitAll()
                        .requestMatchers(memberRequests).hasRole("MEMBER")
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .usernameParameter("username")
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/home")
                        .permitAll()
                );

        return http.build();
    }
}