package gr.aueb.cf.grandmasfurnitureapp.authentication;

import gr.aueb.cf.grandmasfurnitureapp.security.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

/**
 * JWT Authentication Filter that intercepts incoming HTTP requests to validate JWT tokens.
 * This filter extends OncePerRequestFilter to ensure it is executed once per request.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Intercepts and processes incoming requests to validate JWT tokens.
     * Extracts the token from the Authorization header and authenticates the user if valid.
     *
     * @param request     The incoming HTTP request.
     * @param response    The HTTP response to be sent back to the client.
     * @param filterChain The filter chain to pass the request to the next filter.
     * @throws ServletException If there is a servlet processing error.
     * @throws IOException      If an input or output error occurs during filtering.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        if (request.getServletPath().startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        // Extract the Authorization header
        String authHeader = request.getHeader("Authorization");
        String jwt;
        String username;

        // Continue the filter chain if the header is missing or invalid
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            LOGGER.debug("Missing or malformed authorization header");
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the JWT token from the Authorization header
        jwt = authHeader.substring(7);
        LOGGER.info("Extracted JWT: {}", jwt);


        try {
            // Extract username from the token
            username = jwtService.extractSubject(jwt);

            // Authenticate if the user is not already authenticated
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Validate the token and set authentication in the SecurityContext
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    LOGGER.info("Populating SecurityContextHolder for user: {}", username);
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    LOGGER.warn("Token is not valid {}", request.getRequestURI());
                }
            }
        } catch (ExpiredJwtException e) {
            // Handle expired token exception and respond with 401 Unauthorized
            LOGGER.warn("WARN: Token is expired ", e);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            String jsonBody = "{\"code\": \"expired token\", \"message\": \"" + e.getMessage() + "\"}";
            response.getWriter().write(jsonBody);
            return;
        } catch (Exception e) {
            // Handle general token parsing or validation errors
            LOGGER.warn("WARN: Something went wrong while parsing JWT ", e);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            String jsonBody = "{\"code\": \"invalid token\", \"description\": \"" + e.getMessage() + "\"}";
            response.getWriter().write(jsonBody);
            return;
        }

        // Continue the request processing if no issues occur
        filterChain.doFilter(request, response);
    }
}
