package gr.aueb.cf.grandmasfurnitureapp.rest;

import gr.aueb.cf.grandmasfurnitureapp.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.grandmasfurnitureapp.dto.UserReadOnlyDTO;
import gr.aueb.cf.grandmasfurnitureapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);
    private final UserService userService;

    @GetMapping("/paginated")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get paginated users", description = "Get users with pagination (Admin only)")
    public ResponseEntity<Page<UserReadOnlyDTO>> getPaginatedUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<UserReadOnlyDTO> users = userService.getPaginatedUsers(page, size);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/paginated/sorted")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get paginated sorted users", description = "Get users with pagination and sorting (Admin only)")
    public ResponseEntity<Page<UserReadOnlyDTO>> getPaginatedSortedUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        Page<UserReadOnlyDTO> users = userService.getPaginatedSortedUsers(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(users);
    }


    @DeleteMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) throws AppObjectNotFoundException {
        userService.deleteUser(username);
        LOGGER.info("User with username {} deleted successfully", username);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
