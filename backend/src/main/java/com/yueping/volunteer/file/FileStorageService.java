package com.yueping.volunteer.file;

import com.yueping.volunteer.dto.UploadFileResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private final StorageService storageService;

    public FileStorageService(StorageService storageService) {
        this.storageService = storageService;
    }

    public UploadFileResponse store(MultipartFile file, String category) {
        return storageService.store(file, category);
    }

    public String storeBytes(byte[] content, String category, String fileName, String contentType) {
        return storageService.storeBytes(content, category, fileName, contentType);
    }

    public boolean exists(String publicUrl) {
        return storageService.exists(publicUrl);
    }

    public StoredFile load(String publicUrl) {
        return storageService.load(publicUrl);
    }
}
