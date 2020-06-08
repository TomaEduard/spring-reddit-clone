package com.example.springredditclone;

import com.example.springredditclone.config.SwaggerConfiguration;
import com.example.springredditclone.dto.RegisterRequest;
import com.example.springredditclone.dto.SubredditDto;
import com.example.springredditclone.model.User;
import com.example.springredditclone.repository.UserRepository;
import com.example.springredditclone.service.AuthService;
import com.example.springredditclone.service.SubredditService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.stream.Stream;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfiguration.class)
public class SpringRedditCloneApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringRedditCloneApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(AuthService authService, SubredditService subredditService) {
        return (args) -> {

            RegisterRequest user = new RegisterRequest();
            user.setUsername("1asd");
            user.setEmail("1asd@gmail.com");
            user.setPassword("1asd");
            authService.signupWithoutEmailVerification(user);

            RegisterRequest user1 = new RegisterRequest();
            user1.setUsername("Gigel");
            user1.setEmail("Gigel@gmail.com");
            user1.setPassword("1asd");
            authService.signupWithoutEmailVerification(user1);

            RegisterRequest user2 = new RegisterRequest();
            user2.setUsername("Dorel");
            user2.setEmail("dorel@gmail.com");
            user2.setPassword("1asd");
            authService.signupWithoutEmailVerification(user2);

            RegisterRequest user3 = new RegisterRequest();
            user3.setUsername("Ana");
            user3.setEmail("ana@gmail.com");
            user3.setPassword("1asd");
            authService.signupWithoutEmailVerification(user3);

        };
    }

}
