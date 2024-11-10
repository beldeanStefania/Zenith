package com.ubb.zenith.service;
/**
import com.ubb.zenith.config.ApplicationConfig;
import com.ubb.zenith.controller.AuthenticationRequest;
import com.ubb.zenith.controller.AuthenticationResponse;*/
import com.ubb.zenith.controller.AuthenticationRequest;
import com.ubb.zenith.controller.AuthenticationResponse;
import com.ubb.zenith.dto.UserDTO;
import com.ubb.zenith.exception.UserAlreadyExistsException;
import com.ubb.zenith.exception.UserNotFoundException;
import com.ubb.zenith.model.MyUserDetails;
import com.ubb.zenith.model.User;
import com.ubb.zenith.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;git
   private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    /**
     * Retrieves all users from the repository.
     *
     * @return a list of all available users.
     */
    public List<User> getAll() {
        return userRepository.findAll();
    }

    /**
     * Adds a new user after verifying if a user with the same username already exists.
     *
     * @param userDTO DTO object that contains the information of the user to be added.
     * @return the added user.
     * @throws UserAlreadyExistsException if a user with the same username already exists.
     */
    public User add(final UserDTO userDTO) throws UserAlreadyExistsException {
        checkIfUserAlreadyExists(userDTO.getUsername());
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        var user = buildUser(userDTO);

        return userRepository.save(user);
       // var jwt = jwtService.generateToken(user);
        //return AuthenticationResponse.builder().token(jwt).build();
    }

    /**
     * Checks if a user with a specific username exists in the repository.
     *
     * @param username the username of the user to be checked.
     * @throws UserAlreadyExistsException if the user already exists.
     */
    public void checkIfUserAlreadyExists(final String username) throws UserAlreadyExistsException {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }
    }

    /**
     * Builds a user object from a user DTO.
     *
     * @param userDTO the DTO object to build the user from.
     * @return the built user.
     */
    public User buildUser(final UserDTO userDTO) {
        var user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setAge(userDTO.getAge());
        return user;
    }

    /**
     * Adds a new user to the repository.
     *
     * @param user the user to be added.
     * @return the added user.
     */
    public User add(User user) {
        return userRepository.save(user);
    }

    /**
     * Updates a user with the specified username.
     *
     * @param oldUsername the username of the user to be updated.
     * @param userDTO the DTO object containing the new information for the user.
     * @return the updated user.
     * @throws UserNotFoundException if the user is not found.
     */
    public User update(final String oldUsername, final UserDTO userDTO) throws UserNotFoundException {
        return updateUsername(findUser(oldUsername), userDTO);
    }

    /**
     * Finds a user by username.
     *
     * @param username the username of the user to be searched.
     * @return the found user.
     * @throws UserNotFoundException if no user with the specified username is found.
     */
    public User findUser(final String username) throws UserNotFoundException {
        var modifiedUser = userRepository.findAll().stream()
                .filter(user -> user.getUsername().equals(username)).findFirst().orElseThrow(() -> new UserNotFoundException("User not found"));
        return modifiedUser;
    }

    /**
     * Updates a user with the specified username.
     *
     * @param user the user to be updated.
     * @param userDTO the DTO object containing the new information for the user.
     * @return the updated user.
     */
    public User updateUsername(final User user, final UserDTO userDTO) {
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setAge(userDTO.getAge());
        return userRepository.save(user);
    }

    /**
     * Deletes a user from the repository based on its username.
     *
     * @param username the username of the user to delete.
     * @throws UserNotFoundException if no user with the specified username is found.
     */
    public void delete(final String username) throws UserNotFoundException {
        findUser(username);
        userRepository.delete(findUser(username));
    }



}
