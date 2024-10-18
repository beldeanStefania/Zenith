package com.ubb.zenith.service;

import com.ubb.zenith.exception.UserNotFoundException;
import com.ubb.zenith.model.Mood;
import com.ubb.zenith.model.Song;
import com.ubb.zenith.model.User;
import com.ubb.zenith.repository.SongRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongService {

    @Autowired
    private SongRepository songRepository;

    public List<Song> getAll() {
        return songRepository.findAll();
    }

    public Song addSong(Song song) { return songRepository.save(song); }

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
