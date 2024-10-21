package com.ubb.zenith.repository;

import com.ubb.zenith.model.Mood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MoodRepository extends JpaRepository<Mood, Integer> {

    Optional<Mood> findById(Integer moodId);
}
