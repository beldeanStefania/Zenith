package com.ubb.zenith.repository;

import com.ubb.zenith.model.UserPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPlaylistRepository extends JpaRepository<UserPlaylist, Integer> {
    Optional<UserPlaylist> findByUserUsernameAndPlaylistName(String username, String playlistName);
}
