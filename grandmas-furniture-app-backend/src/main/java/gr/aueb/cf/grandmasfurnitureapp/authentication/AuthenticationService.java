package gr.aueb.cf.grandmasfurnitureapp.authentication;

import gr.aueb.cf.grandmasfurnitureapp.core.exceptions.AppObjectNotAuthorizedException;
import gr.aueb.cf.grandmasfurnitureapp.dto.AuthenticationRequestDTO;
import gr.aueb.cf.grandmasfurnitureapp.dto.AuthenticationResponseDTO;
import gr.aueb.cf.grandmasfurnitureapp.model.*;
import gr.aueb.cf.grandmasfurnitureapp.repository.UserRepository;
import gr.aueb.cf.grandmasfurnitureapp.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;


    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO dto)
            throws AppObjectNotAuthorizedException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AppObjectNotAuthorizedException("User", "User not authorized"));

        String token = jwtService.generateToken(authentication.getName(), user.getRole().name());
        return new AuthenticationResponseDTO(user.getFirstName(), user.getLastName(), token, user.getRole().name());
    }
}
