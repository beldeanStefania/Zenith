package com.ubb.zenith.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ubb.zenith.dto.MoodDTO;
import com.ubb.zenith.exception.MoodAlreadyExistsException;
import com.ubb.zenith.exception.MoodNotFoundException;
import com.ubb.zenith.model.Mood;
import com.ubb.zenith.repository.MoodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MoodServiceTests {

    @Mock
    private MoodRepository moodRepository;

    @InjectMocks
    private MoodService moodService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll_ReturnsMoodList() {
        List<Mood> mockMoods = List.of(new Mood(), new Mood());
        when(moodRepository.findAll()).thenReturn(mockMoods);

        List<Mood> result = moodService.getAll();

        assertEquals(2, result.size());
        verify(moodRepository, times(1)).findAll();
    }

    @Test
    void testCheckIfMoodAlreadyExists_ThrowsExceptionWhenMoodExists() {
        MoodDTO moodDTO = new MoodDTO();
        moodDTO.setHappiness_score(5);
        moodDTO.setLove_score(4);
        moodDTO.setSadness_score(3);
        moodDTO.setEnergy_score(2);

        Mood existingMood = new Mood();
        existingMood.setHappiness_score(5);
        existingMood.setLove_score(4);
        existingMood.setSadness_score(3);
        existingMood.setEnergy_score(2);

        when(moodRepository.findAll()).thenReturn(List.of(existingMood));

        assertThrows(MoodAlreadyExistsException.class, () -> moodService.checkIfMoodAlreadyExists(moodDTO));
    }

    @Test
    void testMoodExists_ReturnsTrueWhenMoodExists() {
        Mood existingMood = new Mood();
        existingMood.setHappiness_score(5);
        existingMood.setLove_score(4);
        existingMood.setSadness_score(3);
        existingMood.setEnergy_score(2);

        when(moodRepository.findAll()).thenReturn(List.of(existingMood));

        boolean result = moodService.MoodExists(5, 4, 3, 2);

        assertTrue(result);
    }

    @Test
    void testMoodExists_ReturnsFalseWhenMoodDoesNotExist() {
        when(moodRepository.findAll()).thenReturn(new ArrayList<>());

        boolean result = moodService.MoodExists(5, 4, 3, 2);

        assertFalse(result);
    }

    @Test
    void testAddMood_SuccessfullyAddsMood() throws MoodAlreadyExistsException {
        MoodDTO moodDTO = new MoodDTO();
        moodDTO.setHappiness_score(5);
        moodDTO.setLove_score(4);
        moodDTO.setSadness_score(3);
        moodDTO.setEnergy_score(2);

        Mood newMood = new Mood();
        newMood.setHappiness_score(5);
        newMood.setLove_score(4);
        newMood.setSadness_score(3);
        newMood.setEnergy_score(2);

        when(moodRepository.save(any(Mood.class))).thenReturn(newMood);

        Mood result = moodService.add(moodDTO);

        assertNotNull(result);
        assertEquals(newMood, result);
        verify(moodRepository, times(1)).save(any(Mood.class));
    }

    @Test
    void testAddMood_ThrowsMoodAlreadyExistsException() {
        MoodDTO moodDTO = new MoodDTO();
        moodDTO.setHappiness_score(5);
        moodDTO.setLove_score(4);
        moodDTO.setSadness_score(3);
        moodDTO.setEnergy_score(2);

        Mood existingMood = new Mood();
        existingMood.setHappiness_score(5);
        existingMood.setLove_score(4);
        existingMood.setSadness_score(3);
        existingMood.setEnergy_score(2);

        when(moodRepository.findAll()).thenReturn(List.of(existingMood));

        assertThrows(MoodAlreadyExistsException.class, () -> moodService.add(moodDTO));
    }

    @Test
    void testDeleteMood_DeletesMoodSuccessfully() throws MoodNotFoundException {
        Mood mood = new Mood();
        when(moodRepository.findById(1)).thenReturn(Optional.of(mood));

        moodService.delete(1);

        verify(moodRepository, times(1)).delete(mood);
    }

    @Test
    void testDeleteMood_ThrowsMoodNotFoundException() {
        when(moodRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(MoodNotFoundException.class, () -> moodService.delete(1));
    }

    @Test
    void testUpdateMood_SuccessfullyUpdatesMood() throws MoodNotFoundException {
        Mood existingMood = new Mood();
        MoodDTO moodDTO = new MoodDTO();
        moodDTO.setHappiness_score(7);
        moodDTO.setLove_score(6);
        moodDTO.setSadness_score(5);
        moodDTO.setEnergy_score(4);

        when(moodRepository.findById(1)).thenReturn(Optional.of(existingMood));
        when(moodRepository.save(any(Mood.class))).thenReturn(existingMood);

        Mood result = moodService.update(1, moodDTO);

        assertEquals(7, result.getHappiness_score());
        assertEquals(6, result.getLove_score());
        assertEquals(5, result.getSadness_score());
        assertEquals(4, result.getEnergy_score());
        verify(moodRepository, times(1)).save(existingMood);
    }

    @Test
    void testUpdateMood_ThrowsMoodNotFoundException() {
        MoodDTO moodDTO = new MoodDTO();
        moodDTO.setHappiness_score(7);
        moodDTO.setLove_score(6);
        moodDTO.setSadness_score(5);
        moodDTO.setEnergy_score(4);

        when(moodRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(MoodNotFoundException.class, () -> moodService.update(1, moodDTO));
    }

    @Test
    void testFindMood_ReturnsMoodWhenFound() throws MoodNotFoundException {
        Mood mood = new Mood();
        when(moodRepository.findById(1)).thenReturn(Optional.of(mood));

        Mood result = moodService.findMood(1);

        assertNotNull(result);
        assertEquals(mood, result);
    }

    @Test
    void testFindMood_ThrowsMoodNotFoundException() {
        when(moodRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(MoodNotFoundException.class, () -> moodService.findMood(1));
    }
}
