package com.ubb.zenith.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Represents the response returned to the client after a successful authentication.
 * Contains the JWT token issued to the authenticated user.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    /**
     * The JWT token generated for the authenticated user.
     */
    private String token;
}
