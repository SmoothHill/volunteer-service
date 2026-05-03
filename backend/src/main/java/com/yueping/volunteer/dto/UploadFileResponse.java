package com.yueping.volunteer.dto;

public class UploadFileResponse {

    private String fileName;
    private String url;
    private long size;

    public UploadFileResponse() {
    }

    public UploadFileResponse(String fileName, String url, long size) {
        this.fileName = fileName;
        this.url = url;
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}

