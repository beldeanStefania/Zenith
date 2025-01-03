package com.ubb.zenith.controller;

import com.ubb.zenith.dto.LoginDTO;
import com.ubb.zenith.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;
    

    @Autowired
    private JwtUtil jwtUtil;
    /**
     * Endpoint for user login.
     * Authenticates the user and returns a JWT token upon successful login.
     *
     * @param loginDTO The login credentials (username and password) provided by the user.
     * @return A JWT token if authentication is successful, or an error message otherwise.
     */
    @PostMapping("/login")
    public String login(@RequestBody LoginDTO loginDTO) {
        try {
            // Create an authentication token using the provided username and password.

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()
                    )
            );

            // Generate a JWT token for the authenticated user.
            String token = jwtUtil.generateToken(loginDTO.getUsername());
            return token;
        } catch (AuthenticationException e) {
            // Handle authentication failures and return an error message.

            return "Error: " + e.getMessage();
        }
    }
}
