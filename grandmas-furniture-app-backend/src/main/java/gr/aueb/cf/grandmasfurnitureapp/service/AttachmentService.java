package gr.aueb.cf.grandmasfurnitureapp.service;

import gr.aueb.cf.grandmasfurnitureapp.model.Attachment;
import gr.aueb.cf.grandmasfurnitureapp.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service for handling file attachments and their metadata.
 * Manages both file storage and attachment entity operations.
 */
@Service
@RequiredArgsConstructor
public class AttachmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttachmentService.class);

    private final AttachmentRepository attachmentRepository;
    private final FileService fileService;

    /**
     * Creates and stores an attachment for an ad.
     *
     * @param file The uploaded file
     * @param adId The ad ID to associate with
     * @return Created attachment entity
     */
    @Transactional
    public Attachment createAdAttachment(MultipartFile file, Long adId) {
        LOGGER.info("Creating attachment for ad ID: {}", adId);

        // Store the physical file
        String imagePath = fileService.storeAdImage(file, adId);

        // Create attachment metadata
        Attachment attachment = new Attachment();
        attachment.setFilename(file.getOriginalFilename());
        attachment.setSavedName("image." + fileService.getFileExtension(file.getOriginalFilename()));
        attachment.setFilePath(imagePath);
        attachment.setContentType(file.getContentType());
        attachment.setExtension(fileService.getFileExtension(file.getOriginalFilename()));

        // Save to database
        attachment = attachmentRepository.save(attachment);

        LOGGER.info("Attachment created with ID: {}", attachment.getId());
        return attachment;
    }

    /**
     * Updates an existing attachment with a new file.
     *
     * @param existingAttachment The current attachment to replace
     * @param newFile The new file to upload
     * @param adId The ad ID
     * @return Updated attachment entity
     */
    @Transactional
    public Attachment updateAdAttachment(Attachment existingAttachment, MultipartFile newFile, Long adId) {
        LOGGER.info("Updating attachment ID: {} for ad ID: {}", existingAttachment.getId(), adId);

        // Delete old file
        fileService.deleteAdImage(adId);

        // Store new file
        String imagePath = fileService.storeAdImage(newFile, adId);

        // Update attachment metadata
        existingAttachment.setFilename(newFile.getOriginalFilename());
        existingAttachment.setSavedName("image." + fileService.getFileExtension(newFile.getOriginalFilename()));
        existingAttachment.setFilePath(imagePath);
        existingAttachment.setContentType(newFile.getContentType());
        existingAttachment.setExtension(fileService.getFileExtension(newFile.getOriginalFilename()));

        // Save updated entity
        existingAttachment = attachmentRepository.save(existingAttachment);

        LOGGER.info("Attachment updated successfully");
        return existingAttachment;
    }

    /**
     * Deletes an attachment and its associated file.
     *
     * @param attachment The attachment to delete
     * @param adId The ad ID (for file deletion)
     */
    @Transactional
    public void deleteAdAttachment(Attachment attachment, Long adId) {
        if (attachment == null) {
            return;
        }

        LOGGER.info("Deleting attachment ID: {} for ad ID: {}", attachment.getId(), adId);

        // Delete physical file
        fileService.deleteAdImage(adId);

        // Delete database record
        attachmentRepository.delete(attachment);

        LOGGER.info("Attachment deleted successfully");
    }

    /**
     * Finds attachment by ad ID.
     *
     * @param adId The ad ID
     * @return Attachment if found, null otherwise
     */
    public Attachment findByAdId(Long adId) {
        return attachmentRepository.findByAdId(adId).orElse(null);
    }

    /**
     * Checks if a file is a valid image for attachment.
     *
     * @param file The file to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        // Check content type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return false;
        }

        // Check file extension
        String extension = fileService.getFileExtension(file.getOriginalFilename());
        return extension.matches("jpg|jpeg|png|gif|webp");
    }
}