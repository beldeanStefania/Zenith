package com.ubb.zenith.exception;

public class SongAlreadyExistsException extends Exception{
    public SongAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
