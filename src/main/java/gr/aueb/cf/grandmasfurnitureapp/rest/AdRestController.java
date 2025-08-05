package gr.aueb.cf.grandmasfurnitureapp.rest;

import gr.aueb.cf.grandmasfurnitureapp.core.exceptions.AppObjectInvalidArgumentException;
import gr.aueb.cf.grandmasfurnitureapp.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.grandmasfurnitureapp.core.exceptions.ValidationException;
import gr.aueb.cf.grandmasfurnitureapp.core.filters.AdFilters;
import gr.aueb.cf.grandmasfurnitureapp.core.filters.Paginated;
import gr.aueb.cf.grandmasfurnitureapp.dto.AdInsertDTO;
import gr.aueb.cf.grandmasfurnitureapp.dto.AdReadOnlyDTO;
import gr.aueb.cf.grandmasfurnitureapp.dto.ResponseMessageDTO;
import gr.aueb.cf.grandmasfurnitureapp.model.User;
import gr.aueb.cf.grandmasfurnitureapp.service.AdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * REST Controller for managing furniture ads.
 * Handles CRUD operations for ads with optional image attachments.
 */
@RestController
@RequestMapping("/api/ads")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Ads", description = "Furniture ads management")
public class AdRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdRestController.class);
    private final AdService adService;

    /**
     * Creates a new ad with optional image.
     * Accepts multipart form data with ad JSON and image file.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create new ad", description = "Creates a new furniture ad with optional image upload")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ad created successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AdReadOnlyDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"title\": \"Title is required\", \"price\": \"Price must be positive\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"code\": \"userNotAuthenticated\", \"description\": \"User must authenticate\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    public ResponseEntity<AdReadOnlyDTO> createAd(
            @AuthenticationPrincipal User user,
            @Parameter(description = "Ad data as JSON")
            @RequestPart("ad") @Valid AdInsertDTO adDto,
            @Parameter(description = "Image file (optional)")
            @RequestPart(value = "image", required = false) MultipartFile image,
            BindingResult bindingResult)
            throws AppObjectNotFoundException, ValidationException, AppObjectInvalidArgumentException {

        LOGGER.info("Creating ad request from user: {}", user.getUsername());

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        AdReadOnlyDTO createdAd = adService.createAd(user, adDto, image);
        LOGGER.info("Ad created successfully with ID: {}", createdAd.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdAd);
    }

    /**
     * Updates an existing ad with optional image replacement.
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update ad", description = "Updates an existing ad with optional image replacement")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ad updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AdReadOnlyDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"title\": \"Title is required\", \"price\": \"Price must be positive\"}"))),
            @ApiResponse(responseCode = "404", description = "Ad not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"code\": \"adNotFound\", \"description\": \"Ad with ID not found\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"code\": \"userNotAuthenticated\", \"description\": \"User must authenticate\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    public ResponseEntity<AdReadOnlyDTO> updateAd(
            @Parameter(description = "Ad ID") @PathVariable Long id,
            @Parameter(description = "Updated ad data as JSON")
            @RequestPart("ad") @Valid AdInsertDTO adDto,
            @Parameter(description = "New image file (optional)")
            @RequestPart(value = "image", required = false) MultipartFile image,
            BindingResult bindingResult)
            throws AppObjectNotFoundException, ValidationException, AppObjectInvalidArgumentException {

        LOGGER.info("Updating ad ID: {}", id);

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        AdReadOnlyDTO updatedAd = adService.updateAd(id, adDto, image);
        LOGGER.info("Ad updated successfully: {}", id);

        return ResponseEntity.ok(updatedAd);
    }

    /**
     * Deletes an ad and its associated image.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete ad", description = "Deletes an ad and its associated image")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ad deleted successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseMessageDTO.class))),
            @ApiResponse(responseCode = "404", description = "Ad not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"code\": \"adNotFound\", \"description\": \"Ad with ID not found\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"code\": \"userNotAuthenticated\", \"description\": \"User must authenticate\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    public ResponseEntity<ResponseMessageDTO> deleteAd(
            @Parameter(description = "Ad ID") @PathVariable Long id)
            throws AppObjectNotFoundException {

        LOGGER.info("Deleting ad ID: {}", id);
        adService.deleteAd(id);
        LOGGER.info("Ad deleted successfully: {}", id);

        return ResponseEntity.ok(new ResponseMessageDTO("SUCCESS", "Ad deleted successfully"));
    }

    /**
     * Gets a single ad by ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get ad by ID", description = "Retrieves a single ad with its details and image")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ad found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AdReadOnlyDTO.class))),
            @ApiResponse(responseCode = "404", description = "Ad not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"code\": \"adNotFound\", \"description\": \"Ad with ID not found\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    public ResponseEntity<AdReadOnlyDTO> getAdById(
            @Parameter(description = "Ad ID") @PathVariable Long id)
            throws AppObjectNotFoundException {

        AdReadOnlyDTO ad = adService.getAdById(id);
        return ResponseEntity.ok(ad);
    }

    /**
     * Gets all available ads.
     */
    @GetMapping("/available")
    @Operation(summary = "Get available ads", description = "Retrieves all available ads")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Available ads retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(type = "array", implementation = AdReadOnlyDTO.class)))
    })
    public ResponseEntity<List<AdReadOnlyDTO>> getAvailableAds() {
        List<AdReadOnlyDTO> ads = adService.getAvailableAds();
        return ResponseEntity.ok(ads);
    }

    /**
     * Gets ads by user ID.
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get ads by user", description = "Retrieves all ads created by a specific user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User ads retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(type = "array", implementation = AdReadOnlyDTO.class)))
    })
    public ResponseEntity<List<AdReadOnlyDTO>> getAdsByUser(
            @Parameter(description = "User ID") @PathVariable Long userId) {

        List<AdReadOnlyDTO> ads = adService.getAdsByUserId(userId);
        return ResponseEntity.ok(ads);
    }

    /**
     * Gets ads with pagination and sorting.
     */
    @GetMapping
    @Operation(summary = "Get paginated ads", description = "Retrieves ads with pagination and sorting")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paginated ads retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<AdReadOnlyDTO>> getPaginatedAds(
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field")
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)")
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Page<AdReadOnlyDTO> ads = adService.getPaginatedSortedAds(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(ads);
    }

    /**
     * Gets filtered ads with advanced search.
     */
    @GetMapping("/search")
    @Operation(summary = "Search ads", description = "Advanced ad search with filters")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Filtered ads retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(type = "array", implementation = AdReadOnlyDTO.class)))
    })
    public ResponseEntity<List<AdReadOnlyDTO>> searchAds(
            @Parameter(description = "Ad title filter")
            @RequestParam(required = false) String title,
            @Parameter(description = "Category ID filter")
            @RequestParam(required = false) Long categoryId,
            @Parameter(description = "Category name filter")
            @RequestParam(required = false) String categoryName,
            @Parameter(description = "Condition filter")
            @RequestParam(required = false) String condition,
            @Parameter(description = "Minimum price")
            @RequestParam(required = false) String minPrice,
            @Parameter(description = "Maximum price")
            @RequestParam(required = false) String maxPrice,
            @Parameter(description = "City ID filter")
            @RequestParam(required = false) Long cityId,
            @Parameter(description = "City name filter")
            @RequestParam(required = false) String cityName,
            @Parameter(description = "User ID filter")
            @RequestParam(required = false) Long userId,
            @Parameter(description = "Availability filter")
            @RequestParam(required = false) Boolean isAvailable,
            @Parameter(description = "Description filter")
            @RequestParam(required = false) String description) {

        // Build filters from request parameters
        AdFilters filters = AdFilters.builder()
                .title(title)
                .categoryId(categoryId)
                .categoryName(categoryName)
                .condition(condition != null ?
                        gr.aueb.cf.grandmasfurnitureapp.core.enums.Condition.valueOf(condition.toUpperCase()) : null)
                .minPrice(minPrice != null ? new java.math.BigDecimal(minPrice) : null)
                .maxPrice(maxPrice != null ? new java.math.BigDecimal(maxPrice) : null)
                .cityId(cityId)
                .cityName(cityName)
                .userId(userId)
                .isAvailable(isAvailable)
                .description(description)
                .build();

        List<AdReadOnlyDTO> ads = adService.getAdsFiltered(filters);
        return ResponseEntity.ok(ads);
    }

    /**
     * Gets filtered ads with pagination.
     */
    @GetMapping("/search/paginated")
    @Operation(summary = "Search ads with pagination", description = "Advanced ad search with filters and pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paginated filtered ads retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Paginated.class)))
    })
    public ResponseEntity<Paginated<AdReadOnlyDTO>> searchAdsPaginated(
            @ModelAttribute AdFilters filters) {

        Paginated<AdReadOnlyDTO> ads = adService.getAdsFilteredPaginated(filters);
        return ResponseEntity.ok(ads);
    }

    /**
     * Gets current user's ads.
     */
    @GetMapping("/my-ads")
    @Operation(summary = "Get current user's ads", description = "Retrieves all ads created by the authenticated user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User's ads retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(type = "array", implementation = AdReadOnlyDTO.class)))
    })
    public ResponseEntity<List<AdReadOnlyDTO>> getMyAds(
            @AuthenticationPrincipal User user) {

        List<AdReadOnlyDTO> ads = adService.getAdsByUserId(user.getId());
        return ResponseEntity.ok(ads);
    }
}