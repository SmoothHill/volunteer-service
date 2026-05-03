package com.yueping.volunteer.file;

import com.yueping.volunteer.dto.UploadFileResponse;
import com.yueping.volunteer.properties.StorageProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@ConditionalOnProperty(prefix = "app.storage", name = "type", havingValue = "local", matchIfMissing = true)
public class LocalStorageService implements StorageService {

    private final StorageProperties storageProperties;

    public LocalStorageService(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
    }

    @Override
    public UploadFileResponse store(MultipartFile file, String category) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }
        String originalName = file.getOriginalFilename();
        String extension = StringUtils.getFilenameExtension(originalName);
        String safeExtension = extension == null ? "dat" : extension.toLowerCase();
        String normalizedCategory = normalizeCategory(category);
        String relativeDir = normalizedCategory + "/" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String fileName = UUID.randomUUID().toString().replace("-", "") + "." + safeExtension;
        Path root = rootPath();
        Path targetDir = root.resolve(relativeDir).normalize();
        if (!targetDir.startsWith(root)) {
            throw new IllegalArgumentException("Invalid storage path");
        }
        Path targetFile = targetDir.resolve(fileName);
        try {
            Files.createDirectories(targetDir);
            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new IllegalArgumentException("文件保存失败");
        }
        String url = normalizePublicPrefix(storageProperties.getPublicPrefix()) + "/" + relativeDir.replace("\\", "/") + "/" + fileName;
        return new UploadFileResponse(originalName, url, file.getSize());
    }

    @Override
    public String storeBytes(byte[] content, String category, String fileName, String contentType) {
        String normalizedCategory = normalizeCategory(category);
        String safeFileName = StringUtils.hasText(fileName) ? fileName.trim() : UUID.randomUUID().toString().replace("-", "") + ".dat";
        Path root = rootPath();
        Path targetDir = root.resolve(normalizedCategory).normalize();
        if (!targetDir.startsWith(root)) {
            throw new IllegalArgumentException("Invalid storage path");
        }
        Path targetFile = targetDir.resolve(safeFileName).normalize();
        if (!targetFile.startsWith(root)) {
            throw new IllegalArgumentException("Invalid storage path");
        }
        try {
            Files.createDirectories(targetDir);
            Files.write(targetFile, content);
        } catch (IOException ex) {
            throw new IllegalArgumentException("文件保存失败");
        }
        Path relative = root.relativize(targetFile);
        return normalizePublicPrefix(storageProperties.getPublicPrefix()) + "/" + relative.toString().replace("\\", "/");
    }

    @Override
    public boolean exists(String publicUrl) {
        Path resolved = resolvePublicUrl(publicUrl);
        return resolved != null && Files.exists(resolved);
    }

    @Override
    public StoredFile load(String publicUrl) {
        Path resolved = resolvePublicUrl(publicUrl);
        if (resolved == null || !Files.exists(resolved)) {
            return null;
        }
        try {
            InputStream inputStream = Files.newInputStream(resolved);
            String contentType = Files.probeContentType(resolved);
            long size = Files.size(resolved);
            return new StoredFile(inputStream, size, contentType, resolved.getFileName().toString());
        } catch (IOException ex) {
            throw new IllegalArgumentException("读取文件失败");
        }
    }

    Path resolvePublicUrl(String publicUrl) {
        if (!StringUtils.hasText(publicUrl)) {
            return null;
        }
        String requestPath = publicUrl.trim();
        if (requestPath.startsWith("http://") || requestPath.startsWith("https://")) {
            try {
                requestPath = new URI(requestPath).getPath();
            } catch (URISyntaxException ex) {
                throw new IllegalArgumentException("文件地址格式不正确");
            }
        }
        String publicPrefix = normalizePublicPrefix(storageProperties.getPublicPrefix());
        if (!requestPath.startsWith(publicPrefix)) {
            return null;
        }
        String relativePath = requestPath.substring(publicPrefix.length());
        while (relativePath.startsWith("/")) {
            relativePath = relativePath.substring(1);
        }
        if (!StringUtils.hasText(relativePath)) {
            return null;
        }
        Path root = rootPath();
        Path resolved = root.resolve(relativePath.replace("/", File.separator)).normalize();
        if (!resolved.startsWith(root)) {
            throw new IllegalArgumentException("文件访问路径不合法");
        }
        return resolved;
    }

    Path rootPath() {
        return Paths.get(storageProperties.getRootDir()).toAbsolutePath().normalize();
    }

    private String normalizePublicPrefix(String value) {
        String text = StringUtils.hasText(value) ? value.trim() : "/files";
        if (!text.startsWith("/")) {
            text = "/" + text;
        }
        while (text.endsWith("/")) {
            text = text.substring(0, text.length() - 1);
        }
        return text;
    }

    private String normalizeCategory(String category) {
        String text = StringUtils.hasText(category) ? category.trim() : "common";
        if (!text.matches("[A-Za-z0-9_\\-/]+")) {
            throw new IllegalArgumentException("Invalid file category");
        }
        return text;
    }
}
