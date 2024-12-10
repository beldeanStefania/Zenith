package com.ubb.zenith.service;

import com.ubb.zenith.dto.SongDTO;
import com.ubb.zenith.exception.*;
import com.ubb.zenith.model.Mood;
import com.ubb.zenith.model.Playlist;
import com.ubb.zenith.model.Song;
import com.ubb.zenith.repository.MoodRepository;
import com.ubb.zenith.repository.PlaylistRepository;
import com.ubb.zenith.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SongService {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private MoodRepository moodRepository;

    /**
     * Retrieves all songs from the repository.
     *
     * @return a list of all available songs.
     */
    public List<Song> getAll() {
        return songRepository.findAll();
    }

    /**
     * Adds a new song after verifying if a song with the same title and artist already exists.
     *
     * @param songDTO DTO object that contains the information of the song to be added.
     * @return the added song.
     * @throws SongAlreadyExistsException if a song with the same title and artist already exists.
     * @throws MoodNotFoundException if the mood is not found.
     */
    public Song add(final SongDTO songDTO) throws SongAlreadyExistsException, MoodNotFoundException {
        checkIfSongAlreadyExists(songDTO.getTitle(), songDTO.getArtist());
        return songRepository.save(buildSong(songDTO));
    }

    /**
     * Checks if a song with a specific title and artist exists in the repository.
     *
     * @param title the title of the song to be checked.
     * @param artist the artist of the song to be checked.
     * @throws SongAlreadyExistsException if the song already exists.
     */
    private void checkIfSongAlreadyExists(final String title, final String artist) throws SongAlreadyExistsException {
        if (songRepository.findByArtistAndTitle(artist, title).isPresent()) {
            throw new SongAlreadyExistsException("Song already exists");
        }
    }

    /**
     * Builds a new Song entity based on the information in the DTO.
     *
     * @param songDTO the DTO object containing the information of the song to be added.
     * @return the built Song entity.
     * @throws MoodNotFoundException if the mood is not found.
     */
    private Song buildSong(final SongDTO songDTO) throws MoodNotFoundException {
        // Create the new Song entity
        Song song = new Song();
        song.setArtist(songDTO.getArtist());
        song.setTitle(songDTO.getTitle());
        song.setGenre(songDTO.getGenre());

        Mood mood = moodRepository.findById(songDTO.getMoodId())
                .orElseThrow(() -> new MoodNotFoundException("Mood not found"));

        song.setMood(mood);

        song.setPlaylist(null);

        return song;
    }

//    public void saveAudioData(Integer songId, MultipartFile file) throws IOException, ResourceNotFoundException {
//        Song song = songRepository.findById(songId)
//                .orElseThrow(() -> new ResourceNotFoundException("Song not found"));
//
//        song.setAudioData(file.getBytes()); // Setează datele fișierului audio
//        songRepository.save(song);
//    }
//
//    public byte[] getAudioData(Integer songId) throws ResourceNotFoundException {
//        Song song = songRepository.findById(songId)
//                .orElseThrow(() -> new ResourceNotFoundException("Song not found"));
//        return song.getAudioData(); // Returnează datele fișierului audio
//    }
//
//    public void saveSongWithFile(String title, String artist, String genre, MultipartFile file) throws IOException {
//        Song song = new Song();
//        song.setTitle(title);
//        song.setArtist(artist);
//        song.setGenre(genre);
//        song.setAudioData(file.getBytes()); // Convertește fișierul în byte[] și salvează-l
//
//        songRepository.save(song);
//    }

    public Optional<Song> findSongById(Integer id) {
        return songRepository.findById(id);
    }

    public List<SongDTO> getAllSongsAsDTOs() {
        return songRepository.findAll().stream()
                .map(song -> {
                    SongDTO dto = new SongDTO();
                    dto.setTitle(song.getTitle());
                    dto.setArtist(song.getArtist());
                    dto.setGenre(song.getGenre());
                    dto.setMoodId(song.getMood().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public Song saveSong(String title, String artist, String genre, MultipartFile file) throws IOException {
        Song song = new Song();
        song.setTitle(title);
        song.setArtist(artist);
        song.setGenre(genre);

        // Convertește fișierul într-un array de bytes
        song.setAudioData(file.getBytes());

        // Salvează melodia în baza de date
        return songRepository.save(song);
    }

    /**
     * Updates an existing song based on the old title and artist and the new information from SongDTO.
     *
     * @param title the old title of the song to be updated.
     * @param artist the old artist of the song to be updated.
     * @param newSongDTO DTO object with the new information of the song.
     * @return the updated song.
     * @throws SongNotFoundException if no song with the specified title and artist is found.
     */
    public Song update(final String title, final String artist, final SongDTO newSongDTO) throws SongNotFoundException {
        Song song = songRepository.findByArtistAndTitle(artist, title)
                .orElseThrow(() -> new SongNotFoundException("Song not found"));

        // Update song details
        song.setArtist(newSongDTO.getArtist());
        song.setTitle(newSongDTO.getTitle());
        song.setGenre(newSongDTO.getGenre());

        return songRepository.save(song);
    }

    /**
     * Assign a playlist to a song.
     *
     * @param songId the ID of the song to be updated.
     * @param playlistId the ID of the playlist to be assigned to the song.
     * @return the updated song.
     * @throws SongNotFoundException if no song with the specified ID is found.
     * @throws PlaylistNotFoundException if no playlist with the specified ID is found.
     */
    public Song assignPlaylistToSong(Integer songId, Integer playlistId) throws SongNotFoundException, PlaylistNotFoundException {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException("Song not found"));

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist not found"));

        song.setPlaylist(playlist);
        return songRepository.save(song);
    }

    /**
     * Deletes a song from the repository based on its ID.
     *
     * @param songId the ID of the song to delete.
     * @throws SongNotFoundException if no song with the specified ID is found.
     */
    public void delete(Integer songId) throws SongNotFoundException {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException("Song not found"));
        songRepository.delete(song);
    }

    /**
     * Finds a song by its artist and title.
     *
     * @param artist the artist of the song to be searched.
     * @param title the title of the song to be searched.
     * @return the found song.
     * @throws SongNotFoundException if no song with the specified artist and title is found.
     */
    public Song findSong(final String artist, final String title) throws SongNotFoundException {
        return songRepository.findByArtistAndTitle(artist, title)
                .orElseThrow(() -> new SongNotFoundException("Song not found"));
    }

    /**
     * Finds a song by its ID.
     *
     * @param playlistId the ID of the playlist to be searched.
     * @return the found song.
     * @throws SongNotFoundException if no song with the specified ID is found.
     */
    private Playlist findPlaylistById(Integer playlistId) throws PlaylistNotFoundException {
        return playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist not found with id " + playlistId));
    }
}
