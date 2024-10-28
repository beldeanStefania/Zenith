package com.ubb.zenith.service;

import com.ubb.zenith.dto.MoodDTO;
import com.ubb.zenith.dto.UserPlaylistDTO;
import com.ubb.zenith.exception.PlaylistAlreadyExistsException;
import com.ubb.zenith.exception.UserPlaylistAlreadyExistsException;
import com.ubb.zenith.exception.UserPlaylistNotFoundException;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserPlaylistService {
    @Autowired
    private UserPlaylistRepository userPlaylistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private MoodRepository moodRepository;

    /**
     * Retrieves all user playlists from the repository.
     *
     * @return a list of all available user playlists.
     */
    public List<UserPlaylist> getAll() {
        return userPlaylistRepository.findAll();
    }

    public UserPlaylist createUserPlaylist(UserPlaylistDTO userPlaylistDTO) throws UserPlaylistNotFoundException, UserPlaylistAlreadyExistsException {
        if (isPlaylistAssignedToUser(userPlaylistDTO.getUsername(), userPlaylistDTO.getPlaylistName())) {
            throw new UserPlaylistAlreadyExistsException("Playlist is already assigned to the user");
        }

        Optional<User> user = userRepository.findByUsername(userPlaylistDTO.getUsername());
        Optional<Playlist> playlist = playlistRepository.findByName(userPlaylistDTO.getPlaylistName());

        if (user.isPresent() && playlist.isPresent()) {
            // Construirea obiectului UserPlaylist
            UserPlaylist userPlaylist = new UserPlaylist();
            userPlaylist.setUser(user.get());
            userPlaylist.setPlaylist(playlist.get());
            userPlaylist.setDate(userPlaylistDTO.getDate());

            // Salvarea UserPlaylist în baza de date
            return userPlaylistRepository.save(userPlaylist);
        } else {
            throw new UserPlaylistNotFoundException("User or Playlist not found");
        }
    }

    public void deleteUserPlaylist(String username, String playlistName) throws UserPlaylistNotFoundException {
        if (!isPlaylistAssignedToUser(username, playlistName)) {
            throw new UserPlaylistNotFoundException("Playlist is not assigned to the user");
        }

        Optional<UserPlaylist> userPlaylist = userPlaylistRepository.findByUserUsernameAndPlaylistName(username, playlistName);
        userPlaylist.ifPresent(userPlaylistRepository::delete);
    }

    public boolean isPlaylistAssignedToUser(String username, String playlistName) {
        return userPlaylistRepository.findByUserUsernameAndPlaylistName(username, playlistName).isPresent();
    }

}
