package com.ubb.zenith.controller;

import com.ubb.zenith.dto.UserDTO;
import com.ubb.zenith.exception.UserAlreadyExistsException;
import com.ubb.zenith.exception.UserNotFoundException;
import com.ubb.zenith.model.User;
import com.ubb.zenith.service.UserService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll() {
        // Arrange
        User user = new User();
        user.setUsername("testUser");
        when(userService.getAll()).thenReturn(Collections.singletonList(user));

        // Act
        List<User> result = userController.getAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals("testUser", result.get(0).getUsername());
        verify(userService, times(1)).getAll();
    }

    @Test
    void addSuccess() throws UserAlreadyExistsException {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        User user = new User();
        user.setUsername("testUser");
        when(userService.add(userDTO)).thenReturn(user);

        // Act
        ResponseEntity<User> response = userController.add(userDTO);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("testUser", response.getBody().getUsername());
        verify(userService, times(1)).add(userDTO);
    }

    @Test
    void addUserAlreadyExists() throws UserAlreadyExistsException {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        when(userService.add(userDTO)).thenThrow(new UserAlreadyExistsException("User already exists"));

        // Act
        ResponseEntity<User> response = userController.add(userDTO);

        // Assert
        assertEquals(409, response.getStatusCodeValue());
        verify(userService, times(1)).add(userDTO);
    }

    @Test
    void updateSuccess() throws UserNotFoundException {
        // Arrange
        String username = "testUser";
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        User updatedUser = new User();
        updatedUser.setUsername(username);

        when(userService.update(username, userDTO)).thenReturn(updatedUser);

        // Act
        ResponseEntity<User> response = userController.update(username, userDTO);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(username, response.getBody().getUsername());
        verify(userService, times(1)).update(username, userDTO);
    }

    @Test
    void updateUserNotFound() throws UserNotFoundException {
        // Arrange
        String username = "testUser";
        UserDTO userDTO = new UserDTO();
        when(userService.update(username, userDTO)).thenThrow(new UserNotFoundException("User not found"));

        // Act
        ResponseEntity<User> response = userController.update(username, userDTO);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        verify(userService, times(1)).update(username, userDTO);
    }

    @Test
    void deleteSuccess() throws UserNotFoundException {
        // Arrange
        String username = "testUser";
        doNothing().when(userService).delete(username);

        // Act
        ResponseEntity<Void> response = userController.delete(username);

        // Assert
        assertEquals(204, response.getStatusCodeValue());
        verify(userService, times(1)).delete(username);
    }

    @Test
    void deleteUserNotFound() throws UserNotFoundException {
        // Arrange
        String username = "testUser";
        doThrow(new UserNotFoundException("User not found")).when(userService).delete(username);

        // Act
        ResponseEntity<Void> response = userController.delete(username);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        verify(userService, times(1)).delete(username);
    }

    @Test
    void saveSpotifyTokensSuccess() throws UserNotFoundException {
        // Arrange
        String username = "testUser";
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        int expiresIn = 3600;

        doNothing().when(userService).saveTokens(username, accessToken, refreshToken, expiresIn);

        // Act
        ResponseEntity<String> response = userController.saveSpotifyTokens(username, accessToken, refreshToken, expiresIn);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Spotify tokens saved successfully for testUser", response.getBody());
        verify(userService, times(1)).saveTokens(username, accessToken, refreshToken, expiresIn);
    }

    @Test
    void saveSpotifyTokensUserNotFound() throws UserNotFoundException {
        // Arrange
        String username = "testUser";
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        int expiresIn = 3600;

        doThrow(new UserNotFoundException("User not found")).when(userService).saveTokens(username, accessToken, refreshToken, expiresIn);

        // Act
        try {
            userController.saveSpotifyTokens(username, accessToken, refreshToken, expiresIn);
        } catch (UserNotFoundException e) {
            // Assert
            assertEquals("User not found", e.getMessage());
        }

        verify(userService, times(1)).saveTokens(username, accessToken, refreshToken, expiresIn);
    }

    @SneakyThrows
    @Test
    void getSpotifyAccessTokenSuccess() {
        // Arrange
        String username = "testUser";
        String accessToken = "accessToken";
        when(userService.getAccessToken(username)).thenReturn(accessToken);

        // Act
        ResponseEntity<String> response = userController.getSpotifyAccessToken(username);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(accessToken, response.getBody());
        verify(userService, times(1)).getAccessToken(username);
    }

    @SneakyThrows
    @Test
    void getSpotifyAccessTokenFailure() {
        // Arrange
        String username = "testUser";
        when(userService.getAccessToken(username)).thenThrow(new RuntimeException("Token retrieval failed"));

        // Act
        ResponseEntity<String> response = userController.getSpotifyAccessToken(username);

        // Assert
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Failed to retrieve access token: Token retrieval failed", response.getBody());
        verify(userService, times(1)).getAccessToken(username);
    }
}