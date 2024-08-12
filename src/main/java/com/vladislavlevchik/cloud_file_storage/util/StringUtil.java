package com.vladislavlevchik.cloud_file_storage.util;

import org.springframework.stereotype.Component;

@Component
public class StringUtil {
    private static final long MEGABYTE = 1_048_576; // 1024 * 1024
    private static final long KILOBYTE = 1_024; // 1024

    public String convertBytesToMbOrKb(long sizeInBytes) {
        String formattedSize;

        if (sizeInBytes >= MEGABYTE) {
            double sizeInMB = sizeInBytes / (double) MEGABYTE;
            formattedSize = String.format("%.2fMB", sizeInMB);
        } else {
            double sizeInKB = sizeInBytes / (double) KILOBYTE;
            formattedSize = String.format("%.2fKB", sizeInKB);
        }

        return formattedSize;
    }

    public String getMainSubdirectory(String path) {
        String strippedPath = path.substring(path.indexOf('/') + 1);

        int nextSlashIndex = strippedPath.indexOf('/');

        if (nextSlashIndex != -1) {
            return strippedPath.substring(0, nextSlashIndex);
        } else {
            return "";
        }
    }

    /**
     * Extracts the base directory from a full path by removing the initial part and retaining only the directory.
     *
     * @param fullPath The full path to the file or directory, e.g., "user-Vlad/files/png/award-yel.png".
     * @return The base directory, e.g., "files/png".
     */
    public String getSubdirectories(String fullPath) {
        fullPath = fullPath.replaceFirst("^[^/]+/", "");

        if (fullPath.startsWith("deleted/")) {
            fullPath = fullPath.substring("deleted/".length());
        }

        int lastSlashIndex = fullPath.lastIndexOf('/');

        return (lastSlashIndex == -1) ? "" : fullPath.substring(0, lastSlashIndex);
    }

    public String getFileName(String filePath) {
        int lastSlashIndex = filePath.lastIndexOf('/');
        return (lastSlashIndex == -1) ? filePath : filePath.substring(lastSlashIndex + 1);
    }

}
