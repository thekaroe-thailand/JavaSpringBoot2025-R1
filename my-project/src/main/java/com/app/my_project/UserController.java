package com.app.my_project;

import java.util.Date;
import org.springframework.web.bind.annotation.*;

import com.app.models.TokenRequest;
import com.app.models.UserModel;
import com.auth0.*;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.*;
import com.auth0.jwt.exceptions.*;
import io.github.cdimascio.dotenv.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/jwt")
public class UserController {
    private static final long EXPIRATION_TIME = 60 * 60 * 1000 * 24; // 1 day

    public String getSecret() {
        Dotenv dotenv = Dotenv.configure()
            .directory(System.getProperty("user.dir") + "/my-project")
            .load();
        return dotenv.get("JWT_SECRET");
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(getSecret());
    }

    @PostMapping("/create")
    public String createToken(@RequestBody UserModel user) {
        UserModel userForCreateToken = new UserModel(
            user.getId(),
            user.getName(),
            user.getUsername(),
            user.getPassword()
        );

        return JWT.create()
            .withSubject(String.valueOf(userForCreateToken.getId()))
            .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .withIssuedAt(new Date())
            .sign(getAlgorithm());
    }

    @PostMapping("/check")
    public String checkToken(@RequestBody TokenRequest tokenRequest) {
        try {
            JWTVerifier verifier = JWT.require(getAlgorithm()).build();
            String id = verifier.verify(tokenRequest.getToken()).getSubject();

            return "Token is valid for user id: " + id;
        } catch (JWTVerificationException e) {
            return "Token is invalid or expired: " + e.getMessage();
        }
    }
    
}
