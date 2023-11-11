package de.htwb.ai.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import de.htwb.ai.model.User;
import de.htwb.ai.repo.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.util.Base64;

@RestController
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "/auth", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> authenticateUser(@RequestBody String user) {
        JsonMapper mapper = new JsonMapper();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        User userToAuthenticate;
        try {
            userToAuthenticate = mapper.readValue(user, User.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }
        User authenticatedUser = userRepository.findByUserIdAndPassword(userToAuthenticate.getUserId(), userToAuthenticate.getPassword());
        if (authenticatedUser != null) {
            String authToken = generateToken();
            return ResponseEntity.ok().headers(headers).body(authToken);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private String generateToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] tokenBytes = new byte[24];
        secureRandom.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }
}
