package org.alvl.nix.java.telegamusicbot.app;

import org.alvl.nix.java.telegamusicbot.components.TelegaMusicBot;
import org.alvl.nix.java.telegamusicbot.repository.SongRepository;
import org.alvl.nix.java.telegamusicbot.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.telegram.telegrambots.ApiContextInitializer;

@ComponentScan(basePackages = {"org.alvl.nix.java.telegamusicbot.*","org.alvl.nix.java.telegamusicbot.model"})
@EnableJpaRepositories(basePackageClasses = {UserRepository.class, SongRepository.class})
@EntityScan(basePackages = {"org.alvl.nix.java.telegamusicbot.*"})
@SpringBootApplication
public class TelegamusicbotApplication {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(TelegamusicbotApplication.class, args);
    }
}
