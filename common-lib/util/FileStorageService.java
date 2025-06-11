package com.store.common.util;

import com.store.common.dto.FileInfoResponse;
import com.store.common.exception.ApiException;
import com.store.common.http.ApiCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-path}")
    private String UPLOAD_DIR;

    public FileInfoResponse storeFile(MultipartFile file) {
        if(file == null) return null;
        try {
            String fileName = UUID.randomUUID().toString();

            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null) {
                int dotIndex = originalFilename.lastIndexOf('.');
                if (dotIndex >= 0) {
                    extension = originalFilename.substring(dotIndex);  // ".jpg", ".png" 등
                }
            }

            fileName = fileName + extension.toLowerCase();

            Path dirPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            Path filePath = Paths.get(UPLOAD_DIR + File.separator + fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return FileInfoResponse.builder()
                    .fileUrl(filePath.toString())
                    .fileName(originalFilename)
                    .build();
        } catch (IOException e) {
            throw new ApiException(ApiCode.FILE_UPLOAD_FAIL);
        }
    }
}
