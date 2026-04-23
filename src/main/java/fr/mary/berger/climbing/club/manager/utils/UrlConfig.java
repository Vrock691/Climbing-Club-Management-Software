package fr.mary.berger.climbing.club.manager.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class UrlConfig {

    @Value("${app.base-url}")
    private String baseUrl;

}
