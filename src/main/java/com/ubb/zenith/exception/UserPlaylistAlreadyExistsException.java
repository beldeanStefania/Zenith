package com.ubb.zenith.exception;

public class UserPlaylistAlreadyExistsException extends Exception {
    public UserPlaylistAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
