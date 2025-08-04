package gr.aueb.cf.grandmasfurnitureapp.rest;

import gr.aueb.cf.grandmasfurnitureapp.authentication.AuthenticationService;
import gr.aueb.cf.grandmasfurnitureapp.core.exceptions.*;
import gr.aueb.cf.grandmasfurnitureapp.dto.AuthenticationRequestDTO;
import gr.aueb.cf.grandmasfurnitureapp.dto.AuthenticationResponseDTO;
import gr.aueb.cf.grandmasfurnitureapp.dto.UserInsertDTO;
import gr.aueb.cf.grandmasfurnitureapp.dto.UserReadOnlyDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthRestController.class);
    private final AuthenticationService authenticationService;
    private final UserService userService;


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(@RequestBody AuthenticationRequestDTO authenticationRequestDTO)
            throws AppObjectNotAuthorizedException {
        AuthenticationResponseDTO authenticationResponseDTO = authenticationService.authenticate(authenticationRequestDTO);
        return new ResponseEntity<>(authenticationResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/register")
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

