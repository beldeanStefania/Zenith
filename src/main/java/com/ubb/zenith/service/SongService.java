package com.ubb.zenith.service;

import com.ubb.zenith.dto.SongDTO;
import com.ubb.zenith.exception.UserAlreadyExistsException;
import com.ubb.zenith.exception.UserNotFoundException;
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

    public List<Song> getAll() {
        return songRepository.findAll();
    }

    public void checkIfSongAlreadyExists(final String title,final String artist) throws UserAlreadyExistsException {
        if (songRepository.findByArtistAndTitle(artist,title).isPresent()) {
            throw new UserAlreadyExistsException("Song already exists");
        }
    }

    public Song add(final SongDTO songDTO) throws UserAlreadyExistsException, UserNotFoundException {
        checkIfSongAlreadyExists(songDTO.getTitle(),songDTO.getArtist());
        return addSong(songDTO);
    }
    public Song addSong(final SongDTO song) throws UserNotFoundException {return songRepository.save(BuildSong(song)); }

    public Song BuildSong(SongDTO songDTO) throws UserNotFoundException {
        var song = new Song();
        song.setArtist(songDTO.getArtist());
        song.setTitle(songDTO.getTitle());
        song.setGenre(songDTO.getGenre());
        Mood mood;

        // Verifică dacă există Mood-ul specificat
        if (songDTO.getMoodId() == null) {
            // Creează un Mood nou dacă nu există unul
            mood = new Mood();
            mood.setHappiness_score(5);  // 5 este valoare default
            mood.setSadness_score(5);
            mood.setLove_score(5);
            mood.setEnergy_score(5);

            // Salvează Mood-ul nou în baza de date
            moodRepository.save(mood);
        } else {
            // Găsește Mood-ul specificat
            mood = moodRepository.findById(songDTO.getMoodId()).orElseThrow(() -> new UserNotFoundException("Mood not found"));
        }

        // Inițializează lista de melodii dacă este goală
        if (mood.getSongs() == null) {
            mood.setSongs(new ArrayList<>());
        }

        // Setează Mood-ul în Song
        song.setMood(mood);

        // Adaugă melodia la lista Mood-ului
        mood.getSongs().add(song);

        // Salvează Song și Mood (dacă este necesar)
        songRepository.save(song);
        moodRepository.save(mood);

        return song;
    }

    public void deleteSong(final String artist,final String title) throws UserNotFoundException { songRepository.delete(findSong(artist,title)); }

    public Song updateSong(final Song song, final String newtittle,final String newArtist,final String newGenre,final Mood newMood) {
        song.setArtist(newArtist); song.setTitle(newtittle); song.setGenre(newGenre); song.setMood(newMood);
        return songRepository.save(song); }

    public Song findSong(final String artist,final String title) throws UserNotFoundException { return songRepository.findAll().stream()
            .filter(song -> song.getArtist().equals(artist) && song.getTitle().equals(title)).findFirst().orElseThrow(() -> new UserNotFoundException("Song not found")); }

    public Song findSongsByMood(final Mood mood) throws UserNotFoundException {
        return songRepository.findAll().stream()
                .filter(song -> song.getMood().equals(mood)).findFirst().orElseThrow(() -> new UserNotFoundException("Song not found"));
    }

}
