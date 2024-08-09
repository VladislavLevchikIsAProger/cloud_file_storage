package com.vladislavlevchik.cloud_file_storage.util;

import org.springframework.stereotype.Component;

@Component
public class BytesConverter {
    private static final long MEGABYTE = 1_048_576; // 1024 * 1024
    private static final long KILOBYTE = 1_024; // 1024

    public String convertToMbOrKb(long sizeInBytes) {
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

}
