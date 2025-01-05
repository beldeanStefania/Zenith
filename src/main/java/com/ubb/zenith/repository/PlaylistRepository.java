package com.ubb.zenith.repository;

import com.ubb.zenith.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
    List<Playlist> findByUserId(Integer userId);
    List<Playlist> findByUserUsername(String username);
}

