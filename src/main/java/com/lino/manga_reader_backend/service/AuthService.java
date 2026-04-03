package com.lino.manga_reader_backend.service;

import com.lino.manga_reader_backend.dto.*;
import com.lino.manga_reader_backend.config.AppProperties;
import com.lino.manga_reader_backend.domain.*;
import com.lino.manga_reader_backend.repository.RefreshTokenRepository;
import com.lino.manga_reader_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final AppProperties props;

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.email()))
            throw new IllegalArgumentException("E-mail já cadastrado");
        if (userRepository.existsByUsername(req.username()))
            throw new IllegalArgumentException("Username já cadastrado");

        var user = User.builder()
            .username(req.username())
            .email(req.email())
            .password(passwordEncoder.encode(req.password()))
            .build();

        userRepository.save(user);
        return buildTokens(user);
    }

    @Transactional
    public AuthResponse login(LoginRequest req) {
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.email(), req.password())
        );
        var user = userRepository.findByEmail(req.email()).orElseThrow();
        refreshTokenRepository.revokeAllByUserId(user.getId());
        return buildTokens(user);
    }

    @Transactional
    public AuthResponse refresh(String rawToken) {
        var stored = refreshTokenRepository.findByToken(rawToken)
            .orElseThrow(() -> new IllegalArgumentException("Refresh token inválido"));

        if (stored.isRevoked() || stored.getExpiresAt().isBefore(Instant.now()))
            throw new IllegalArgumentException("Refresh token expirado ou revogado");

        stored.setRevoked(true);
        refreshTokenRepository.save(stored);
        return buildTokens(stored.getUser());
    }

    @Transactional
    public void logout(String rawToken) {
        refreshTokenRepository.findByToken(rawToken)
            .ifPresent(t -> { t.setRevoked(true); refreshTokenRepository.save(t); });
    }

    private AuthResponse buildTokens(User user) {
        String access = jwtService.generateAccessToken(user);
        String raw = UUID.randomUUID().toString();

        var refresh = RefreshToken.builder()
            .user(user)
            .token(raw)
            .expiresAt(Instant.now().plusMillis(props.jwt().refreshExpirationMs()))
            .build();

        refreshTokenRepository.save(refresh);
        return new AuthResponse(access, raw);
    }
}