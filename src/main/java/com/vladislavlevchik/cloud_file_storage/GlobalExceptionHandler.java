package com.vladislavlevchik.cloud_file_storage;

import com.vladislavlevchik.cloud_file_storage.dto.response.MessageResponseDto;
import com.vladislavlevchik.cloud_file_storage.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public MessageResponseDto handleBadCredentialsException() {
        return MessageResponseDto.builder().message("Invalid username or password").build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return errors;
    }

    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public MessageResponseDto handleMaxUploadSizeExceededException() {
        return MessageResponseDto.builder().message("File size exceeds the maximum limit.").build();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserAlreadyExistException.class)
    @ResponseBody
    public MessageResponseDto handleUserAlreadyExistException(UserAlreadyExistException ex) {
        return MessageResponseDto.builder().message(ex.getMessage()).build();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(FolderAlreadyExistException.class)
    @ResponseBody
    public MessageResponseDto handleFolderAlreadyExistException(FolderAlreadyExistException ex) {
        return MessageResponseDto.builder().message(ex.getMessage()).build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(FolderNotFoundException.class)
    @ResponseBody
    public MessageResponseDto handleFolderNotFoundException(FolderNotFoundException ex) {
        return MessageResponseDto.builder().message(ex.getMessage()).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UploadFileException.class)
    @ResponseBody
    public MessageResponseDto handleUploadFileException(UploadFileException ex) {
        return MessageResponseDto.builder().message(ex.getMessage()).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FilePathException.class)
    @ResponseBody
    public MessageResponseDto handleFilePathException(FilePathException ex) {
        return MessageResponseDto.builder().message(ex.getMessage()).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IncorrectFileNameException.class)
    @ResponseBody
    public MessageResponseDto handleIncorrectFileNameException(IncorrectFileNameException ex) {
        return MessageResponseDto.builder().message(ex.getMessage()).build();
    }
}
