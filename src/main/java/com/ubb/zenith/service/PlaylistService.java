package com.ubb.zenith.service;

import com.ubb.zenith.dto.PlaylistDTO;
import com.ubb.zenith.exception.PlaylistAlreadyExistsException;
import com.ubb.zenith.exception.PlaylistNotFoundException;
import com.ubb.zenith.model.Playlist;
import com.ubb.zenith.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;

    /**
     * Preia toate playlist-urile din repository.
     *
     * @return o listă cu toate playlist-urile disponibile.
     */
    public List<Playlist> getAll() {
        return playlistRepository.findAll();
    }

    /**
     * Adaugă un playlist nou, verificând mai întâi dacă există un playlist cu același nume.
     *
     * @param playlistDTO obiect DTO care conține informațiile playlist-ului de adăugat.
     * @return playlist-ul adăugat.
     * @throws PlaylistAlreadyExistsException dacă un playlist cu același nume există deja.
     */
    public Playlist add(final PlaylistDTO playlistDTO) throws PlaylistAlreadyExistsException {
        checkIfPlaylistAlreadyExists(playlistDTO.getName());
        return add(buildPlaylist(playlistDTO));
    }

    /**
     * Verifică dacă un playlist cu un anumit nume există în repository.
     *
     * @param name numele playlist-ului de verificat.
     * @throws PlaylistAlreadyExistsException dacă playlist-ul există deja.
     */
    public void checkIfPlaylistAlreadyExists(final String name) throws PlaylistAlreadyExistsException {
        if (playlistRepository.findByName(name).isPresent()) {
            throw new PlaylistAlreadyExistsException("Playlist already exists");
        }
    }

    /**
     * Construiește un obiect Playlist dintr-un PlaylistDTO.
     *
     * @param playlistDTO obiect DTO care conține informațiile pentru construirea playlist-ului.
     * @return un obiect Playlist construit.
     */
    public Playlist buildPlaylist(final PlaylistDTO playlistDTO) {
        var playlist = new Playlist();
        playlist.setName(playlistDTO.getName());
        playlist.setSongs(playlistDTO.getSongs());
        return playlist;
    }

    /**
     * Adaugă un playlist în repository.
     *
     * @param playlist obiectul Playlist de adăugat.
     * @return playlist-ul salvat în repository.
     */
    public Playlist add(Playlist playlist) {
        return playlistRepository.save(playlist);
    }

    /**
     * Actualizează un playlist existent pe baza vechiului nume și a noilor informații din PlaylistDTO.
     *
     * @param oldName numele vechi al playlist-ului care trebuie actualizat.
     * @param playlistDTO obiect DTO cu noile informații ale playlist-ului.
     * @return playlist-ul actualizat.
     * @throws PlaylistNotFoundException dacă nu există un playlist cu numele specificat.
     */
    public Playlist update(final String oldName, final PlaylistDTO playlistDTO) throws PlaylistNotFoundException {
        return updateName(findPlaylist(oldName), playlistDTO);
    }

    /**
     * Actualizează numele și melodiile unui playlist existent.
     *
     * @param playlist obiectul Playlist de actualizat.
     * @param playlistDTO obiect DTO cu noile informații ale playlist-ului.
     * @return playlist-ul actualizat.
     */
    public Playlist updateName(final Playlist playlist, final PlaylistDTO playlistDTO) {
        playlist.setName(playlistDTO.getName());
        playlist.setSongs(playlistDTO.getSongs());
        return playlistRepository.save(playlist);
    }

    /**
     * Caută un playlist după nume.
     *
     * @param name numele playlist-ului căutat.
     * @return playlist-ul găsit.
     * @throws PlaylistNotFoundException dacă nu există un playlist cu numele specificat.
     */
    public Playlist findPlaylist(final String name) throws PlaylistNotFoundException {
        var modifiedPlaylist = playlistRepository.findAll().stream()
                .filter(playlist -> playlist.getName().equals(name)).findFirst().orElseThrow(() -> new PlaylistNotFoundException("Playlist not found"));
        return modifiedPlaylist;
    }

    /**
     * Șterge un playlist existent pe baza numelui.
     *
     * @param name numele playlist-ului de șters.
     * @throws PlaylistNotFoundException dacă nu există un playlist cu numele specificat.
     */
    public void delete(final String name) throws PlaylistNotFoundException {
        playlistRepository.delete(findPlaylist(name));
    }
}
