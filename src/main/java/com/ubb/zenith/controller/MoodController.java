package com.ubb.zenith.controller;

import com.ubb.zenith.dto.MoodDTO;
import com.ubb.zenith.exception.MoodAlreadyExistsException;
import com.ubb.zenith.exception.MoodNotFoundException;
import com.ubb.zenith.model.Mood;
import com.ubb.zenith.service.MoodService;
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

    /**
     * Retrieves all mood entries from the repository.
     *
     * @return a list of all moods in the database
     */
    @GetMapping("/getAll")
    public List<Mood> getAll() {
        return moodService.getAll();
    }

    /**
     * Adds a new mood after verifying if a mood with the same scores already exists.
     *
     * @param moodDTO DTO object that contains the information of the mood to be added.
     * @return the added mood.
     * @throws MoodAlreadyExistsException if a mood with the same scores already exists.
     */
    @PostMapping("/add")
    public ResponseEntity<Mood> add(@RequestBody MoodDTO moodDTO) {
        try {
            return ok(moodService.add(moodDTO));
        } catch (MoodAlreadyExistsException e) {
            return notFound().build();
        }
    }

    /**
     * Updates a mood entry in the repository.
     *
     * @param moodId the ID of the mood to be updated.
     * @param moodDTO the DTO object containing the new information of the mood.
     * @return the updated mood.
     */
    @PutMapping("/update/{moodId}")
    public ResponseEntity<Mood> update(@PathVariable Integer moodId, @RequestBody MoodDTO moodDTO) {
        try {
            return ok(moodService.update(moodId, moodDTO));
        } catch (MoodNotFoundException e) {
            return badRequest().build();
        }
    }

    /**
     * Deletes a mood entry from the repository.
     *
     * @param moodId the ID of the mood to be deleted.
     * @return the deleted mood.
     */
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
