package gr.aueb.cf.grandmasfurnitureapp.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


/**
 * Simple file service for ad images.
 * Stores images in: src/main/resources/uploads/ads/{adId}/image.{ext}
 */
@Service
public class FileService {

    public String storeAdImage(MultipartFile file, Long adId) {
        try {
            String extension = file.getOriginalFilename().substring(
                    file.getOriginalFilename().lastIndexOf(".") + 1);

            Path adDir = Paths.get("src/main/resources/uploads/ads/" + adId);
            Files.createDirectories(adDir);

            Path targetPath = adDir.resolve("image." + extension);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/ads/" + adId + "/image." + extension;
        } catch (Exception e) {
            throw new RuntimeException("File upload failed", e);
        }
    }

    public void deleteAdImage(Long adId) {
        try {
            Path adDir = Paths.get("src/main/resources/uploads/ads/" + adId);
            if (Files.exists(adDir)) {
                Files.walk(adDir).forEach(path -> {
                    try { Files.delete(path); } catch (Exception ignored) {}
                });
            }
        } catch (Exception ignored) {}
    }

    /**
     * Extracts file extension from filename.
     * @param filename The original filename
     * @return File extension (lowercase) or "jpg" as default
     */
    public String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty() || !filename.contains(".")) {
            return "jpg";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }
}
