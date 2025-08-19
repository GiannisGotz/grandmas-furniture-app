package gr.aueb.cf.grandmasfurnitureapp.rest;

import gr.aueb.cf.grandmasfurnitureapp.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.grandmasfurnitureapp.dto.UserReadOnlyDTO;
import gr.aueb.cf.grandmasfurnitureapp.service.UserService;
import gr.aueb.cf.grandmasfurnitureapp.dto.UserRoleUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *REST controller for user management operations
 */
@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Users", description = "User management operations")
public class UserRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);
    private final UserService userService;


    /**
     /**
     * Retrieves a page of users.
     *
     * @param page the zero-based page index (default = 0)
     * @param size the number of items per page (default = 10)
     * @return ResponseEntity with 200 OK and paginated users
     */
    @GetMapping("/paginated")
    @Operation(summary = "Get paginated users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<UserReadOnlyDTO>> getPaginatedUsers(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        Page<UserReadOnlyDTO> users = userService.getPaginatedUsers(page, size);
        return ResponseEntity.ok(users);
    }

    /**
     * Retrieves a page of users sorted by the specified field and direction.
     * @param page          the zero-based page index (default = 0)
     * @param size          the number of items per page (default = 10)
     * @param sortBy        the field to sort by (default = "id")
     * @param sortDirection the direction of sorting: "asc" or "desc" (default = "asc")
     * @return ResponseEntity with 200 OK and paginated sorted users
     */

    @GetMapping("/paginated/sorted")
    @Operation(summary = "Get paginated sorted users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sorted users retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid sort parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<UserReadOnlyDTO>> getPaginatedSortedUsers(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)") @RequestParam(defaultValue = "asc") String sortDirection) {
        Page<UserReadOnlyDTO> users = userService.getPaginatedSortedUsers(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(users);
    }



    /**
     * Deletes a user by username.
     *
     * @param username the username of the user to delete
     * @return ResponseEntity with 200 OK if deletion is successful
     * @throws AppObjectNotFoundException if no user exists with the provided username
     */
    @DeleteMapping("/{username}")
    @Operation(summary = "Deletes a user", description = "Deletes user by username (Admin only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User deleted successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"message\": \"User deleted successfully\"}"))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"code\": \"userNotFound\", \"description\": \"User with username not found\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"code\": \"userNotAuthenticated\", \"description\": \"User must authenticate\"}"))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"code\": \"userNotAuthorized\", \"description\": \"Admin access required\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    public ResponseEntity<Void> deleteUser(@Parameter(description = "Username of the user to delete") @PathVariable String username) throws AppObjectNotFoundException {
        userService.deleteUser(username);
        LOGGER.info("User with username {} deleted successfully", username);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    /**
     * Updates a user's role.
     *
     * @param userId the ID of the user to update
     * @param roleUpdateDTO the new role information
     * @return ResponseEntity with 200 OK if update is successful
     * @throws AppObjectNotFoundException if no user exists with the provided ID
     */
    @PutMapping("/{userId}/role")
    @Operation(summary = "Update user role", description = "Updates user role by ID (Admin only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User role updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"message\": \"User role updated successfully\"}"))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"code\": \"userNotFound\", \"description\": \"User with ID not found\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid role",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"error\": \"Invalid role specified\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"code\": \"userNotAuthenticated\", \"description\": \"User must authenticate\"}"))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"code\": \"userNotAuthorized\", \"description\": \"Admin access required\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    public ResponseEntity<Void> updateUserRole(
            @Parameter(description = "ID of the user to update") @PathVariable Long userId,
            @Parameter(description = "Role update information") @RequestBody UserRoleUpdateDTO roleUpdateDTO) throws AppObjectNotFoundException {
        userService.updateUserRole(userId, roleUpdateDTO.getRole());
        LOGGER.info("User with ID {} role updated to {}", userId, roleUpdateDTO.getRole());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
