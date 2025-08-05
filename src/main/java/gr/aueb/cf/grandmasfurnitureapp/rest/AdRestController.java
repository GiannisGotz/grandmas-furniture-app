package gr.aueb.cf.grandmasfurnitureapp.rest;

import gr.aueb.cf.grandmasfurnitureapp.core.exceptions.AppObjectInvalidArgumentException;
import gr.aueb.cf.grandmasfurnitureapp.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.grandmasfurnitureapp.core.exceptions.ValidationException;
import gr.aueb.cf.grandmasfurnitureapp.core.filters.AdFilters;
import gr.aueb.cf.grandmasfurnitureapp.dto.AdInsertDTO;
import gr.aueb.cf.grandmasfurnitureapp.dto.AdReadOnlyDTO;
import gr.aueb.cf.grandmasfurnitureapp.dto.ResponseMessageDTO;
import gr.aueb.cf.grandmasfurnitureapp.mapper.Mapper;
import gr.aueb.cf.grandmasfurnitureapp.model.User;
import gr.aueb.cf.grandmasfurnitureapp.service.AdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/ads")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
public class AdRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdRestController.class);
    private final AdService adService;
    private final Mapper mapper;

    @GetMapping
    public ResponseEntity<Page<AdReadOnlyDTO>> getAllAds(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        try {
            Page<AdReadOnlyDTO> ads = adService.getPaginatedSortedAds(page, size, sortBy, sortDirection);
            return ResponseEntity.ok(ads);
        } catch (Exception e) {
            LOGGER.error("Error getting ads", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//    @PostMapping
//    public ResponseEntity<AdReadOnlyDTO> createAd (
//            @AuthenticationPrincipal User loggedInUser,
//            @Valid @RequestBody AdInsertDTO adInsertDTO,
//            BindingResult bindingResult)
//            throws AppObjectNotFoundException, ValidationException, AppObjectInvalidArgumentException {
//
//        if (bindingResult.hasErrors()) {
//            throw new ValidationException(bindingResult);
//        }
//
//
//        AdReadOnlyDTO createdAd = adService.createAd(loggedInUser, adInsertDTO);
//        return new ResponseEntity<>(createdAd, HttpStatus.CREATED);
//    }



//    @PostMapping("/with-image")
//    public ResponseEntity<AdReadOnlyDTO> createAdWithImage(
//            @Valid @RequestPart("ad") AdInsertDTO adInsertDTO,
//            @RequestPart(value = "image", required = false) MultipartFile imageFile,
//            Authentication authentication) {
//        try {
//            Long userId = getUserIdFromAuthentication(authentication);
//            AdReadOnlyDTO createdAd = adService.createAdWithImage(adInsertDTO, userId, imageFile);
//            return ResponseEntity.status(HttpStatus.CREATED).body(createdAd);
//        } catch (Exception e) {
//            LOGGER.error("Error creating ad with image", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//
//
//    @GetMapping("/filtered")
//    public ResponseEntity<List<AdReadOnlyDTO>> getFilteredAds(@ModelAttribute AdFilters filters) {
//        try {
//            List<AdReadOnlyDTO> ads = adService.getAdsFiltered(filters);
//            return ResponseEntity.ok(ads);
//        } catch (Exception e) {
//            LOGGER.error("Error getting filtered ads", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<AdReadOnlyDTO> getAdById(@PathVariable Long id) {
//        try {
//            AdReadOnlyDTO ad = adService.getAdById(id);
//            return ResponseEntity.ok(ad);
//        } catch (AppObjectNotFoundException e) {
//            return ResponseEntity.notFound().build();
//        } catch (Exception e) {
//            LOGGER.error("Error getting ad by id: " + id, e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<AdReadOnlyDTO>> getAdsByUser(@PathVariable Long userId) {
//        try {
//            List<AdReadOnlyDTO> ads = adService.getAdsByUserId(userId);
//            return ResponseEntity.ok(ads);
//        } catch (Exception e) {
//            LOGGER.error("Error getting ads by user: " + userId, e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    @GetMapping("/available")
//    public ResponseEntity<List<AdReadOnlyDTO>> getAvailableAds() {
//        try {
//            List<AdReadOnlyDTO> ads = adService.getAvailableAds();
//            return ResponseEntity.ok(ads);
//        } catch (Exception e) {
//            LOGGER.error("Error getting available ads", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<AdReadOnlyDTO> updateAd(
//            @PathVariable Long id,
//            @Valid @RequestBody AdInsertDTO adInsertDTO,
//            Authentication authentication) {
//        try {
//            Long userId = getUserIdFromAuthentication(authentication);
//            AdReadOnlyDTO updatedAd = adService.updateAd(id, adInsertDTO, userId);
//            return ResponseEntity.ok(updatedAd);
//        } catch (AppObjectNotFoundException e) {
//            return ResponseEntity.notFound().build();
//        } catch (Exception e) {
//            LOGGER.error("Error updating ad: " + id, e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<ResponseMessageDTO> deleteAd(
//            @PathVariable Long id,
//            Authentication authentication) {
//        try {
//            Long userId = getUserIdFromAuthentication(authentication);
//            adService.deleteAdWithAuth(id, userId);
//            return ResponseEntity.ok(new ResponseMessageDTO("Ad deleted successfully"));
//        } catch (AppObjectNotFoundException e) {
//            return ResponseEntity.notFound().build();
//        } catch (Exception e) {
//            LOGGER.error("Error deleting ad: " + id, e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    private Long getUserIdFromAuthentication(Authentication authentication) {
//        // This is a placeholder - you'll need to implement this based on your JWT structure
//        // Typically you'd extract the username and look up the user ID
//        String username = authentication.getName();
//        // Return user ID based on username - you'll need to implement this
//        return 1L; // Placeholder
//    }
}


