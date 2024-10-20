package com.ubb.zenith.service;

import com.ubb.zenith.dto.MoodDTO;
import com.ubb.zenith.exception.MoodAlreadyExistsExcetion;
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
     * @return o lista de mood-uri
     */
    public List<Mood> getAll() {
        return moodRepository.findAll();
    }

    /**
     * verifica daca exista mood-ul daca se intampla acest lucru arunca o exceptie
     * @param moodDTO
     * @throws MoodAlreadyExistsExcetion
     */
    public void checkIfMoodAlreadyExists(final MoodDTO moodDTO) throws MoodAlreadyExistsExcetion {
        if (FindByHappiness_scoreSadness_scoreLove_scoreEnergy_Score(moodDTO.getHappiness_score(), moodDTO.getLove_score() , moodDTO.getSadness_score(),moodDTO.getEnergy_score())==true) {
            throw new MoodAlreadyExistsExcetion("Mood already exists");
        }
    }

    /**
     * verifica daca exista si daca nu salveaza mood-ul creat in functia build
     * @param mood
     * @return noul mood
     * @throws MoodAlreadyExistsExcetion
     */
    public Mood add(final MoodDTO mood) throws MoodAlreadyExistsExcetion {
        checkIfMoodAlreadyExists(mood);
        return moodRepository.save(buildMood(mood)); }

    /**
     * creaza noul mood cu atributele din DTO si atribuie o lista goala de cantece
     * @param moodDTO
     * @return
     */
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

    /**
     * salveaza un mood nou
     * @param mood
     * @return
     */
    public Mood add(Mood mood){return moodRepository.save(mood);}

    /**
     * sterge folosindu-se de parametri pentru a gasi entitatea dorita
     * @param happiness_score
     * @param love_score
     * @param sadness_score
     * @param energy_score
     * @throws UserNotFoundException
     */
    public void deleteMood(final Integer happiness_score,final Integer love_score,final Integer sadness_score,final Integer energy_score) throws UserNotFoundException {
        moodRepository.delete(findMood(happiness_score,love_score,sadness_score,energy_score));
    }

    /**
     * modifica toate campurile dintr-un song
     * @param mood
     * @param newhappiness_score
     * @param newlove_score
     * @param newsadness_score
     * @param newenergy_score
     */
    public void updateMood(final MoodDTO mood, final Integer newhappiness_score, final Integer newlove_score, final Integer newsadness_score, final Integer newenergy_score) {
        mood.setHappiness_score(newhappiness_score); mood.setLove_score(newlove_score); mood.setSadness_score(newsadness_score); mood.setEnergy_score(newenergy_score);
        moodRepository.save(buildMood(mood));
    }

    /**
     * cauta mood ul folosindu-se de scorurile oferite
     * @param happiness_score
     * @param love_score
     * @param sadness_score
     * @param energy_score
     * @return
     * @throws UserNotFoundException
     */
    public Mood findMood(final Integer happiness_score,final Integer love_score,final Integer sadness_score,final Integer energy_score) throws UserNotFoundException {
        return moodRepository.findAll().stream()
                .filter(mood -> mood.getHappiness_score().equals(happiness_score) && mood.getLove_score().equals(love_score) && mood.getSadness_score().equals(sadness_score) && mood.getEnergy_score().equals(energy_score)).findFirst().orElseThrow(() -> new UserNotFoundException("Mood not found"));
    }

    /**
     * verifica daca exista sau nu
     * @param happiness_score
     * @param love_score
     * @param sadness_score
     * @param energy_score
     * @return false daca exista si true daca nu exista
     */
    public boolean FindByHappiness_scoreSadness_scoreLove_scoreEnergy_Score(final Integer happiness_score,final Integer love_score,final Integer sadness_score,final Integer energy_score){
        List<Mood> mood = getAll();
        for(int i =0;i< mood.size();i++){
            if(mood.get(i).getHappiness_score()==happiness_score && mood.get(i).getSadness_score()==sadness_score && mood.get(i).getLove_score()==love_score && mood.get(i).getEnergy_score()==energy_score)
                return false;
        }
        return true;
    }
}

