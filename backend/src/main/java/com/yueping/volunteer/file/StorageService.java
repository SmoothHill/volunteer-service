package com.yueping.volunteer.file;

import com.yueping.volunteer.dto.UploadFileResponse;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    UploadFileResponse store(MultipartFile file, String category);

    String storeBytes(byte[] content, String category, String fileName, String contentType);

    boolean exists(String publicUrl);

    StoredFile load(String publicUrl);
}
