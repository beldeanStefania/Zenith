package com.ubb.zenith.controller;

import com.ubb.zenith.dto.UserPlaylistDTO;
import com.ubb.zenith.exception.UserPlaylistAlreadyExistsException;
import com.ubb.zenith.exception.UserPlaylistNotFoundException;
import com.ubb.zenith.model.UserPlaylist;
import com.ubb.zenith.service.UserPlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/userPlaylist")
public class UserPlaylistController {

    @Autowired
    private UserPlaylistService userPlaylistService;

    @GetMapping("/getAll")
    public List<UserPlaylist> getAll() {
        return userPlaylistService.getAll();
    }

    @PostMapping("/add")
    public ResponseEntity<UserPlaylist> add(@RequestBody UserPlaylistDTO userPlaylistDTO) {
        try {
            return ok(userPlaylistService.createUserPlaylist(userPlaylistDTO));
        } catch (UserPlaylistNotFoundException | UserPlaylistAlreadyExistsException e) {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("/delete/{username}/{playlistName}")
    public ResponseEntity<UserPlaylist> delete(@PathVariable String username, @PathVariable String playlistName) {
        try {
            userPlaylistService.deleteUserPlaylist(username, playlistName);
            return ok().build();
        } catch (UserPlaylistNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
