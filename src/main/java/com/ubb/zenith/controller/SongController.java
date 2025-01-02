//package com.ubb.zenith.controller;
//
//import com.ubb.zenith.dto.SongDTO;
//import com.ubb.zenith.exception.MoodNotFoundException;
//import com.ubb.zenith.exception.SongAlreadyExistsException;
//import com.ubb.zenith.exception.SongNotFoundException;
//import com.ubb.zenith.model.Song;
//import com.ubb.zenith.service.SongService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Optional;
//
//import static org.springframework.http.ResponseEntity.badRequest;
//import static org.springframework.http.ResponseEntity.notFound;
//import static org.springframework.http.ResponseEntity.ok;
//
//@CrossOrigin(origins = "http://localhost:3000")
//@RestController
//@RequestMapping("/api/song")
//public class SongController {
//
//    @Autowired
//    private SongService songService;
//
//    /**
//     * Retrieves all song entries from the repository.
//     *
//     * @return a list of all songs in the database
//     */
//    @GetMapping("/getAll")
//    public List<Song> getAll() {
//        return songService.getAll();
//    }
//
//    @GetMapping("/getAllDTOs")
//    public List<SongDTO> getAllDTOs() {
//        return songService.getAllSongsAsDTOs();
//    }
//
//    /**
//     * Adds a new song after verifying if a song with the same title and artist already exists.
//     *
//     * @param songDTO DTO object that contains the information of the song to be added.
//     * @return the added song.
//     * @throws SongAlreadyExistsException if a song with the same title and artist already exists.
//     * @throws MoodNotFoundException if the mood is not found.
//     */
//    @PostMapping("/add")
//    public ResponseEntity<Song> add(@Valid @RequestBody SongDTO songDTO) {
//        try {
//            return ok(songService.add(songDTO));
//        } catch (SongAlreadyExistsException | MoodNotFoundException e) {
//            return notFound().build();
//        }
//    }
//
//    @PostMapping("/upload")
//    public ResponseEntity<Song> uploadSong(
//            @RequestParam("title") String title,
//            @RequestParam("artist") String artist,
//            @RequestParam(value = "genre", required = false) String genre,
//            @RequestParam("file") MultipartFile file) {
//        try {
//            Song savedSong = songService.saveSong(title, artist, genre, file);
//            return new ResponseEntity<>(savedSong, HttpStatus.CREATED);
//        } catch (IOException e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @GetMapping("/play/{id}")
//    public ResponseEntity<byte[]> playSong(@PathVariable Integer id) {
//        Optional<Song> songOptional = songService.findSongById(id);
//        if (songOptional.isPresent() && songOptional.get().getAudioData() != null) {
//            return ResponseEntity.ok()
//                    .header("Content-Type", "audio/mp3") // sau "audio/mp4" dacă fișierul este audio
//                    .body(songOptional.get().getAudioData());
//        }
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
//
//
//
//
//    /**
//     * Updates a song entry in the repository.
//     *
//     * @param title the title of the song to be updated.
//     * @param artist the artist of the song to be updated.
//     * @param songDTO DTO object that contains the information of the song to be updated.
//     * @return the updated song.
//     */
//    @PutMapping("/update/{title}/{artist}")
//    public ResponseEntity<Song> update(@PathVariable String title, @PathVariable String artist, @RequestBody SongDTO songDTO) {
//        try {
//            return ok(songService.update(title, artist, songDTO));
//        } catch (SongNotFoundException e) {
//            return badRequest().build();
//        }
//    }
//
//    /**
//     * Deletes a song entry from the repository.
//     *
//     * @param songId the ID of the song to be deleted.
//     * @return the deleted song.
//     */
//    @DeleteMapping("/delete/{songId}")
//    public ResponseEntity<Song> delete(@PathVariable Integer songId) {
//        try {
//            songService.delete(songId);
//            return ok().build();
//        } catch (SongNotFoundException e) {
//            return badRequest().build();
//        }
//    }
//
//    @GetMapping
//    public List<Song> getAllSongs() {
//        return songService.getAll();
//    }
//
//}
