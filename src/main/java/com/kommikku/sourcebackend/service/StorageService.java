package com.kommikku.sourcebackend.service;

import com.kommikku.sourcebackend.dto.storage.StorageUploadResponse;

public interface StorageService {

    StorageUploadResponse upload(String objectPath, byte[] content, String contentType);

    void delete(String objectPath);
}
