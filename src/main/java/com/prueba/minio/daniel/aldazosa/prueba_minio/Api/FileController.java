package com.prueba.minio.daniel.aldazosa.prueba_minio.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private MinioClient minioClient;

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket("mybucket")
                            .object(file.getOriginalFilename())
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .build()
            );
            return "File uploaded successfully!";
        } catch (Exception e) {
            return "Error uploading file: " + e.getMessage();
        }
    }
}

