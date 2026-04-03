package com.lino.manga_reader_backend.service;

import com.lino.manga_reader_backend.domain.User;
import com.lino.manga_reader_backend.dto.user.UpdateUserRequest;
import com.lino.manga_reader_backend.dto.user.UserResponse;
import com.lino.manga_reader_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse getMe(UUID userId) {
        return UserResponse.from(findById(userId));
    }

    public UserResponse updateMe(UUID userId, UpdateUserRequest req) {
        User user = findById(userId);

        if (req.name() != null)            user.setUsername(req.name());
        if (req.email() != null)           user.setEmail(req.email());
        if (req.preferredLang() != null)   user.setPreferredLang(req.preferredLang());
        if (req.imageQuality() != null)    user.setImageQuality(req.imageQuality());
        if (req.readerDirection() != null) user.setReaderDirection(req.readerDirection());
        user.setUpdatedAt(Instant.now());

        return UserResponse.from(userRepository.save(user));
    }

    private User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }
}
