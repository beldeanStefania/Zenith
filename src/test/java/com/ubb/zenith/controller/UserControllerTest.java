package com.ubb.zenith.controller;

import com.ubb.zenith.dto.UserDTO;
import com.ubb.zenith.exception.UserAlreadyExistsException;
import com.ubb.zenith.exception.UserNotFoundException;
import com.ubb.zenith.model.User;
import com.ubb.zenith.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void shouldGetAllUsers() throws Exception {
        User user1 = new User();
        user1.setUsername("user1");
        User user2 = new User();
        user2.setUsername("user2");

        List<User> users = Arrays.asList(user1, user2);

        when(userService.getAll()).thenReturn(users);

        mockMvc.perform(get("/api/user/getAll"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'username':'user1'}, {'username':'user2'}]"))
                .andDo(print());

        verify(userService).getAll();
    }

    @Test
    public void shouldAddUsersSuccssfully() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("newUser");
        User newUser = new User();
        newUser.setUsername("newUser");

        when(userService.add(any(UserDTO.class))).thenReturn(newUser);

        mockMvc.perform(post("/api/user/add")
                        .contentType(APPLICATION_JSON)
                        .content("{\"username\": \"newUser\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'username':'newUser'}"))
                .andDo(print());

        verify(userService).add(any(UserDTO.class));
    }

    @Test
    public void shouldThrowExceptionWhenAddingUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("existingUser");

        when(userService.add(any(UserDTO.class))).thenThrow(new UserAlreadyExistsException("User already exists"));

        mockMvc.perform(post("/api/user/add")
                        .contentType(APPLICATION_JSON)
                        .content("{\"username\": \"existingUser\"}"))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(userService).add(any(UserDTO.class));
    }

    @Test
    public void shouldUpdateUserSuccessfully() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("updatedUser");

        User updatedUser = new User();
        updatedUser.setUsername("updatedUser");

        when(userService.update(eq("oldUsername"), any(UserDTO.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/user/update/oldUsername")
                        .contentType(APPLICATION_JSON)
                        .content("{\"username\": \"updatedUser\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'username':'updatedUser'}"))
                .andDo(print());

        verify(userService).update(eq("oldUsername"), any(UserDTO.class));
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("updatedUser");

        when(userService.update(eq("oldUsername"), any(UserDTO.class))).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(put("/api/user/update/oldUsername")
                        .contentType(APPLICATION_JSON)
                        .content("{\"username\": \"updatedUser\"}"))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(userService).update(eq("oldUsername"), any(UserDTO.class));
    }

    @Test
    public void shouldDeleteUserSuccessfully() throws Exception {
        mockMvc.perform(delete("/api/user/delete/userToDelete"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(userService).delete("userToDelete");
    }

    @Test
    public void shouldThrowExceptionWhenDeletingUser() throws Exception {
        doThrow(new UserNotFoundException("User not found")).when(userService).delete("userToDelete");

        mockMvc.perform(delete("/api/user/delete/userToDelete"))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(userService).delete("userToDelete");
    }
}