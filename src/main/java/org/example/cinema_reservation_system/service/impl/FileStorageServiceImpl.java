package org.example.cinema_reservation_system.service.impl;


import org.example.cinema_reservation_system.service.storage.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;


@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public String saveFile(MultipartFile file, String folder) {
        String newFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path folderPath = Paths.get("uploads", folder);

        try {
            Files.createDirectories(folderPath); // Tạo folder nếu chưa có
            Path filePath = folderPath.resolve(newFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Không thể lưu file: " + file.getOriginalFilename(), e);
        }

        // Trả về đường dẫn đầy đủ có thể Ctrl + Click được
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(folder + "/")
                .path(newFileName)
                .toUriString();
    }
}
