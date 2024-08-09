package com.vladislavlevchik.cloud_file_storage.exception;

public class FolderNotFoundException extends RuntimeException{

    public FolderNotFoundException(String message) {
        super(message);
    }
}
