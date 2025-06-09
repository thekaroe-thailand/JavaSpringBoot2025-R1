package com.app.my_project.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import io.github.cdimascio.dotenv.Dotenv;

public class UserService {
    private String getSecret() {
        String dir = System.getProperty("user.dir") + "/";
        Dotenv dotenv = Dotenv.configure()
                .directory(dir)
                .load();
        String secret = dotenv.get("JWT_SECRET");
        if (secret == null) {
            throw new RuntimeException("JWT_SECRET is not set in .env file");
        }
        return secret;
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(getSecret());
    }

    public Long getUserIdFromToken(String token) {
        String tokenWithoutBearer = token.replace("Bearer ", "");

        if (tokenWithoutBearer.trim().isEmpty()) {
            throw new IllegalArgumentException("Token is empty");
        }

        return Long.valueOf(
                JWT.require(getAlgorithm())
                        .build()
                        .verify(tokenWithoutBearer)
                        .getSubject());
    }
}
