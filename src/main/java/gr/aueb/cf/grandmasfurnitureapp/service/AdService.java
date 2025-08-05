package gr.aueb.cf.grandmasfurnitureapp.service;

import gr.aueb.cf.grandmasfurnitureapp.core.exceptions.AppObjectInvalidArgumentException;
import gr.aueb.cf.grandmasfurnitureapp.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.grandmasfurnitureapp.core.filters.AdFilters;
import gr.aueb.cf.grandmasfurnitureapp.core.filters.Paginated;
import gr.aueb.cf.grandmasfurnitureapp.core.specifications.AdSpecification;
import gr.aueb.cf.grandmasfurnitureapp.dto.AdInsertDTO;
import gr.aueb.cf.grandmasfurnitureapp.dto.AdReadOnlyDTO;
import gr.aueb.cf.grandmasfurnitureapp.mapper.Mapper;
import gr.aueb.cf.grandmasfurnitureapp.model.Ad;
import gr.aueb.cf.grandmasfurnitureapp.model.Attachment;
import gr.aueb.cf.grandmasfurnitureapp.model.User;
import gr.aueb.cf.grandmasfurnitureapp.repository.AdRepository;
import gr.aueb.cf.grandmasfurnitureapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing furniture ads.
 * Handles ad business logic and delegates file operations to AttachmentService.
 */
@Service
@RequiredArgsConstructor
public class AdService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdService.class);

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final AttachmentService attachmentService;
    private final Mapper mapper;

    /**
     * Creates a new ad with optional image.
     *
     * @param user The authenticated user creating the ad
     * @param dto Ad data
     * @param image Optional image file
     * @return Created ad as DTO
     */
    @Transactional
    public AdReadOnlyDTO createAd(User user, AdInsertDTO dto, MultipartFile image)
            throws AppObjectNotFoundException, AppObjectInvalidArgumentException {

        LOGGER.info("Creating ad '{}' for user: {}", dto.getTitle(), user.getUsername());

        // Validate image if provided
        if (image != null && !image.isEmpty() && !attachmentService.isValidImageFile(image)) {
            throw new AppObjectInvalidArgumentException("Image", "Invalid image file");
        }

        // Create and save the ad
        Ad ad = mapper.mapToAdEntity(dto);
        ad.setUser(user);
        ad = adRepository.save(ad);

        // Handle image attachment
        if (image != null && !image.isEmpty()) {
            LOGGER.info("Processing image for ad ID: {}", ad.getId());
            Attachment attachment = attachmentService.createAdAttachment(image, ad.getId());
            ad.setImage(attachment);
            ad = adRepository.save(ad);
        }

        LOGGER.info("Ad created successfully with ID: {}", ad.getId());
        return mapper.mapToAdReadOnlyDTO(ad);
    }

    /**
     * Updates an existing ad and optionally replaces its image.
     *
     * @param adId The ad ID to update
     * @param dto Updated ad data
     * @param newImage Optional new image file
     * @return Updated ad as DTO
     */
    @Transactional
    public AdReadOnlyDTO updateAd(Long adId, AdInsertDTO dto, MultipartFile newImage)
            throws AppObjectNotFoundException, AppObjectInvalidArgumentException {

        LOGGER.info("Updating ad ID: {}", adId);

        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new AppObjectNotFoundException("Ad", "Ad with ID " + adId + " not found"));

        // Validate new image if provided
        if (newImage != null && !newImage.isEmpty() && !attachmentService.isValidImageFile(newImage)) {
            throw new AppObjectInvalidArgumentException("Image", "Invalid image file");
        }

        // Update ad fields
        updateAdFields(ad, dto);

        // Handle image update
        if (newImage != null && !newImage.isEmpty()) {
            LOGGER.info("Updating image for ad ID: {}", adId);

            if (ad.getImage() != null) {
                // Update existing attachment
                Attachment updatedAttachment = attachmentService.updateAdAttachment(
                        ad.getImage(), newImage, adId);
                ad.setImage(updatedAttachment);
            } else {
                // Create new attachment
                Attachment newAttachment = attachmentService.createAdAttachment(newImage, adId);
                ad.setImage(newAttachment);
            }
        }

        ad = adRepository.save(ad);
        LOGGER.info("Ad updated successfully: {}", adId);
        return mapper.mapToAdReadOnlyDTO(ad);
    }

    /**
     * Deletes an ad and its associated image.
     *
     * @param adId The ad ID to delete
     */
    @Transactional
    public void deleteAd(Long adId) throws AppObjectNotFoundException {
        LOGGER.info("Deleting ad ID: {}", adId);

        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new AppObjectNotFoundException("Ad", "Ad with ID " + adId + " not found"));

        // Delete attachment if exists
        if (ad.getImage() != null) {
            attachmentService.deleteAdAttachment(ad.getImage(), adId);
        }

        // Delete the ad
        adRepository.delete(ad);
        LOGGER.info("Ad deleted successfully: {}", adId);
    }

    /**
     * Gets a single ad by ID.
     */
    @Transactional
    public AdReadOnlyDTO getAdById(Long adId) throws AppObjectNotFoundException {
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new AppObjectNotFoundException("Ad", "Ad with ID " + adId + " not found"));
        return mapper.mapToAdReadOnlyDTO(ad);
    }

    /**
     * Gets all available ads.
     */
    @Transactional
    public List<AdReadOnlyDTO> getAvailableAds() {
        return adRepository.findByIsAvailableTrue()
                .stream()
                .map(mapper::mapToAdReadOnlyDTO)
                .collect(Collectors.toList());
    }

    /**
     * Gets ads by user ID.
     */
    @Transactional
    public List<AdReadOnlyDTO> getAdsByUserId(Long userId) {
        return adRepository.findByUserId(userId)
                .stream()
                .map(mapper::mapToAdReadOnlyDTO)
                .collect(Collectors.toList());
    }

    // PAGINATION AND FILTERING METHODS (unchanged)

    public Page<AdReadOnlyDTO> getPaginatedAds(int page, int size) {
        String defaultSort = "id";
        Pageable pageable = PageRequest.of(page, size, Sort.by(defaultSort).ascending());
        return adRepository.findAll(pageable).map(mapper::mapToAdReadOnlyDTO);
    }

    @Transactional
    public Page<AdReadOnlyDTO> getPaginatedSortedAds(int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return adRepository.findAll(pageable).map(mapper::mapToAdReadOnlyDTO);
    }

    @Transactional
    public List<AdReadOnlyDTO> getAdsFiltered(AdFilters filters) {
        return adRepository.findAll(getSpecsFromFilters(filters))
                .stream().map(mapper::mapToAdReadOnlyDTO).collect(Collectors.toList());
    }

    @Transactional
    public Paginated<AdReadOnlyDTO> getAdsFilteredPaginated(AdFilters filters) {
        var filtered = adRepository.findAll(getSpecsFromFilters(filters), filters.getPageable());
        return new Paginated<>(filtered.map(mapper::mapToAdReadOnlyDTO));
    }

    private Specification<Ad> getSpecsFromFilters(AdFilters filters) {
        return AdSpecification.adTitleLike(filters.getTitle())
                .and(AdSpecification.adCategoryIs(filters.getCategoryId()))
                .and(AdSpecification.adCategoryNameLike(filters.getCategoryName()))
                .and(AdSpecification.adConditionIs(filters.getCondition()))
                .and(AdSpecification.adPriceBetween(filters.getMinPrice(), filters.getMaxPrice()))
                .and(AdSpecification.adCityIs(filters.getCityId()))
                .and(AdSpecification.adCityNameLike(filters.getCityName()))
                .and(AdSpecification.adUserIs(filters.getUserId()))
                .and(AdSpecification.adIsAvailable(filters.getIsAvailable()))
                .and(AdSpecification.adDescriptionLike(filters.getDescription()));
    }

    /**
     * Helper method to update ad fields from DTO.
     */
    private void updateAdFields(Ad ad, AdInsertDTO dto) {
        ad.setTitle(dto.getTitle());
        ad.setCategory(dto.getCategory());
        ad.setCondition(dto.getCondition());
        ad.setPrice(dto.getPrice());
        ad.setAvailable(dto.isAvailable());
        ad.setDescription(dto.getDescription());
    }
}