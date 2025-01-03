//package com.ubb.zenith.controller;
//
//import com.ubb.zenith.dto.MoodDTO;
//import com.ubb.zenith.dto.PlaylistDTO;
//import com.ubb.zenith.exception.PlaylistAlreadyExistsException;
//import com.ubb.zenith.exception.PlaylistNotFoundException;
//import com.ubb.zenith.exception.SongAlreadyExistsException;
//import com.ubb.zenith.exception.SongNotFoundException;
//import com.ubb.zenith.model.Playlist;
//import com.ubb.zenith.model.Song;
//import com.ubb.zenith.service.PlaylistService;
//import com.ubb.zenith.service.SongService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//import static org.springframework.http.ResponseEntity.badRequest;
//import static org.springframework.http.ResponseEntity.notFound;
//import static org.springframework.http.ResponseEntity.ok;
//
//@RestController
//@RequestMapping("/api/playlist")
//public class PlaylistController {
//
//    @Autowired
//    private PlaylistService playlistService;
//
//    /**
//     * Retrieves all playlist entries from the repository.
//     *
//     * @return a list of all playlists in the database
//     */
//    @GetMapping("/getAll")
//    public List<Playlist> getAll() {
//        return playlistService.getAll();
//    }
//
//    /**
//     * Adds a new playlist after verifying if a playlist with the same name already exists.
//     *
//     * @param playlistDTO DTO object that contains the information of the playlist to be added.
//     * @return the added playlist.
//     * @throws PlaylistAlreadyExistsException if a playlist with the same name already exists.
//     */
//    @PostMapping("/add")
//    public ResponseEntity<Playlist> add(@Valid @RequestBody PlaylistDTO playlistDTO) {
//        try {
//            return ok(playlistService.add(playlistDTO));
//        } catch (PlaylistAlreadyExistsException e) {
//            return notFound().build();
//        }
//    }
//
//    /**
//     * Adds a song to a playlist.
//     *
//     * @param playlistName the name of the playlist to add the song to.
//     * @param songId the ID of the song to be added.
//     * @return the updated playlist.
//     * @throws PlaylistNotFoundException if the playlist is not found.
//     * @throws SongNotFoundException if the song is not found.
//     * @throws SongAlreadyExistsException if the song is already in the playlist.
//     */
//    @PostMapping("/addSongToPlaylist/{playlistName}/{songId}")
//    public ResponseEntity<Playlist> addSong(@PathVariable String playlistName, @PathVariable Integer songId) {
//        try {
//            // Find the playlist first
//            Playlist updatedPlaylist = playlistService.addSongToPlaylist(playlistName, songId);
//
//            return ok(updatedPlaylist);
//        } catch (PlaylistNotFoundException | SongNotFoundException | SongAlreadyExistsException e) {
//            return notFound().build();
//        }
//    }
//
//    /**
//     * Updates a playlist entry in the repository.
//     *
//     * @param oldName the name of the playlist to be updated.
//     * @param playlistDTO the DTO object containing the new information of the playlist.
//     * @return the updated playlist.
//     */
//    @PutMapping("/update/{oldName}")
//    public ResponseEntity<Playlist> update(@PathVariable String oldName, @RequestBody PlaylistDTO playlistDTO) {
//        try {
//            return ok(playlistService.update(oldName, playlistDTO));
//        } catch (PlaylistNotFoundException e) {
//            return badRequest().build();
//        }
//    }
//
//    /**
//     * Deletes a playlist entry from the repository.
//     *
//     * @param name the name of the playlist to be deleted.
//     * @return the deleted playlist.
//     */
//    @DeleteMapping("/delete/{name}")
//    public ResponseEntity<Playlist> delete(@PathVariable String name) {
//        try {
//            playlistService.delete(name);
//            return ok().build();
//        } catch (PlaylistNotFoundException e) {
//            return badRequest().build();
//        }
//    }
//}
