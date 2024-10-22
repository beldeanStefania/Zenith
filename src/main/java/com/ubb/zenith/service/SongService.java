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
     * Simply calls the repository to get the desired list.
     * @return a list of songs
     */
    public List<Song> getAll() {
        return songRepository.findAll();
    }

    /**
     * Checks if a song is already added to the list and throws an error if it exists.
     * @param title
     * @param artist
     * @throws SongAlreadyExistsException if the song already exists
     */
    public void checkIfSongAlreadyExists(final String title, final String artist) throws SongAlreadyExistsException {
        if (songRepository.findByArtistAndTitle(artist, title).isPresent()) {
            throw new SongAlreadyExistsException("Song already exists");
        }
    }

    /**
     * After ensuring that the song does not already exist, it calls the add function.
     * @param songDTO
     * @return calls the addSong function to add it to the repository.
     * @throws SongAlreadyExistsException if the song already exists
     * @throws SongNotFoundException if the song is not found
     * @throws MoodNotFoundException if the mood is not found
     */
    public Song add(final SongDTO songDTO) throws SongAlreadyExistsException, SongNotFoundException, MoodNotFoundException {
        checkIfSongAlreadyExists(songDTO.getTitle(), songDTO.getArtist());
        return addSong(songDTO);
    }

    /**
     * Saves the new song entity created in the buildSong function to the repository.
     * @param song
     * @return the new entity
     * @throws SongNotFoundException if the song is not found
     * @throws MoodNotFoundException if the mood is not found
     */
    public Song addSong(final SongDTO song) throws MoodNotFoundException {
        return songRepository.save(BuildSong(song));
    }

    /**
     * Creates a new song, assigns it the values from the DTO, and creates a default mood if none exists.
     * If the mood exists, the new song is added to the mood's list.
     * @param songDTO
     * @return the created song
     * @throws MoodNotFoundException when we try to add a song to a mood that does not exist
     */
    public Song BuildSong(SongDTO songDTO) throws MoodNotFoundException {
        var song = new Song();
        song.setArtist(songDTO.getArtist());
        song.setTitle(songDTO.getTitle());
        song.setGenre(songDTO.getGenre());
        Mood mood;
        if (songDTO.getMoodId() == null) {
            mood = new Mood();
            mood.setHappiness_score(5);  // 5 is the default value
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
     * Deletes the song using the provided parameters.
     * @param artist
     * @param title
     * @throws SongNotFoundException if the song is not found
     */
    public void deleteSong(final String artist, final String title) throws SongNotFoundException {
        songRepository.delete(findSong(artist, title));
    }

    /**
     *
     * @param title
     * @param artist
     * @param newSong
     * @return
     * @throws SongNotFoundException
     */
    public Song updateSong(final String title,final String artist, final SongDTO newSong) throws SongNotFoundException {
        Song song = songRepository.findByArtistAndTitle(artist, title).orElseThrow(() -> new SongNotFoundException("Song not found"));
        song.setArtist(newSong.getArtist());
        song.setTitle(newSong.getTitle());
        song.setGenre(newSong.getGenre());
        song.setMood(moodRepository.findAll().get(newSong.getMoodId()));
        return songRepository.save(song);
    }

    /**
     * Searches the repository list for a song using the artist and title fields.
     * @param artist
     * @param title
     * @return the song with the desired artist and title
     * @throws SongNotFoundException if the song is not found
     */
    public Song findSong(final String artist, final String title) throws SongNotFoundException {
        return songRepository.findAll().stream()
                .filter(song -> song.getArtist().equals(artist) && song.getTitle().equals(title))
                .findFirst().orElseThrow(() -> new SongNotFoundException("Song not found"));
    }

    /**
     * This time, the search is done using the mood parameter.
     * @param mood
     * @return the song with the desired mood
     * @throws SongNotFoundException   if the song is not found
     */
    public Song findSongsByMood(final Mood mood) throws SongNotFoundException {
        return songRepository.findAll().stream()
                .filter(song -> song.getMood().equals(mood))
                .findFirst().orElseThrow(() -> new SongNotFoundException("Song not found"));
    }

}
