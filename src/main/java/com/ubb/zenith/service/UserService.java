package com.ubb.zenith.service;

import com.ubb.zenith.dto.UserDTO;
import com.ubb.zenith.exception.UserAlreadyExistsException;
import com.ubb.zenith.exception.UserNotFoundException;
import com.ubb.zenith.model.User;
import com.ubb.zenith.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User add(final UserDTO userDTO) throws UserAlreadyExistsException {
        checkIfUserAlreadyExists(userDTO.getUsername());
        return add(buildUser(userDTO));
    }

    public void checkIfUserAlreadyExists(final String username) throws UserAlreadyExistsException {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }
    }

    public User buildUser(final UserDTO userDTO) {
        var user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setAge(userDTO.getAge());
        return user;
    }

    public User add(User user) {
        return userRepository.save(user);
    }

    public User update(final String oldUsername, final UserDTO userDTO) throws UserNotFoundException {
        return updateUsername(findUser(oldUsername), userDTO);
    }

    public User findUser(final String username) throws UserNotFoundException {
        var modifiedUser = userRepository.findAll().stream()
                .filter(user -> user.getUsername().equals(username)).findFirst().orElseThrow(() -> new UserNotFoundException("User not found"));
        return modifiedUser;
    }

    public User updateUsername(final User user, final UserDTO userDTO) {
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setAge(userDTO.getAge());
        return userRepository.save(user);
    }

    public void delete(final String username) throws UserNotFoundException {
        findUser(username);
        userRepository.delete(findUser(username));
    }

}
