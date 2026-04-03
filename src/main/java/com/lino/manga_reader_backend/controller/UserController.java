package com.lino.manga_reader_backend.controller;

import com.lino.manga_reader_backend.domain.User;
import com.lino.manga_reader_backend.dto.user.UpdateUserRequest;
import com.lino.manga_reader_backend.dto.user.UserResponse;
import com.lino.manga_reader_backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "Perfil e preferências do usuário autenticado")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Retorna os dados do usuário logado")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getMe(user.getId()));
    }

    @Operation(summary = "Atualiza nome, email ou preferências de leitura")
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/me")
    public ResponseEntity<UserResponse> updateMe(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UpdateUserRequest req) {
        return ResponseEntity.ok(userService.updateMe(user.getId(), req));
    }
}
