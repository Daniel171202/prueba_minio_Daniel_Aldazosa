package com.prueba.minio.daniel.aldazosa.prueba_minio.Api;

import java.util.HashMap;
import java.util.Map;

import javax.swing.text.html.FormSubmitEvent.MethodType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.GetPresignedObjectUrlArgs;
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


            // ...

            String response = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(io.minio.http.Method.GET)
                    .bucket("mybucket")
                    .object(file.getOriginalFilename())
                    .build()
            );

            System.out.println(response);
            // Construir la respuesta con el enlace
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("message", "File uploaded successfully!");
            responseMap.put("download_link", response);

            return responseMap.toString();
        } catch (Exception e) {
            return "Error uploading file: " + e.getMessage();
        }
    }
}

