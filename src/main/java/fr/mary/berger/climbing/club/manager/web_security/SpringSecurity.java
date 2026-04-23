package fr.mary.berger.climbing.club.manager.web_security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SpringSecurity {

    /*
    @Autowired
    XUserRepository userRepo;
     */

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> {
            web.ignoring().requestMatchers("/webjars/**");
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        String[] anonymousRequests = {
                "/",
                "/public/**",
                "/login",
                "/forgot-password",
                "/css/**",
                "/js/**",
                "/images/**"
        };

        String[] memberRequests = {
                "/member/**"
        };

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(anonymousRequests).permitAll()
                        .requestMatchers(memberRequests).hasRole("MEMBRE") // --> Resstriction pour les membres
                        .anyRequest().authenticated() // --> Securité par défaut
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/public/categories", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/public/categories")
                        .permitAll()
                );

        return http.build();
    }
}