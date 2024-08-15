package com.vladislavlevchik.cloud_file_storage.exception;

public class FolderAlreadyExistException extends RuntimeException{

    public FolderAlreadyExistException(String message) {
        super(message);
    }
}
