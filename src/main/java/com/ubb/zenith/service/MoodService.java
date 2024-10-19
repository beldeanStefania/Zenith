package com.ubb.zenith.service;

import com.ubb.zenith.dto.MoodDTO;
import com.ubb.zenith.exception.UserAlreadyExistsException;
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

    public List<Mood> getAll() {
        return moodRepository.findAll();
    }

    public Mood addMood(final MoodDTO mood) throws UserAlreadyExistsException {
        return moodRepository.save(buildMood(mood)); }

    public Mood buildMood(MoodDTO moodDTO) {
        //functie care construieste un obiect de tip Mood
        var mood = new Mood();
        mood.setEnergy_score(moodDTO.getEnergy_score());
        mood.setSadness_score(moodDTO.getSadness_score());
        mood.setHappiness_score(moodDTO.getHappiness_score());
        mood.setLove_score(moodDTO.getLove_score());
        mood.setSongs(new ArrayList<>());
        return mood;
    }

    public void deleteMood(final Integer happiness_score,final Integer love_score,final Integer sadness_score,final Integer energy_score) throws UserNotFoundException {
        moodRepository.delete(findMood(happiness_score,love_score,sadness_score,energy_score));
    }

    public void updateMood(final MoodDTO mood, final Integer newhappiness_score, final Integer newlove_score, final Integer newsadness_score, final Integer newenergy_score) {
        mood.setHappiness_score(newhappiness_score); mood.setLove_score(newlove_score); mood.setSadness_score(newsadness_score); mood.setEnergy_score(newenergy_score);
        moodRepository.save(buildMood(mood));
    }
    public Mood findMood(final Integer happiness_score,final Integer love_score,final Integer sadness_score,final Integer energy_score) throws UserNotFoundException {
        return moodRepository.findAll().stream()
                .filter(mood -> mood.getHappiness_score().equals(happiness_score) && mood.getLove_score().equals(love_score) && mood.getSadness_score().equals(sadness_score) && mood.getEnergy_score().equals(energy_score)).findFirst().orElseThrow(() -> new UserNotFoundException("Mood not found"));
    }

}

