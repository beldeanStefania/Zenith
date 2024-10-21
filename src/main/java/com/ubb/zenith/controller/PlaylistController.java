package com.ubb.zenith.controller;

import com.ubb.zenith.dto.PlaylistDTO;
import com.ubb.zenith.exception.PlaylistAlreadyExistsException;
import com.ubb.zenith.exception.PlaylistNotFoundException;
import com.ubb.zenith.model.Playlist;
import com.ubb.zenith.service.PlaylistService;
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
@RequestMapping("/api/playlist")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;


    @GetMapping("/getAll")
    public List<Playlist> getAll() {
        return playlistService.getAll();
    }

    @PostMapping("/add")
    public ResponseEntity<Playlist> add(@Valid @RequestBody PlaylistDTO playlistDTO) {
        try {
            return ok(playlistService.add(playlistDTO));
        } catch (PlaylistAlreadyExistsException e) {
            return notFound().build();
        }
    }

    @PostMapping("/addSong/{playlistName}/{songId}")
    public ResponseEntity<Playlist> addSong(@PathVariable String playlistName, @PathVariable Integer songId) {
        try {
            return ok(playlistService.addSongToPlaylist(playlistName, songId));
        } catch (PlaylistNotFoundException e) {
            return notFound().build();
        }
    }

    @PutMapping("/update/{oldName}")
    public ResponseEntity<Playlist> update(@PathVariable String oldName, @RequestBody PlaylistDTO playlistDTO) {
        try {
            return ok(playlistService.update(oldName, playlistDTO));
        } catch (PlaylistNotFoundException e) {
            return badRequest().build();
        }
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<Playlist> delete(@PathVariable String name) {
        try {
            playlistService.delete(name);
            return ok().build();
        } catch (PlaylistNotFoundException e) {
            return badRequest().build();
        }
    }
}
