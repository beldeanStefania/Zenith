package com.ubb.zenith.controller;

import com.ubb.zenith.dto.MoodDTO;
import com.ubb.zenith.exception.PlaylistAlreadyExistsException;
import com.ubb.zenith.exception.PlaylistNotFoundException;
import com.ubb.zenith.exception.SongNotFoundException;
import com.ubb.zenith.exception.UserNotFoundException;
import com.ubb.zenith.model.Playlist;
import com.ubb.zenith.model.Song;
import com.ubb.zenith.model.UserPlaylist;
import com.ubb.zenith.repository.SongRepository;
import com.ubb.zenith.service.PlaylistService;
import com.ubb.zenith.service.UserPlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/userPlaylist")
public class UserPlaylistController {

    @Autowired
    SongRepository songRepository;

    @Autowired
    private UserPlaylistService userPlaylistService;

    @Autowired
    private PlaylistService playlistService;

    @GetMapping("/getAll")
    public List<UserPlaylist> getAll() {
        return userPlaylistService.getAll();
    }

//    @PostMapping("/generateSpotifyPlaylist/{username}/{playlistName}")
//    public ResponseEntity<UserPlaylist> generateSpotifyPlaylist(@PathVariable String username, @PathVariable String playlistName, @RequestBody MoodDTO moodDTO) {
//        try {
//            UserPlaylist userPlaylist = userPlaylistService.generatePlaylistForUser(
//                    username,
//                    moodDTO.getHappiness_score(),
//                    moodDTO.getSadness_score(),
//                    moodDTO.getLove_score(),
//                    moodDTO.getEnergy_score(),
//                    playlistName
//            );
//            return ResponseEntity.ok(userPlaylist);
//        } catch (PlaylistAlreadyExistsException | UserNotFoundException e) {
//            return ResponseEntity.status(NOT_FOUND).build();
//        }
//    }

//    @PostMapping("/generate/{username}/{playlistName}")
//    public ResponseEntity<UserPlaylist> generatePlaylist(@PathVariable String username, @PathVariable String playlistName, @RequestBody MoodDTO moodDTO) {
//        try {
//
//            UserPlaylist userPlaylist = userPlaylistService.generatePlaylistForUser(
//                    username,
//                    moodDTO.getHappiness_score(),
//                    moodDTO.getSadness_score(),
//                    moodDTO.getLove_score(),
//                    moodDTO.getEnergy_score(),
//                    playlistName
//            );
//            return ResponseEntity.ok(userPlaylist);
//        } catch (PlaylistAlreadyExistsException | UserNotFoundException e) {
//            return notFound().build();
//        }
//    }

    /**
     * Generates a playlist based on the mood scores provided.
     *
     * @param playlistName the name of the playlist to be generated.
     * @param moodDTO the DTO object containing the mood scores.
     * @return the generated playlist.
     */

    /**
     * Retrieves all songs from a playlist.
     *
     * @param playlistName the name of the playlist to get the songs from.
     * @return a list of all songs in the playlist.
     */
    @GetMapping("/getSongsFromPlaylist/{playlistName}")
    public ResponseEntity<List<Song>> getSongsFromPlaylist(@PathVariable String playlistName) {
        try {
            List<Song> songs = playlistService.getSongsFromPlaylist(playlistName);
            if (songs.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).build(); // Returnează 404
            }
            return ok(songs);
        } catch (PlaylistNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).build(); // 404 în loc de 400
        }
    }


    @GetMapping("/getAudio/{songId}")
    public ResponseEntity<byte[]> getAudio(@PathVariable Integer songId) {
        try {
            Song song = songRepository.findById(songId)
                    .orElseThrow(() -> new SongNotFoundException("Song not found"));

            return ResponseEntity.ok()
                    .header("Content-Type", "audio/mpeg") // MIME type pentru MP3
                    .body(song.getAudioData());
        } catch (SongNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
