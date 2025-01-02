package com.ubb.zenith.service;

import com.ubb.zenith.dto.UserDTO;
import com.ubb.zenith.exception.UserAlreadyExistsException;
import com.ubb.zenith.exception.UserNotFoundException;
import com.ubb.zenith.model.Role;
import com.ubb.zenith.model.User;
import com.ubb.zenith.repository.RoleRepository;
import com.ubb.zenith.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpotifyAuthService spotifyAuthService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Retrieve all users.
     *
     * @return a list of all users in the database.
     */
    public List<User> getAll() {
        return userRepository.findAll();
    }

    /**
     * Add a new user to the database.
     *
     * @param userDTO the user data to be added.
     * @return the added user.
     * @throws UserAlreadyExistsException if the username already exists.
     */
    public User add(UserDTO userDTO) throws UserAlreadyExistsException {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("User with this username already exists");
        }

        Role role = roleRepository.findByName("USER").orElseThrow(() -> new RuntimeException("Role not found"));

        // Creează utilizatorul
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setAge(userDTO.getAge());
        user.setSpotifyAccessToken(null); // Initial, nu există token Spotify
        user.setSpotifyRefreshToken(null); // Acesta poate fi setat ulterior, în cazul în care există
        user.setSpotifyTokenExpiry(null);
        user.setRole(role);

        // Salvează utilizatorul în baza de date
        user = userRepository.save(user);

        // Verifică dacă există un refresh token existent
        if (user.getSpotifyRefreshToken() != null) {
            try {
                // Încearcă să obții un access token folosind refresh token-ul existent
                String newAccessToken = spotifyAuthService.refreshAccessToken(user.getSpotifyRefreshToken());

                // Actualizează utilizatorul cu token-ul de acces și expirarea acestuia
                user.setSpotifyAccessToken(newAccessToken);
                user.setSpotifyTokenExpiry(LocalDateTime.now().plusSeconds(3600)); // Presupunem că token-ul expira într-o oră
                userRepository.save(user);
            } catch (IOException e) {
                throw new RuntimeException("Failed to refresh Spotify access token", e);
            }
        }

        return user;
    }



    /**
     * Update an existing user.
     *
     * @param username the username of the user to be updated.
     * @param userDTO  the new user data.
     * @return the updated user.
     * @throws UserNotFoundException if the user does not exist.
     */
    public User update(String username, UserDTO userDTO) throws UserNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setAge(userDTO.getAge());

        return userRepository.save(user);
    }

    /**
     * Delete a user from the database.
     *
     * @param username the username of the user to be deleted.
     * @throws UserNotFoundException if the user does not exist.
     */
    public void delete(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.delete(user);
    }

    /**
     * Save Spotify tokens for a user.
     *
     * @param username     the username of the user.
     * @param accessToken  the Spotify access token.
     * @param refreshToken the Spotify refresh token.
     * @param expiresIn    the token expiry time in seconds.
     */
    public void saveTokens(String username, String accessToken, String refreshToken, int expiresIn) throws UserNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setSpotifyAccessToken(accessToken);
        user.setSpotifyRefreshToken(refreshToken);
        user.setSpotifyTokenExpiry(LocalDateTime.now().plusSeconds(expiresIn));

        userRepository.save(user);
    }

    /**
     * Get the Spotify access token for a user. Refresh it if it has expired.
     *
     * @param username the username of the user.
     * @return the Spotify access token.
     * @throws UserNotFoundException if the user does not exist.
     */
    // În UserService.java
    public String getAccessToken(String username) throws UserNotFoundException {
        // Căutăm utilizatorul în baza de date
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Dacă refresh token-ul lipsește, utilizatorul trebuie să se autentifice din nou
        if (user.getSpotifyRefreshToken() == null) {
            throw new IllegalStateException("Refresh token is missing. User must authenticate with Spotify again.");
        }

        // În caz contrar, folosim refresh token-ul pentru a obține un nou access token
        try {
            // Înlocuim access token-ul folosind refresh token-ul
            String accessToken = spotifyAuthService.refreshAccessToken(user.getSpotifyRefreshToken());

            // Actualizăm utilizatorul cu noul access token
            user.setSpotifyAccessToken(accessToken);

            // Salvăm utilizatorul cu noul token
            userRepository.save(user);

            return accessToken; // Returnăm noul access token
        } catch (IOException e) {
            // În caz de eroare la obținerea access token-ului, aruncăm o excepție
            throw new RuntimeException("Failed to refresh access token", e);
        }
    }



    public User findByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
