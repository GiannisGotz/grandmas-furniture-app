package gr.aueb.cf.grandmasfurnitureapp.rest;

import gr.aueb.cf.grandmasfurnitureapp.authentication.AuthenticationService;
import gr.aueb.cf.grandmasfurnitureapp.core.exceptions.*;
import gr.aueb.cf.grandmasfurnitureapp.dto.AuthenticationRequestDTO;
import gr.aueb.cf.grandmasfurnitureapp.dto.AuthenticationResponseDTO;
import gr.aueb.cf.grandmasfurnitureapp.dto.UserInsertDTO;
import gr.aueb.cf.grandmasfurnitureapp.dto.UserReadOnlyDTO;
import gr.aueb.cf.grandmasfurnitureapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


/**
 * REST controller for user authentication and registration endpoints.
 */
@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User authentication and registration")
public class AuthRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthRestController.class);
    private final AuthenticationService authenticationService;
    private final UserService userService;


    /**
     * Authenticates user credentials for successful login and returns JWT token.
     *
     * @param authenticationRequestDTO containing username and password
     * @return  ResponseEntity containing the generated JWT and its expiration details
     * @throws AppObjectNotAuthorizedException if the provided credentials are invalid
     */
    @PostMapping("/login")
    @Operation(summary = "User login")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AuthenticationResponseDTO> authenticate(@RequestBody AuthenticationRequestDTO authenticationRequestDTO)
            throws AppObjectNotAuthorizedException {
        AuthenticationResponseDTO authenticationResponseDTO = authenticationService.authenticate(authenticationRequestDTO);
        return new ResponseEntity<>(authenticationResponseDTO, HttpStatus.OK);
    }


    /**
     * Registers a new user
     *
     * @param userInsertDTO containing required user input data.
     * @param bindingResult holds validation errors, if any.
     * @return ResponseEntity containing the created {@link UserReadOnlyDTO} and Location header
     * @throws AppObjectAlreadyExists if a user with the same username or email exists
     * @throws ValidationException if the request data are invalid
     */
    @PostMapping("/register")
    @Operation(summary = "User registration")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or validation errors"),
            @ApiResponse(responseCode = "409", description = "User already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserReadOnlyDTO> registerUser(
            @Valid @RequestBody UserInsertDTO userInsertDTO,
            BindingResult bindingResult)
            throws AppObjectAlreadyExists , ValidationException {

        LOGGER.info("Register User Request: {}", userInsertDTO);

        if (bindingResult.hasErrors()) {
            LOGGER.error("Registering errors: {}", bindingResult.getAllErrors());
            throw new ValidationException(bindingResult);
        }

        try {
            UserReadOnlyDTO userReadOnlyDTO = userService.registerUser(userInsertDTO);
            LOGGER.info("User registered.");
            return new ResponseEntity<>(userReadOnlyDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error("Registration Error:", e);
            throw e;
        }
    }


}

