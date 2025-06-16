package com.app.my_project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.my_project.entity.UserEntity;
import com.app.my_project.repository.UserRepository;

import java.util.List;
import java.util.Date;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import io.github.cdimascio.dotenv.Dotenv;

@RestController
@RequestMapping("/api/users")
public class UserApiController {
    private final UserRepository userRepository;
    private static final long EXPIRATION_TIME = 60 * 60 * 1000 * 24 * 7; // 1 week

    public UserApiController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<UserEntity> getAlUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    public UserEntity createUser(@RequestBody UserEntity user) {
        return userRepository.save(user);
    }

    @GetMapping("/{id}")
    public UserEntity getUserById(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public UserEntity updateUser(@PathVariable Long id, @RequestBody UserEntity user) {
        UserEntity userToUpdate = userRepository.findById(id).orElse(null);

        if (userToUpdate == null) {
            throw new IllegalArgumentException("User not found");
        }

        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setEmail(user.getEmail());

        return userRepository.save(userToUpdate);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

    @PostMapping("/signin")
    public UserEntity signin(@RequestBody UserEntity user) {
        String username = user.getUsername();
        String email = user.getEmail();

        UserEntity userToSignin = userRepository.findByUsernameAndEmail(username, email);

        if (userToSignin == null) {
            throw new IllegalArgumentException("User not found");
        }

        return userToSignin;
    }

    private String getSecret() {
        Dotenv dotenv = Dotenv.configure()
                .directory(System.getProperty("user.dir") + "/my-project")
                .load();
        return dotenv.get("JWT_SECRET");
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(getSecret());
    }

    @PostMapping("/admin-signin")
    public Object adminSigin(@RequestBody UserEntity user) {
        try {
            String u = user.getUsername();
            String p = user.getPassword();

            UserEntity userForCreateToken = userRepository.findByUsernameAndPassword(u, p);

            String token = JWT.create()
                    .withSubject(String.valueOf(userForCreateToken.getId()))
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .withIssuedAt(new Date())
                    .sign(getAlgorithm());
            String role = userForCreateToken.getRole();
            record UserResponse(String token, String role) {
            }
            return new UserResponse(token, role);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error creating token");
        }
    }

    @GetMapping("/admin-info")
    public Object adminInfo(@RequestHeader("Authorization") String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                throw new IllegalArgumentException("Invalid token format with 'Bearer '");
            }

            String tokenWithoutBearer = token.replace("Bearer ", "");

            if (tokenWithoutBearer.trim().isEmpty()) {
                throw new IllegalArgumentException("Token is empty");
            }

            String subject = JWT.require(getAlgorithm())
                    .build()
                    .verify(tokenWithoutBearer)
                    .getSubject();

            Long userId = Long.valueOf(subject);
            UserEntity user = userRepository.findById(userId).orElse(null);

            if (user == null)
                throw new IllegalArgumentException("user not found");

            String role = user.getRole();

            record UserResponse(Long id, String username, String email, String role) {
            }

            return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), role);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Authorization error: " + e.getMessage());
        }
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

    @PostMapping("/admin-edit-profile")
    public UserEntity adminEditProfile(@RequestHeader("Authorization") String token, @RequestBody UserEntity user) {
        try {
            Long userId = getUserIdFromToken(token);
            UserEntity userToUpdate = userRepository.findById(userId).orElse(null);

            if (userToUpdate == null)
                throw new IllegalArgumentException("User not found");

            userToUpdate.setUsername(user.getUsername());
            userToUpdate.setEmail(user.getEmail());

            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                userToUpdate.setPassword(user.getPassword());
            }

            userRepository.save(userToUpdate);

            return userToUpdate;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("update user error: " + e.getMessage());
        }
    }

    @PostMapping("/admin-update-profile")
    public UserEntity adminUpdateProfile(@RequestHeader("Authorization") String token, @RequestBody UserEntity user) {
        try {
            UserEntity userToUpdate = userRepository.findById(user.getId()).orElse(null);

            if (userToUpdate == null)
                throw new IllegalArgumentException("User not found");

            userToUpdate.setUsername(user.getUsername());
            userToUpdate.setEmail(user.getEmail());

            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                userToUpdate.setPassword(user.getPassword());
            }

            userToUpdate.setRole(user.getRole());

            userRepository.save(userToUpdate);

            return userToUpdate;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("update user error: " + e.getMessage());
        }
    }

    @PostMapping("/admin-create")
    public UserEntity adminCreate(
            @RequestHeader("Authorization") String token,
            @RequestBody UserEntity user) {
        try {
            userRepository.save(user);
            return user;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Authentication error " + e.getMessage());
        }
    }

    @DeleteMapping("/admin-delete/{id}")
    public void adminDelete(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        try {
            UserEntity userToDelete = userRepository.findById(id).orElse(null);

            if (userToDelete == null)
                throw new IllegalArgumentException("User not found");

            userRepository.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Authentication error " + e.getMessage());
        }
    }

}
