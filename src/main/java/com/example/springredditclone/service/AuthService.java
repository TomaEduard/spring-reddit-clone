package com.example.springredditclone.service;

import com.example.springredditclone.controller.RegisterRequest;
import com.example.springredditclone.exception.SpringRedditException;
import com.example.springredditclone.model.User;
import com.example.springredditclone.model.VerificationToken;
import com.example.springredditclone.repository.UserRepository;
import com.example.springredditclone.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.example.springredditclone.utils.Constants.ACTIVATION_EMAIL;

@Service
@Slf4j
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final MailContentBuilder mailContentBuilder;

    @Transactional
    public void signup(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEnabled(false);

        userRepository.save(user);
        log.info("User Registered Successfully, Sending Authentication Email");

        String token = generateVerificationToken(user);
        String message = mailContentBuilder.build(
                "Thank you for signing up to Spring Reddit, please click on the below url to activate your account : "
                        + "http://localhost:8085/api/auth/accountVerification/" + token);

//        mailService.sendMail(new NotificationEmail("Please activate your Account",
//                user.getEmail(), "Thank you for signing up to Spring Reddit, " +
//                "http://localhost:8080/api/auth/accountVerification/" + token));

        mailService.sendMail(user.getEmail(), message);
        log.info("Activation email sent!");
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token"));
        fetchUserAndEnable(verificationToken.get());
    }

    @Transactional
    void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new SpringRedditException("User not foundwith name - " + username)
        );
        user.setEnabled(true);
        userRepository.save(user);
    }
}
