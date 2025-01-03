package com.ubb.zenith.controller;

import com.ubb.zenith.dto.UserDTO;
import com.ubb.zenith.exception.UserAlreadyExistsException;
import com.ubb.zenith.exception.UserNotFoundException;
import com.ubb.zenith.model.User;
import com.ubb.zenith.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Retrieves all user entries from the repository.
     *
     * @return a list of all users in the database
     */
    @GetMapping("/getAll")
    public List<User> getAll() {
        return userService.getAll();
    }

    /**
     * Adds a new user after verifying if a user with the same username already exists.
     *
     * @param userDTO DTO object that contains the information of the user to be added.
     * @return the added user.
     * @throws UserAlreadyExistsException if a user with the same username already exists.
     */
    @PostMapping("/add")
    public ResponseEntity<User> add(@Valid @RequestBody UserDTO userDTO) {
        try {
            return ok(userService.add(userDTO));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(409).body(null); // Conflict status code for existing user
        }
    }

    /**
     * Updates a user entry in the repository.
     *
     * @param username the username of the user to be updated.
     * @param userDTO  DTO object that contains the information of the user to be updated.
     * @return the updated user.
     */
    @PutMapping("/update/{username}")
    public ResponseEntity<User> update(@PathVariable String username, @RequestBody UserDTO userDTO) {
        try {
            return ok(userService.update(username, userDTO));
        } catch (UserNotFoundException e) {
            return notFound().build();
        }
    }

    /**
     * Deletes a user entry from the repository.
     *
     * @param username the username of the user to be deleted.
     * @return HTTP response indicating success or failure.
     */
    @DeleteMapping("/delete/{username}")
    public ResponseEntity<Void> delete(@PathVariable String username) {
        try {
            userService.delete(username);
            return ResponseEntity.noContent().build(); // No Content status code for successful deletion
        } catch (UserNotFoundException e) {
            return notFound().build();
        }
    }

    /**
     * Saves Spotify tokens for a user after authentication.
     *
     * @param username      the username of the user.
     * @param accessToken   the Spotify access token.
     * @param refreshToken  the Spotify refresh token.
     * @param expiresIn     the expiration time of the access token in seconds.
     * @return HTTP response indicating success or failure.
     */
    @PostMapping("/saveSpotifyTokens/{username}")
    public ResponseEntity<String> saveSpotifyTokens(
            @PathVariable String username,
            @RequestParam String accessToken,
            @RequestParam String refreshToken,
            @RequestParam int expiresIn) throws UserNotFoundException {
        userService.saveTokens(username, accessToken, refreshToken, expiresIn);
        return ok("Spotify tokens saved successfully for " + username);
    }

    /**
     * Retrieves the Spotify access token for a user. Refreshes it if expired.
     *
     * @param username the username of the user.
     * @return the Spotify access token.
     */
    @GetMapping("/getSpotifyAccessToken/{username}")
    public ResponseEntity<String> getSpotifyAccessToken(@PathVariable String username) {
        try {
            String accessToken = userService.getAccessToken(username);
            return ok(accessToken);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to retrieve access token: " + e.getMessage());
        }
    }
}
