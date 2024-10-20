package com.ubb.zenith.exception;

public class SongNotFoundException extends Exception{
    public SongNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
