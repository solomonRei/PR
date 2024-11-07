package com.pr.parser.service;

import com.pr.parser.config.DocumentStorageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class FileService {
    private final DocumentStorageProperties documentStorageProperties;

    public void saveFile(MultipartFile fileToSave) throws IOException {
        if (fileToSave == null) {
            throw new NullPointerException("fileToSave is null");
        }

        Path storagePath = Path.of(documentStorageProperties.getFileStoragePath());
        Path targetFile = storagePath.resolve(fileToSave.getOriginalFilename()).normalize();

        if (!Files.exists(storagePath)) {
            Files.createDirectories(storagePath);
        }

        if (!targetFile.startsWith(storagePath)) {
            throw new SecurityException("Unsupported filename! Potential directory traversal attempt detected.");
        }

        Files.copy(fileToSave.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
    }
}
