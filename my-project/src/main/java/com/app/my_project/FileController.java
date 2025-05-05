package com.app.my_project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            String fileName = file.getOriginalFilename();
            String uploadDir = "my-project/src/main/resources/static/uploads/";
            String targetLocation = uploadDir + fileName;
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path path = Paths.get(targetLocation);

            Files.write(path, bytes);

            return "File uploaded successfully";
        } catch (IOException e) {
            return "Failed to upload file: " + e.getMessage();
        }
    }

    @DeleteMapping("/remove/{fileName}")
    public String removeFile(@PathVariable String fileName) {
        String uploadDir = "my-project/src/main/resources/static/uploads/";
        Path uploadPath = Paths.get(uploadDir + fileName);

        if (Files.exists(uploadPath)) {
            try {
                Files.delete(uploadPath);

                return "File removed successfully";
            } catch (IOException e) {
                return "Failed to remove file: " + e.getMessage();
            }
        }

        return "File not found";
    }

}

