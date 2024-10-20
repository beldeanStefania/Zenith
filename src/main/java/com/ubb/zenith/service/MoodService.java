package com.ubb.zenith.service;

import com.ubb.zenith.dto.MoodDTO;
import com.ubb.zenith.exception.MoodAlreadyExistsExcetion;
import com.ubb.zenith.exception.MoodNotFoundException;
import com.ubb.zenith.exception.UserNotFoundException;
import com.ubb.zenith.model.Mood;
import com.ubb.zenith.repository.MoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MoodService {
    @Autowired
    private MoodRepository moodRepository;

    /**
     *
     * @return a list of moods
     */
    public List<Mood> getAll() {
        return moodRepository.findAll();
    }

    /**
     * Checks if the mood already exists, and if it does, throws an exception.
     * @param moodDTO
     * @throws MoodAlreadyExistsExcetion if the mood already exists
     */
    public void checkIfMoodAlreadyExists(final MoodDTO moodDTO) throws MoodAlreadyExistsExcetion {
        if (FindByHappiness_scoreSadness_scoreLove_scoreEnergy_Score(moodDTO.getHappiness_score(), moodDTO.getLove_score(), moodDTO.getSadness_score(), moodDTO.getEnergy_score()) == true) {
            throw new MoodAlreadyExistsExcetion("Mood already exists");
        }
    }

    /**
     * Checks if the mood exists, and if not, saves the created mood in the build method.
     * @param mood
     * @return the new mood after it is saved in the build method if it does not exist
     * @throws MoodAlreadyExistsExcetion if the mood already exists
     */
    public Mood add(final MoodDTO mood) throws MoodAlreadyExistsExcetion {
        checkIfMoodAlreadyExists(mood);
        return moodRepository.save(buildMood(mood));
    }

    /**
     * Creates a new mood with the attributes from the DTO and assigns an empty list of songs.
     * @param moodDTO
     * @return the new build mood
     */
    public Mood buildMood(MoodDTO moodDTO) {
        // Function that builds a Mood object
        var mood = new Mood();
        mood.setEnergy_score(moodDTO.getEnergy_score());
        mood.setSadness_score(moodDTO.getSadness_score());
        mood.setHappiness_score(moodDTO.getHappiness_score());
        mood.setLove_score(moodDTO.getLove_score());
        mood.setSongs(new ArrayList<>());
        return mood;
    }

    /**
     * Saves a new mood.
     * @param mood
     * @return the new mood after it is saved
     */
    public Mood add(Mood mood) {
        return moodRepository.save(mood);
    }

    /**
     * Deletes a mood using the provided parameters to find the desired entity.
     * @param happiness_score
     * @param love_score
     * @param sadness_score
     * @param energy_score
     * @throws MoodNotFoundException if the mood is not found
     */
    public void deleteMood(final Integer happiness_score, final Integer love_score, final Integer sadness_score, final Integer energy_score) throws MoodNotFoundException {
        moodRepository.delete(findMood(happiness_score, love_score, sadness_score, energy_score));
    }

    /**
     * Updates all fields of a mood.
     * @param mood
     * @param newhappiness_score
     * @param newlove_score
     * @param newsadness_score
     * @param newenergy_score
     */
    public void updateMood(final MoodDTO mood, final Integer newhappiness_score, final Integer newlove_score, final Integer newsadness_score, final Integer newenergy_score) {
        mood.setHappiness_score(newhappiness_score);
        mood.setLove_score(newlove_score);
        mood.setSadness_score(newsadness_score);
        mood.setEnergy_score(newenergy_score);
        moodRepository.save(buildMood(mood));
    }

    /**
     * Finds a mood using the provided scores.
     * @param happiness_score
     * @param love_score
     * @param sadness_score
     * @param energy_score
     * @return the mood after the wish filters are applied
     * @throws MoodNotFoundException if the mood is not found
     */
    public Mood findMood(final Integer happiness_score, final Integer love_score, final Integer sadness_score, final Integer energy_score) throws MoodNotFoundException {
        return moodRepository.findAll().stream()
                .filter(mood -> mood.getHappiness_score().equals(happiness_score) && mood.getLove_score().equals(love_score) && mood.getSadness_score().equals(sadness_score) && mood.getEnergy_score().equals(energy_score))
                .findFirst().orElseThrow(() -> new MoodNotFoundException("Mood not found"));
    }

    /**
     * Checks if the mood exists or not.
     * @param happiness_score
     * @param love_score
     * @param sadness_score
     * @param energy_score
     * @return false if it exists, and true if it does not exist
     */
    public boolean FindByHappiness_scoreSadness_scoreLove_scoreEnergy_Score(final Integer happiness_score, final Integer love_score, final Integer sadness_score, final Integer energy_score) {
        List<Mood> mood = getAll();
        for (int i = 0; i < mood.size(); i++) {
            if (mood.get(i).getHappiness_score() == happiness_score && mood.get(i).getSadness_score() == sadness_score && mood.get(i).getLove_score() == love_score && mood.get(i).getEnergy_score() == energy_score)
                return false;
        }
        return true;
    }

}

