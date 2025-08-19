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
import gr.aueb.cf.grandmasfurnitureapp.model.static_data.Category;
import gr.aueb.cf.grandmasfurnitureapp.model.static_data.City;
import gr.aueb.cf.grandmasfurnitureapp.repository.AdRepository;
import gr.aueb.cf.grandmasfurnitureapp.repository.CategoryRepository;
import gr.aueb.cf.grandmasfurnitureapp.repository.CityRepository;
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
 * Service for ad management operations.
 * Handles CRUD operations for furniture ads with image attachment support.
 */
@Service
@RequiredArgsConstructor
public class AdService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdService.class);

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CityRepository cityRepository;
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

        LOGGER.debug("Creating ad '{}' for user: {}", dto.getTitle(), user.getUsername());

        // Validate image if provided
        validateImageFile(image);

        // Create and save the ad
        Ad ad = mapper.mapToAdEntity(dto);
        ad.setUser(user);
        ad = adRepository.save(ad);

        // Handle image attachment if provided
        handleImageAttachment(ad, image, ad.getId());
        ad = adRepository.save(ad);

        LOGGER.debug("Ad created successfully with ID: {}", ad.getId());
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

        LOGGER.debug("Updating ad ID: {}", adId);

        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new AppObjectNotFoundException("Ad", "Ad with ID " + adId + " not found"));

        // Validate new image if provided
        validateImageFile(newImage);

        // Update ad fields
        updateAdFields(ad, dto);

        // Handle image update if provided
        handleImageAttachment(ad, newImage, adId);

        ad = adRepository.save(ad);
        LOGGER.debug("Ad updated successfully: {}", adId);
        return mapper.mapToAdReadOnlyDTO(ad);
    }

    /**
     * Deletes an ad and its associated image.
     *
     * @param adId The ad ID to delete
     */
    @Transactional
    public void deleteAd(Long adId) throws AppObjectNotFoundException {
        LOGGER.debug("Deleting ad ID: {}", adId);

        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new AppObjectNotFoundException("Ad", "Ad with ID " + adId + " not found"));

        // Delete attachment if exists
        if (ad.getImage() != null) {
            attachmentService.deleteAdAttachment(ad.getImage(), adId);
        }

        // Delete the ad
        adRepository.delete(ad);
        LOGGER.debug("Ad deleted successfully: {}", adId);
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
    public List<AdReadOnlyDTO> getAdsFiltered(AdFilters filters, Long currentUserId) {
        LOGGER.debug("Searching ads with filters: {}", filters);
        
        // Create specification and execute query
        AdFilters safeFilters = createSafeFilters(filters);
        Specification<Ad> spec = getSpecsFromFilters(safeFilters, currentUserId);
        List<Ad> result = adRepository.findAll(spec);
        
        LOGGER.debug("Found {} filtered results", result.size());
        return result.stream().map(mapper::mapToAdReadOnlyDTO).collect(Collectors.toList());
    }

    @Transactional
    public Paginated<AdReadOnlyDTO> getAdsFilteredPaginated(AdFilters filters, Long currentUserId) {
        LOGGER.debug("Searching ads with filters: {}", filters);
        
        // Create specification and execute query
        AdFilters safeFilters = createSafeFilters(filters);
        Pageable pageable = safeFilters.getPageable();
        Specification<Ad> spec = getSpecsFromFilters(safeFilters, currentUserId);
        Page<Ad> result = adRepository.findAll(spec, pageable);
        
        LOGGER.debug("Found {} filtered results", result.getTotalElements());
        return new Paginated<>(result.map(mapper::mapToAdReadOnlyDTO));
    }



    private Specification<Ad> getSpecsFromFilters(AdFilters filters, Long currentUserId) {
        return AdSpecification.adTitleLike(filters.getTitle())
                .and(AdSpecification.adCategoryNameLike(filters.getCategoryName()))
                .and(AdSpecification.adConditionIs(filters.getCondition()))
                .and(AdSpecification.adPriceBetween(filters.getMinPrice(), filters.getMaxPrice()))
                .and(AdSpecification.adCityNameLike(filters.getCityName()))
                .and(AdSpecification.adIsAvailable(filters.getIsAvailable()))
                .and(AdSpecification.adIsMyAds(filters.getMyAds(), currentUserId));
    }

    /**
     * Helper method to create safe filters with defaults.
     */
    private AdFilters createSafeFilters(AdFilters filters) {
        return filters != null ? filters : AdFilters.builder().build();
    }

    /**
     * Helper method to update ad fields from DTO.
     */
    private void updateAdFields(Ad ad, AdInsertDTO dto) throws AppObjectNotFoundException {
        ad.setTitle(dto.getTitle());
        ad.setCondition(dto.getCondition());
        ad.setPrice(dto.getPrice());
        ad.setIsAvailable(dto.getIsAvailable());
        ad.setDescription(dto.getDescription());
        
        // Update category if provided
        if (dto.getCategoryName() != null) {
            ad.setCategory(findCategoryByName(dto.getCategoryName()));
        }
        
        // Update city if provided
        if (dto.getCityName() != null) {
            ad.setCity(findCityByName(dto.getCityName()));
        }
    }
    
    /**
     * Helper method to find category by name.
     */
    private Category findCategoryByName(String categoryName) throws AppObjectNotFoundException {
        return categoryRepository.findByCategory(categoryName)
                .orElseThrow(() -> new AppObjectNotFoundException("Category", "Category not found: " + categoryName));
    }
    
    /**
     * Helper method to find city by name.
     */
    private City findCityByName(String cityName) throws AppObjectNotFoundException {
        return cityRepository.findByCityName(cityName)
                .orElseThrow(() -> new AppObjectNotFoundException("City", "City not found: " + cityName));
    }
    
    /**
     * Helper method to validate image file.
     */
    private void validateImageFile(MultipartFile image) throws AppObjectInvalidArgumentException {
        if (image != null && !image.isEmpty() && !attachmentService.isValidImageFile(image)) {
            throw new AppObjectInvalidArgumentException("Image", "Invalid image file");
        }
    }
    
    /**
     * Helper method to handle image attachment creation or update.
     */
    private void handleImageAttachment(Ad ad, MultipartFile image, Long adId) {
        if (image != null && !image.isEmpty()) {
            if (ad.getImage() != null) {
                // Update existing attachment
                Attachment updatedAttachment = attachmentService.updateAdAttachment(
                        ad.getImage(), image, adId);
                ad.setImage(updatedAttachment);
            } else {
                // Create new attachment
                Attachment newAttachment = attachmentService.createAdAttachment(image, adId);
                ad.setImage(newAttachment);
            }
        }
    }
}