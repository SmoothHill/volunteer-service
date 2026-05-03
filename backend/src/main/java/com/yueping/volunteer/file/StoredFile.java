package com.yueping.volunteer.file;

import java.io.InputStream;

public class StoredFile {

    private final InputStream inputStream;
    private final long size;
    private final String contentType;
    private final String fileName;

    public StoredFile(InputStream inputStream, long size, String contentType, String fileName) {
        this.inputStream = inputStream;
        this.size = size;
        this.contentType = contentType;
        this.fileName = fileName;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public long getSize() {
        return size;
    }

    public String getContentType() {
        return contentType;
    }

    public String getFileName() {
        return fileName;
    }
}
