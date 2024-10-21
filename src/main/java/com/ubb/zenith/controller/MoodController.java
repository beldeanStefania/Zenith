package com.ubb.zenith.controller;

import com.ubb.zenith.dto.MoodDTO;
import com.ubb.zenith.dto.UserDTO;
import com.ubb.zenith.exception.MoodAlreadyExistsException;
import com.ubb.zenith.exception.MoodNotFoundException;
import com.ubb.zenith.exception.UserAlreadyExistsException;
import com.ubb.zenith.exception.UserNotFoundException;
import com.ubb.zenith.model.Mood;
import com.ubb.zenith.model.User;
import com.ubb.zenith.service.MoodService;
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
@RequestMapping("/api/mood")
public class MoodController {

    @Autowired
    private MoodService moodService;

    @GetMapping("/getAll")
    public List<Mood> getAll() {
        return moodService.getAll();
    }

    @PostMapping("/add")
    public ResponseEntity<Mood> add(@Valid @RequestBody MoodDTO moodDTO) {
        try {
            return ok(moodService.add(moodDTO));
        } catch (MoodAlreadyExistsException e) {
            return notFound().build();
        }
    }

    @PutMapping("/update/{moodId}")
    public ResponseEntity<Mood> update(@PathVariable Integer moodId, @RequestBody MoodDTO moodDTO) {
        try {
            return ok(moodService.update(moodId, moodDTO));
        } catch (MoodNotFoundException e) {
            return badRequest().build();
        }
    }

    @DeleteMapping("/delete/{moodId}")
    public ResponseEntity<Mood> delete(@PathVariable Integer moodId) {
        try {
            moodService.delete(moodId);
            return ok().build();
        } catch (MoodNotFoundException e) {
            return badRequest().build();
        }
    }

}
