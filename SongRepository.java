package com.ubb.zenith.repository;

import com.ubb.zenith.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, Integer>{
    Optional<Song> findByIdMood (Integer id_mood);
}
