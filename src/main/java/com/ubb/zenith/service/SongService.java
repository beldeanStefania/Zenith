package com.ubb.zenith.service;

import com.ubb.zenith.dto.SongDTO;
import com.ubb.zenith.exception.*;
import com.ubb.zenith.model.Mood;
import com.ubb.zenith.model.Song;
import com.ubb.zenith.repository.MoodRepository;
import com.ubb.zenith.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SongService {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private MoodRepository moodRepository;

    /**
     * doar apeleaza repository sa faca rost de lista dorita
     * @return o lista de cantece
     */
    public List<Song> getAll() {
        return songRepository.findAll();
    }

    /**
     * verifica daca exista un cantesc deja adaugat in lista si arunca eroare daca exista
     * @param title
     * @param artist
     * @throws SongAlreadyExistsException
     */
    public void checkIfSongAlreadyExists(final String title,final String artist) throws SongAlreadyExistsException {
        if (songRepository.findByArtistAndTitle(artist,title).isPresent()) {
            throw new SongAlreadyExistsException("Song already exists");
        }
    }

    /**
     * dupa ce se asigura ca nu exista cantecul deja apeleaza functia de adaugare
     * @param songDTO
     * @return apeleaza functia addSong sa adauge in repo
     * @throws SongAlreadyExistsException
     * @throws SongNotFoundException
     * @throws MoodNotFoundException
     */
    public Song add(final SongDTO songDTO) throws SongAlreadyExistsException, SongNotFoundException,MoodNotFoundException {
        checkIfSongAlreadyExists(songDTO.getTitle(),songDTO.getArtist());
        return addSong(songDTO);
    }

    /**
     * salveaza in repo noua entitate song creata in functia buildSong
     * @param song
     * @return noua entitate
     * @throws SongNotFoundException
     * @throws MoodNotFoundException
     */
    public Song addSong(final SongDTO song) throws MoodNotFoundException {return songRepository.save(BuildSong(song)); }

    /**
     * creaza un nou song ii atribuie valorile din DTO si creaza un mood default daca nu exista ,daca exista adauga in lista mood ului noul cantect creat
     * @param songDTO
     * @return cantectul creat
     * @throws MoodNotFoundException
     */
    public Song BuildSong(SongDTO songDTO) throws MoodNotFoundException {
        var song = new Song();
        song.setArtist(songDTO.getArtist());
        song.setTitle(songDTO.getTitle());
        song.setGenre(songDTO.getGenre());
        Mood mood;
        if (songDTO.getMoodId() == null) {
            mood = new Mood();
            mood.setHappiness_score(5);  // 5 este valoare default
            mood.setSadness_score(5);
            mood.setLove_score(5);
            mood.setEnergy_score(5);

            moodRepository.save(mood);
        } else {
            mood = moodRepository.findById(songDTO.getMoodId()).orElseThrow(() -> new MoodNotFoundException("Mood not found"));
        }

        if (mood.getSongs() == null) {
            mood.setSongs(new ArrayList<>());
        }

        song.setMood(mood);
        mood.getSongs().add(song);
        songRepository.save(song);
        moodRepository.save(mood);

        return song;
    }

    /**
     * sterge cantectul folosindu-se de parametrii prezentati
     * @param artist
     * @param title
     * @throws SongNotFoundException
     */
    public void deleteSong(final String artist,final String title) throws SongNotFoundException { songRepository.delete(findSong(artist,title)); }

    /**
     * modifica un cantec cu noile campuri
     * @param song
     * @param newtittle
     * @param newArtist
     * @param newGenre
     * @param newMood
     * @return
     */
    public Song updateSong(final Song song, final String newtittle,final String newArtist,final String newGenre,final Mood newMood) {
        song.setArtist(newArtist); song.setTitle(newtittle); song.setGenre(newGenre); song.setMood(newMood);
        return songRepository.save(song); }

    /**
     * cauta in lista de pe repo un cantesc folosindu-se de campurile artist si title
     * @param artist
     * @param title
     * @return
     * @throws SongNotFoundException
     */
    public Song findSong(final String artist,final String title) throws SongNotFoundException { return songRepository.findAll().stream()
            .filter(song -> song.getArtist().equals(artist) && song.getTitle().equals(title)).findFirst().orElseThrow(() -> new SongNotFoundException("Song not found")); }

    /**
     * se face o cautare de data asta folosindu-se de parametrul mood
     * @param mood
     * @return
     * @throws SongNotFoundException
     */
    public Song findSongsByMood(final Mood mood) throws SongNotFoundException {
        return songRepository.findAll().stream()
                .filter(song -> song.getMood().equals(mood)).findFirst().orElseThrow(() -> new SongNotFoundException("Song not found"));
    }

}
