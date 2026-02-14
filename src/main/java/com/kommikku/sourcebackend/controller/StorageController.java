package com.kommikku.sourcebackend.controller;

import com.kommikku.sourcebackend.dto.storage.StorageUploadResponse;
import com.kommikku.sourcebackend.service.StorageService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Validated
@RestController
@RequestMapping("/api/v1/storage")
public class StorageController {

    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/uploads")
    public StorageUploadResponse upload(
            @RequestParam("path") @NotBlank String objectPath,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        return storageService.upload(objectPath, file.getBytes(), file.getContentType());
    }

    @DeleteMapping("/objects")
    public void delete(@RequestParam("path") @NotBlank String objectPath) {
        storageService.delete(objectPath);
    }
}
