package com.vladislavlevchik.cloud_file_storage.exception;

public class SameNameException extends RuntimeException{

    public SameNameException(String message) {
        super(message);
    }
}
