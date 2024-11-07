package com.ubb.zenith.service;

import com.ubb.zenith.exception.PlaylistAlreadyExistsException;
import com.ubb.zenith.exception.PlaylistNotFoundException;
import com.ubb.zenith.exception.UserNotFoundException;
import com.ubb.zenith.model.Mood;
import com.ubb.zenith.model.Playlist;
import com.ubb.zenith.model.Song;
import com.ubb.zenith.model.User;
import com.ubb.zenith.model.UserPlaylist;
import com.ubb.zenith.repository.MoodRepository;
import com.ubb.zenith.repository.PlaylistRepository;
import com.ubb.zenith.repository.UserPlaylistRepository;
import com.ubb.zenith.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserPlaylistService {

    @Autowired
    private UserPlaylistRepository userPlaylistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MoodRepository moodRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    public List<UserPlaylist> getAll() {
        return userPlaylistRepository.findAll();
    }

    /**
     * Generates a playlist based on the user's mood scores.
     *
     * @param happinessScore the happiness score of the user.
     * @param sadnessScore the sadness score of the user.
     * @param loveScore the love score of the user.
     * @param energyScore the energy score of the user.
     * @param playlistName the name of the playlist to be generated.
     * @return the generated playlist.
     * @throws PlaylistAlreadyExistsException if a playlist with the same name already exists.
     */
    public UserPlaylist generatePlaylistForUser(String username, Integer happinessScore, Integer sadnessScore, Integer loveScore, Integer energyScore, String playlistName) throws PlaylistAlreadyExistsException, UserNotFoundException {
        checkIfPlaylistAlreadyExists(playlistName);

        List<Mood> allMoods = moodRepository.findAll();
        List<Song> matchingSongs = allMoods.stream()
                .filter(mood -> isMoodMatch(mood, happinessScore, sadnessScore, loveScore, energyScore))
                .flatMap(mood -> mood.getSongs().stream())
                .collect(Collectors.toList());

        Playlist playlist = new Playlist();
        playlist.setName(playlistName);
        playlist = playlistRepository.save(playlist);

        Playlist finalPlaylist = playlist;
        matchingSongs.forEach(song -> song.setPlaylist(finalPlaylist));

        playlist.setSongs(matchingSongs);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        UserPlaylist userPlaylist = new UserPlaylist();
        userPlaylist.setPlaylist(playlist);
        userPlaylist.setUser(user);
        userPlaylist.setDate(java.time.LocalDate.now());

        return userPlaylistRepository.save(userPlaylist);
    }


    /**
     * Checks if a playlist with a specific name exists in the repository.
     *
     * @param name the name of the playlist to be checked.
     * @throws PlaylistAlreadyExistsException if the playlist already exists.
     */
    public void checkIfPlaylistAlreadyExists(final String name) throws PlaylistAlreadyExistsException {
        if (playlistRepository.findByName(name).isPresent()) {
            throw new PlaylistAlreadyExistsException("Playlist already exists");
        }
    }

    /**
     * Checks if a mood matches the user's mood scores.
     *
     * @param mood the mood to be checked.
     * @param happinessScore the happiness score of the user.
     * @param sadnessScore the sadness score of the user.
     * @param loveScore the love score of the user.
     * @param energyScore the energy score of the user.
     * @return true if the mood matches the user's mood scores, false otherwise.
     */
    private boolean isMoodMatch(Mood mood, Integer happinessScore, Integer sadnessScore, Integer loveScore, Integer energyScore) {
        // Define a threshold for matching (e.g., +/- 2 points)
        int threshold = 2;

        return Math.abs(mood.getHappiness_score() - happinessScore) <= threshold
                && Math.abs(mood.getSadness_score() - sadnessScore) <= threshold
                && Math.abs(mood.getLove_score() - loveScore) <= threshold
                && Math.abs(mood.getEnergy_score() - energyScore) <= threshold;
    }
}
