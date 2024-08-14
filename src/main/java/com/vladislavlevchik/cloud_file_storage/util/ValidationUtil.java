package com.vladislavlevchik.cloud_file_storage.util;

import com.vladislavlevchik.cloud_file_storage.exception.FilePathException;
import com.vladislavlevchik.cloud_file_storage.exception.UploadFileException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class ValidationUtil {

    public void validateFiles(List<MultipartFile> files) {
        if (files == null) {
            throw new UploadFileException("Files parameter not passed");
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new UploadFileException("One or more files are empty.");
            }
        }
    }

    public void validateUploadPath(String path) {
        if (path != null && !isValidPath(path)) {
            throw new FilePathException("The path must match the format img, img/png, files/photo/img");
        }
    }

    public void validatePath(String path) {
        if (path == null) {
            throw new FilePathException("You forgot to pass the parameter");
        }
        if (!isValidPath(path)) {
            throw new FilePathException("The path must match the format img, img/png, files/photo/img");
        }
    }

    private boolean isValidPath(String path) {
        return path.matches("([a-zA-Z0-9]+/)*[a-zA-Z0-9]+");
    }

}
