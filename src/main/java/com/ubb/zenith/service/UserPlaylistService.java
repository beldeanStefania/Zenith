package com.ubb.zenith.service;

import com.ubb.zenith.exception.PlaylistNotFoundException;
import com.ubb.zenith.exception.UserNotFoundException;
import com.ubb.zenith.model.User;
import com.ubb.zenith.model.Playlist;
import com.ubb.zenith.model.UserPlaylist;
import com.ubb.zenith.repository.UserPlaylistRepository;
import com.ubb.zenith.repository.UserRepository;
import com.ubb.zenith.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserPlaylistService {

    @Autowired
    private UserPlaylistRepository userPlaylistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    /**
     * Retrieves all user playlists from the repository.
     *
     * @return a list of all user playlists.
     */
    public List<UserPlaylist> getAll() {
        return userPlaylistRepository.findAll();
    }

    /**
     * Adds a new user playlist.
     *
     * @param userId the ID of the user.
     * @param playlistId the ID of the playlist.
     * @param date the date when the playlist was added to the user.
     * @return the added user playlist.
     * @throws UserNotFoundException if the user is not found.
     * @throws PlaylistNotFoundException if the playlist is not found.
     */
    public UserPlaylist add(Integer userId, Integer playlistId, Date date) throws UserNotFoundException, PlaylistNotFoundException {
        User user = findUser(userId);
        Playlist playlist = findPlaylist(playlistId);

        UserPlaylist userPlaylist = new UserPlaylist(new UserPlaylist.UserPlaylistsId(userId, playlistId), user, playlist, date);
        return userPlaylistRepository.save(userPlaylist);
    }

    /**
     * Updates an existing user playlist.
     *
     * @param userId the ID of the user.
     * @param playlistId the ID of the playlist.
     * @param date the new date for the user playlist.
     * @return the updated user playlist.
     * @throws UserNotFoundException if the user is not found.
     * @throws PlaylistNotFoundException if the playlist is not found.
     */
    public UserPlaylist update(Integer userId, Integer playlistId, Date date) throws UserNotFoundException, PlaylistNotFoundException {
        UserPlaylist userPlaylist = findUserPlaylist(userId, playlistId);
        userPlaylist.setDate(date);
        return userPlaylistRepository.save(userPlaylist);
    }

    /**
     * Finds a user playlist by user ID and playlist ID.
     *
     * @param userId the ID of the user.
     * @param playlistId the ID of the playlist.
     * @return the found user playlist.
     * @throws UserNotFoundException if the user is not found.
     * @throws PlaylistNotFoundException if the playlist is not found.
     */
    public UserPlaylist findUserPlaylist(Integer userId, Integer playlistId) throws UserNotFoundException, PlaylistNotFoundException {
        return userPlaylistRepository.findByUserIdAndPlaylistId(userId, playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException("UserPlaylist not found"));
    }

    /**
     * Deletes an existing user playlist.
     *
     * @param userId the ID of the user.
     * @param playlistId the ID of the playlist.
     * @throws UserNotFoundException if the user is not found.
     * @throws PlaylistNotFoundException if the playlist is not found.
     */
    public void delete(Integer userId, Integer playlistId) throws UserNotFoundException, PlaylistNotFoundException {
        UserPlaylist userPlaylist = findUserPlaylist(userId, playlistId);
        userPlaylistRepository.delete(userPlaylist);
    }

    /**
     * Finds a user by ID.
     *
     * @param userId the ID of the user.
     * @return the found user.
     * @throws UserNotFoundException if the user is not found.
     */
    private User findUser(Integer userId) throws UserNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    /**
     * Finds a playlist by ID.
     *
     * @param playlistId the ID of the playlist.
     * @return the found playlist.
     * @throws PlaylistNotFoundException if the playlist is not found.
     */
    private Playlist findPlaylist(Integer playlistId) throws PlaylistNotFoundException {
        return playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist not found"));
    }
}
