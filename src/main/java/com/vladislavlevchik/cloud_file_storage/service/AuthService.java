package com.vladislavlevchik.cloud_file_storage.service;

import com.vladislavlevchik.cloud_file_storage.dto.UserLoginRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.UserRegisterRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.UserResponseDto;
import com.vladislavlevchik.cloud_file_storage.entity.User;
import com.vladislavlevchik.cloud_file_storage.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper mapper;
    private final PasswordEncoder encoder;

    public void signIn(UserLoginRequestDto userLoginRequestDto, HttpServletRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginRequestDto.getUsername(), userLoginRequestDto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
    }

    public UserResponseDto registerUser(UserRegisterRequestDto userRegisterRequestDto) {
        userRegisterRequestDto.setPassword(encoder.encode(userRegisterRequestDto.getPassword()));

        User user = repository.save(mapper.map(userRegisterRequestDto, User.class));

        return mapper.map(user, UserResponseDto.class);
    }

    public Map<String, Object> checkStatus(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAuthenticated = authentication.isAuthenticated();

        Map<String, Object> response = new HashMap<>();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        response.put("authenticated", isAuthenticated);
        response.put("username", userDetails.getUsername());

        return response;
    }

}
