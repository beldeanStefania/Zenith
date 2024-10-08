package com.ubb.zenith.controller;

import com.ubb.zenith.dto.UserDTO;
import com.ubb.zenith.exception.UserAlreadyExistsException;
import com.ubb.zenith.exception.UserNotFoundException;
import com.ubb.zenith.model.User;
import com.ubb.zenith.service.UserService;
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
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getAll")
    public List<User> getAll() {
        return userService.getAll();
    }

    @PostMapping("/add")
    public ResponseEntity<User> add(@RequestBody UserDTO userDTO) {
        try {
            return ok(userService.add(userDTO));
        } catch (UserAlreadyExistsException e) {
            return badRequest().build();
        }
    }

    @PutMapping("/update/{username}")
    public ResponseEntity<User> update(@PathVariable String username, @RequestBody UserDTO userDTO) {
        try {
            return ok(userService.update(username, userDTO));
        } catch (UserNotFoundException e) {
            return badRequest().build();
        }
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<User> delete(@PathVariable String username) {
        try {
            userService.delete(username);
            return ok().build();
        } catch (UserNotFoundException e) {
            return badRequest().build();
        }
    }
}
