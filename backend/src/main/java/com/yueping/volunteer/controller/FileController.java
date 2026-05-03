package com.yueping.volunteer.controller;

import com.yueping.volunteer.auth.AuthRequired;
import com.yueping.volunteer.common.ApiResponse;
import com.yueping.volunteer.dto.UploadFileResponse;
import com.yueping.volunteer.file.FileStorageService;
import com.yueping.volunteer.file.StoredFile;
import com.yueping.volunteer.model.UserRole;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping
public class FileController {

    private final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @AuthRequired(roles = {UserRole.VOLUNTEER, UserRole.ADMIN})
    @PostMapping("/api/files/upload")
    public ApiResponse<UploadFileResponse> upload(@RequestParam("file") MultipartFile file,
                                                  @RequestParam(value = "category", defaultValue = "common") String category) {
        return ApiResponse.ok("上传成功", fileStorageService.store(file, category));
    }

    @GetMapping("/files/**")
    public ResponseEntity<byte[]> viewFile(HttpServletRequest request) throws IOException {
        StoredFile storedFile = fileStorageService.load(request.getRequestURI());
        if (storedFile == null) {
            return ResponseEntity.notFound().build();
        }
        try (InputStream inputStream = storedFile.getInputStream()) {
            MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
            if (storedFile.getContentType() != null && !storedFile.getContentType().trim().isEmpty()) {
                mediaType = MediaType.parseMediaType(storedFile.getContentType());
            }
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CACHE_CONTROL, "public, max-age=31536000")
                    .body(readAllBytes(inputStream));
        }
    }

    private byte[] readAllBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }
        return outputStream.toByteArray();
    }
}
