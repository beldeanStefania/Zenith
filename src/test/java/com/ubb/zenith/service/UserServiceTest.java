package com.ubb.zenith.service;

import com.ubb.zenith.dto.UserDTO;
import com.ubb.zenith.exception.UserAlreadyExistsException;
import com.ubb.zenith.exception.UserNotFoundException;
import com.ubb.zenith.model.Role;
import com.ubb.zenith.model.User;
import com.ubb.zenith.repository.RoleRepository;
import com.ubb.zenith.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SpotifyAuthService spotifyAuthService;

    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll() {
        // Arrange
        List<User> users = new ArrayList<>();
        users.add(new User());
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void addUserSuccess() throws UserAlreadyExistsException {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password");

        Role role = new Role();
        role.setName("USER");

        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.empty());
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = userService.add(userDTO);

        // Assert
        assertNotNull(result);
        assertEquals(userDTO.getUsername(), result.getUsername());
        assertEquals(userDTO.getEmail(), result.getEmail());
        verify(userRepository, times(1)).findByUsername(userDTO.getUsername());
        verify(roleRepository, times(1)).findByName("USER");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void addUserAlreadyExists() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("existingUser");

        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.of(new User()));

        // Act & Assert
        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> {
            userService.add(userDTO);
        });
        assertEquals("User with this username already exists", exception.getMessage());
        verify(userRepository, times(1)).findByUsername(userDTO.getUsername());
    }

    @Test
    void updateUserSuccess() throws UserNotFoundException {
        // Arrange
        String username = "testUser";
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("new@example.com");
        userDTO.setPassword("newPassword");

        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = userService.update(username, userDTO);

        // Assert
        assertNotNull(result);
        assertEquals(userDTO.getEmail(), result.getEmail());
        assertEquals(userDTO.getPassword(), result.getPassword());
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUserNotFound() {
        // Arrange
        String username = "nonExistentUser";
        UserDTO userDTO = new UserDTO();

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.update(username, userDTO);
        });
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void deleteUserSuccess() throws UserNotFoundException {
        // Arrange
        String username = "testUser";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        // Act
        userService.delete(username);

        // Assert
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void deleteUserNotFound() {
        // Arrange
        String username = "nonExistentUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.delete(username);
        });
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void saveTokensSuccess() throws UserNotFoundException {
        // Arrange
        String username = "testUser";
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        int expiresIn = 3600;

        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        userService.saveTokens(username, accessToken, refreshToken, expiresIn);

        // Assert
        assertEquals(accessToken, user.getSpotifyAccessToken());
        assertEquals(refreshToken, user.getSpotifyRefreshToken());
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getAccessTokenSuccess() throws UserNotFoundException, IOException {
        // Arrange
        String username = "testUser";
        String refreshToken = "refreshToken";
        String newAccessToken = "newAccessToken";

        User user = new User();
        user.setUsername(username);
        user.setSpotifyRefreshToken(refreshToken);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(spotifyAuthService.refreshAccessToken(refreshToken)).thenReturn(newAccessToken);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        String result = userService.getAccessToken(username);

        // Assert
        assertNotNull(result);
        assertEquals(newAccessToken, result);
        verify(userRepository, times(1)).findByUsername(username);
        verify(spotifyAuthService, times(1)).refreshAccessToken(refreshToken);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void findByUsernameSuccess() throws UserNotFoundException {
        // Arrange
        String username = "testUser";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        User result = userService.findByUsername(username);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void findByUsernameNotFound() {
        // Arrange
        String username = "nonExistentUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.findByUsername(username);
        });
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findByUsername(username);
    }
}