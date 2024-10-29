package com.ubb.zenith.service;

import com.ubb.zenith.dto.UserDTO;
import com.ubb.zenith.exception.UserAlreadyExistsException;
import com.ubb.zenith.exception.UserNotFoundException;
import com.ubb.zenith.model.User;
import com.ubb.zenith.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddUser_whenUserDoesNotExist_shouldAddUser() throws UserAlreadyExistsException {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("newUser");

        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.empty());

        User user = new User();
        user.setUsername(userDTO.getUsername());

        when(userRepository.save(any(User.class))).thenReturn(user);

        assertEquals("newUser",userService.add(userDTO).getUsername());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testAddUser_whenUserAlreadyExists_shouldThrowException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("existingUser");

        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class, () -> userService.add(userDTO));
    }

    @Test
    void testFindUser_whenUserExists_shouldReturnUser() throws UserNotFoundException {
        String username = "existingUser";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findAll()).thenReturn(List.of(user));

        User foundUser = userService.findUser(username);

        assertNotNull(foundUser);
        assertEquals(username, foundUser.getUsername());
    }

    @Test
    void testFindUser_whenUserDoesNotExist_shouldThrowException() {

        String username = "nonExistentUser";

        when(userRepository.findAll()).thenReturn(List.of());

        assertThrows(UserNotFoundException.class, () -> userService.findUser(username));
    }

    @Test
    void testDeleteUser_whenUserExists_shouldDeleteUser() throws UserNotFoundException {
        String username = "deleteUser";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findAll()).thenReturn(List.of(user));

        userService.delete(username);

        verify(userRepository).delete(user);
    }

    @Test
    void testDeleteUser_whenUserDoesNotExist_shouldThrowException() {

        String username = "nonExistentUser";

        when(userRepository.findAll()).thenReturn(List.of());

        assertThrows(UserNotFoundException.class, () -> userService.delete(username));
    }

    @Test
    void testGetAllUsers_whenUsersExists_shouldReturnUsers(){

        assertNotNull(when(userService.getAll()).thenReturn(List.of()));

    }

    @Test
    void testUpdate_Successful() throws UserNotFoundException {
        User existingUser = new User();
        existingUser.setUsername("oldUser");

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("newUser");

        when(userRepository.findAll()).thenReturn(List.of(existingUser));

        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        User updatedUser = userService.update("oldUser", userDTO);

        assertEquals("newUser", updatedUser.getUsername());

        verify(userRepository).save(existingUser);
    }

}
