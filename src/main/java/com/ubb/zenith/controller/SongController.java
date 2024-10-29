package com.ubb.zenith.controller;

import com.ubb.zenith.dto.SongDTO;
import com.ubb.zenith.exception.MoodNotFoundException;
import com.ubb.zenith.exception.SongAlreadyExistsException;
import com.ubb.zenith.exception.SongNotFoundException;
import com.ubb.zenith.model.Song;
import com.ubb.zenith.service.SongService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/song")
public class SongController {

    @Autowired
    private SongService songService;

    /**
     * Retrieves all song entries from the repository.
     *
     * @return a list of all songs in the database
     */
    @GetMapping("/getAll")
    public List<Song> getAll() {
        return songService.getAll();
    }

    /**
     * Adds a new song after verifying if a song with the same title and artist already exists.
     *
     * @param songDTO DTO object that contains the information of the song to be added.
     * @return the added song.
     * @throws SongAlreadyExistsException if a song with the same title and artist already exists.
     * @throws MoodNotFoundException if the mood is not found.
     */
    @PostMapping("/add")
    public ResponseEntity<Song> add(@Valid @RequestBody SongDTO songDTO) {
        try {
            return ok(songService.add(songDTO));
        } catch (SongAlreadyExistsException | MoodNotFoundException e) {
            return notFound().build();
        }
    }

    /**
     * Updates a song entry in the repository.
     *
     * @param title the title of the song to be updated.
     * @param artist the artist of the song to be updated.
     * @param songDTO DTO object that contains the information of the song to be updated.
     * @return the updated song.
     */
    @PutMapping("/update/{title}/{artist}")
    public ResponseEntity<Song> update(@PathVariable String title, @PathVariable String artist, @RequestBody SongDTO songDTO) {
        try {
            return ok(songService.update(title, artist, songDTO));
        } catch (SongNotFoundException e) {
            return badRequest().build();
        }
    }

    /**
     * Deletes a song entry from the repository.
     *
     * @param songId the ID of the song to be deleted.
     * @return the deleted song.
     */
    @DeleteMapping("/delete/{songId}")
    public ResponseEntity<Song> delete(@PathVariable Integer songId) {
        try {
            songService.delete(songId);
            return ok().build();
        } catch (SongNotFoundException e) {
            return badRequest().build();
        }
    }

}
