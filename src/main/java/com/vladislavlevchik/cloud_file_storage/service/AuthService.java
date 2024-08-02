package com.vladislavlevchik.cloud_file_storage.service;

import com.vladislavlevchik.cloud_file_storage.dto.StatusResponseDto;
import com.vladislavlevchik.cloud_file_storage.dto.UserLoginRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;

    /**
     * Authenticates a user with the provided credentials and sets up the security context.
     *
     * <p>This method uses the provided username and password to authenticate the user and sets the authentication
     * information in the security context. It also creates or retrieves an HTTP session and stores the security context
     * in the session.</p>
     *
     * @param userLoginRequestDto an object containing the user's username and password.
     * @param request             the {@link HttpServletRequest} used to get or create an HTTP session.
     */
    public void signIn(UserLoginRequestDto userLoginRequestDto, HttpServletRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginRequestDto.getUsername(), userLoginRequestDto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
    }

    /**
     * Retrieves the current authentication status and user details.
     *
     * <p>This method checks if the user is authenticated and returns a {@link StatusResponseDto}
     * containing the authentication status and username.</p>
     *
     * <p>If the request is not authenticated, the {@link CustomAuthenticationEntryPoint} will handle
     * the error by returning a 401 (Unauthorized) response with {"authenticated": false}.</p>
     *
     * @return a {@link StatusResponseDto} with the authentication status and username of the current user.
     */
    public StatusResponseDto checkStatus() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAuthenticated = authentication.isAuthenticated();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return StatusResponseDto.builder()
                .authenticated(isAuthenticated)
                .username(userDetails.getUsername())
                .build();
    }

}
