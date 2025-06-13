package org.example.cinema_reservation_system.service.impl;

import org.example.cinema_reservation_system.service.storage.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

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
    public String saveFile(MultipartFile file, String subDir) {
        try {
            Path dir = Paths.get(uploadDir, subDir).toAbsolutePath().normalize();
            Files.createDirectories(dir);

            String filename = UUID.randomUUID() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
            Path filePath = dir.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + subDir + "/" + filename; // Trả về URL tương đối để hiển thị
        } catch (IOException ex) {
            throw new RuntimeException("Lỗi khi lưu file: " + ex.getMessage());
        }
    }

}
