package com.vladislavlevchik.cloud_file_storage.exception;

public class FileNotFoundException extends RuntimeException{

    public FileNotFoundException(String message) {
        super(message);
    }
}
