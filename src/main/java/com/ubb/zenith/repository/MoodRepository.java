package com.ubb.zenith.repository;

import com.ubb.zenith.model.Mood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MoodRepository extends JpaRepository<Mood, Integer> {
   // Object findByEnergy_scoreAndHappiness_scoreAndLove_scoreAndSadness_score(Integer energyScore, Integer happinessScore, Integer loveScore, Integer sadnessScore);
}
