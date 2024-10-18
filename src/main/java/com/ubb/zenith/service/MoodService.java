package com.ubb.zenith.service;

import com.ubb.zenith.exception.UserNotFoundException;
import com.ubb.zenith.model.Mood;
import com.ubb.zenith.model.User;
import com.ubb.zenith.repository.MoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoodService {

    @Autowired
    private MoodRepository moodRepository;

    public List<Mood> getAll() {
        return moodRepository.findAll();
    }
    public Mood addMood(Mood mood) { return moodRepository.save(mood); }
    public void deleteMood(final Integer happiness_score,final Integer love_score,final Integer sadness_score,final Integer energy_score) throws UserNotFoundException {
        moodRepository.delete(findMood(happiness_score,love_score,sadness_score,energy_score));
    }

    public void updateMood(final Mood mood,final Integer newhappiness_score,final Integer newlove_score,final Integer newsadness_score,final Integer newenergy_score) {
        mood.setHappiness_score(newhappiness_score); mood.setLove_score(newlove_score); mood.setSadness_score(newsadness_score); mood.setEnergy_score(newenergy_score);
        moodRepository.save(mood);
    }
    public Mood findMood(final Integer happiness_score,final Integer love_score,final Integer sadness_score,final Integer energy_score) throws UserNotFoundException {
        return moodRepository.findAll().stream()
                .filter(mood -> mood.getHappiness_score().equals(happiness_score) && mood.getLove_score().equals(love_score) && mood.getSadness_score().equals(sadness_score) && mood.getEnergy_score().equals(energy_score)).findFirst().orElseThrow(() -> new UserNotFoundException("Mood not found"));
    }

    /*
     public void deleteMood(final Integer score[4]) throws UserNotFoundException {
        moodRepository.delete(findMood(score[0],score[1],score[2],score[3]));
    }

    public void updateMood(final Mood mood,final Integer newscore[4]) {
        mood.setHappiness_score(score[0]); mood.setLove_score(score[1]); mood.setSadness_score(score[2]); mood.setEnergy_score(score[3]);
        moodRepository.save(mood);
    }
     public Mood findMood(final Integer score[4]) throws UserNotFoundException {
        return moodRepository.findAll().stream()
                .filter(mood -> mood.getHappiness_score().equals(score[0]) && mood.getLove_score().equals(score[1]) && mood.getSadness_score().equals(score[2]) && mood.getEnergy_score().equals(score[3])).findFirst().orElseThrow(() -> new UserNotFoundException("Mood not found"));
    }
     */

}
