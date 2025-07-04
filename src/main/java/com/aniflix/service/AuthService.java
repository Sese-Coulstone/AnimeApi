package com.aniflix.service;

import com.aniflix.dto.LoginRequest;
import com.aniflix.dto.RegisterRequest;
import com.aniflix.dto.ResponseDto;
import com.aniflix.entity.Role;
import com.aniflix.entity.User;
import com.aniflix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ResponseDto register(RegisterRequest request) {
        var user = User.builder()
                .username(request.getFirstname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        var token = jwtService.generateToken(user);

        return ResponseDto.builder()
                        .accessToken(token)
                        .build();

    }

    public ResponseDto login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        var token = jwtService.generateToken(user);

        return ResponseDto.builder()
                .accessToken(token)
                .build();
    }
}
