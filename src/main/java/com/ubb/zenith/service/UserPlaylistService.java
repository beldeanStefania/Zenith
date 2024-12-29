package com.ubb.zenith.service;

import com.ubb.zenith.exception.PlaylistAlreadyExistsException;
import com.ubb.zenith.exception.UserNotFoundException;
import com.ubb.zenith.model.Playlist;
import com.ubb.zenith.model.User;
import com.ubb.zenith.model.UserPlaylist;
import com.ubb.zenith.repository.PlaylistRepository;
import com.ubb.zenith.repository.UserPlaylistRepository;
import com.ubb.zenith.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class UserPlaylistService {

    @Autowired
    private UserPlaylistRepository userPlaylistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private SpotifyApiService spotifyApiService;

    @Autowired
    private UserService userService;

    /**
     * Retrieves all playlists for users.
     *
     * @return List of all user playlists.
     */
    public List<UserPlaylist> getAll() {
        return userPlaylistRepository.findAll();
    }

    /**
     * Generates a playlist for a user based on their mood scores and saves it to Spotify.
     *
     * @param username the username of the user.
     * @param happinessScore the happiness score of the user.
     * @param sadnessScore the sadness score of the user.
     * @param loveScore the love score of the user.
     * @param energyScore the energy score of the user.
     * @param playlistName the name of the playlist to be created.
     * @return the created UserPlaylist object.
     * @throws PlaylistAlreadyExistsException if a playlist with the same name already exists.
     * @throws UserNotFoundException if the user does not exist.
     */
    @Transactional
    public UserPlaylist generatePlaylistForUser(
            String username,
            Integer happinessScore,
            Integer sadnessScore,
            Integer loveScore,
            Integer energyScore,
            String playlistName) throws PlaylistAlreadyExistsException, UserNotFoundException {

        // Check if playlist already exists
        if (checkIfPlaylistAlreadyExists(playlistName)) {
            throw new PlaylistAlreadyExistsException("Playlist with this name already exists.");
        }

        try {
            // Get access token for Spotify API
            String accessToken = userService.getAccessToken(username);

            // Generate search query based on mood scores
            String query = generateQueryBasedOnMood(happinessScore, sadnessScore, loveScore, energyScore);

            // Search for tracks on Spotify
            List<String> trackUris = spotifyApiService.searchTracks(query, accessToken);

            // Get Spotify user ID
            String userId = spotifyApiService.getCurrentUserId(accessToken);

            // Create Spotify playlist and get the playlist ID
            String playlistId = spotifyApiService.createPlaylist(userId, playlistName, "Generated based on mood", false, accessToken);

            // Add tracks to the created playlist
            spotifyApiService.addTracksToPlaylist(playlistId, trackUris, accessToken);

            // Save playlist in database
            Playlist playlist = new Playlist();
            playlist.setName(playlistName);
            playlistRepository.save(playlist);

            // Link playlist to the user
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UserNotFoundException("User not found."));

            UserPlaylist userPlaylist = new UserPlaylist();
            userPlaylist.setPlaylist(playlist);
            userPlaylist.setUser(user);
            userPlaylist.setDate(java.time.LocalDate.now());

            return userPlaylistRepository.save(userPlaylist);

        } catch (IOException e) {
            throw new RuntimeException("Failed to interact with Spotify API", e);
        }
    }


    /**
     * Checks if a playlist with the given name already exists.
     *
     * @param name the name of the playlist.
     * @return true if a playlist with the given name exists, false otherwise.
     */
    private boolean checkIfPlaylistAlreadyExists(String name) {
        return playlistRepository.findByName(name).isPresent();
    }

    /**
     * Generates a search query for Spotify based on mood scores.
     *
     * @param happinessScore the happiness score.
     * @param energyScore the energy score.
     * @return the generated search query.
     */

    private String generateQueryBasedOnMood(int happinessScore, int sadnessScore, int loveScore, int energyScore) {
        if (happinessScore > 8 && energyScore > 8) {
            return "happy upbeat";
        } else if (sadnessScore > 7) {
            return "sad emotional";
        } else if (loveScore > 7) {
            return "romantic ballad";
        } else {
            return "relaxing chill";
        }
    }

//    private String generateQueryBasedOnMood(int happinessScore, int sadnessScore, int loveScore, int energyScore) {
//        if (happinessScore > 7 && energyScore > 7) {
//            return "happy upbeat";
//        } else if (sadnessScore > 7) {
//            return "sad emotional";
//        } else if (loveScore > 7) {
//            return "romantic ballad";
//        } else if (energyScore > 7) {
//            return "energetic workout";
//        } else {
//            return "calm relaxing";
//        }
//    }

}
