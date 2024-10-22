package com.ubb.zenith.repository;

import com.ubb.zenith.model.Playlist;
import com.ubb.zenith.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
    Optional<Playlist> findByName(String name);
    Optional<Playlist> findBySongs(Song song);
}

