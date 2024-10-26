package com.ubb.zenith.service;

import com.ubb.zenith.dto.MoodDTO;
import com.ubb.zenith.exception.MoodAlreadyExistsException;
import com.ubb.zenith.exception.MoodNotFoundException;
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
     *Retrieves all mood entries from the repository.
     *
     * @return a list of all moods in the database
     */
    public List<Mood> getAll() {
        return moodRepository.findAll();
    }

    /**
     * Checks if the mood already exists base on its attributes
     * If a mood with the same scores already exists, an exception is thrown.
     * @param moodDTO the DTO containing mood attributes to check for existence.
     * @throws MoodAlreadyExistsException if the mood already exists
     */
    public void checkIfMoodAlreadyExists(final MoodDTO moodDTO) throws MoodAlreadyExistsException {
        if (MoodExists(moodDTO.getHappiness_score(), moodDTO.getLove_score(), moodDTO.getSadness_score(), moodDTO.getEnergy_score()) == true) {
            throw new MoodAlreadyExistsException("Mood already exists");
        }
    }

    /**
     * Checks if a mood with the specified scores exists in the database.
     *
     * @param happiness_score the happiness score to check.
     * @param love_score the love score to check.
     * @param sadness_score the sadness score to check.
     * @param energy_score the energy score to check.
     * @return true if a mood with these scores exists, false otherwise.
     */
    public boolean MoodExists(final Integer happiness_score, final Integer love_score, final Integer sadness_score, final Integer energy_score) {
        List<Mood> mood = getAll();
        if(mood.isEmpty())
            return false;
        for (int i = 0; i < mood.size(); i++) {
            if (mood.get(i).getHappiness_score().equals(happiness_score) && mood.get(i).getSadness_score().equals(sadness_score) && mood.get(i).getLove_score().equals( love_score) && mood.get(i).getEnergy_score().equals(energy_score))
                return true;
        }
        return false;
    }

    /**
     * Adds a new mood to the database after verifying that it does not already exist.
     *
     * @param mood the MoodDTO containing the details of the mood to be added.
     * @return the newly added Mood entity.
     * @throws MoodAlreadyExistsException if a mood with the same scores already exists.
     */
    public Mood add(final MoodDTO mood) throws MoodAlreadyExistsException {
        checkIfMoodAlreadyExists(mood);
        return moodRepository.save(buildMood(mood));
    }

    /**
     * Creates a new Mood entity from a MoodDTO.
     *
     * @param moodDTO the DTO containing mood attributes.
     * @return a new Mood object populated with the provided attributes.
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
     * Saves a new mood entity directly, without checking for existence.
     *
     * @param mood the Mood object to save.
     * @return the saved Mood entity.
     */
    public Mood add(Mood mood) {
        return moodRepository.save(mood);
    }


    /**
     * Deletes a mood from the database based on its ID.
     *
     * @param moodId the ID of the mood to delete.
     * @throws MoodNotFoundException if no mood with the specified ID exists.
     */
    public void delete(final Integer moodId) throws MoodNotFoundException {
        moodRepository.delete(findMood(moodId));
    }

    /**
     * Updates an existing mood in the database with new values from a MoodDTO.
     *
     * @param moodId the ID of the mood to update.
     * @param moodDTO the DTO containing updated mood scores.
     * @return the updated Mood entity.
     * @throws MoodNotFoundException if no mood with the specified ID exists.
     */
    public Mood update(final Integer moodId, final MoodDTO moodDTO) throws MoodNotFoundException {
        Mood mood = moodRepository.findById(moodId).orElseThrow(() -> new MoodNotFoundException("Mood not found"));
        mood.setHappiness_score(moodDTO.getHappiness_score());
        mood.setLove_score(moodDTO.getLove_score());
        mood.setSadness_score(moodDTO.getSadness_score());
        mood.setEnergy_score(moodDTO.getEnergy_score());
        return moodRepository.save(mood);
    }

    /**
     * Finds a mood by its id.
     * @param moodId
     * @return
     * @throws MoodNotFoundException
     */
    public Mood findMood(final Integer moodId) throws MoodNotFoundException {
        return moodRepository.findById(moodId).orElseThrow(() -> new MoodNotFoundException("Mood not found"));
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

