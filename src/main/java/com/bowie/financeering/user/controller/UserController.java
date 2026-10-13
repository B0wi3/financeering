package com.bowie.financeering.user.controller;

import com.bowie.financeering.user.dto.UserCreateDTO;
import com.bowie.financeering.user.dto.UserResponseDTO;
import com.bowie.financeering.user.dto.UserUpdateDTO;
import com.bowie.financeering.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
            @Valid @RequestBody UserCreateDTO dto,
            @AuthenticationPrincipal Jwt jwt
    ) {

        String userSub = jwt.getSubject();
        UserResponseDTO responseDTO = userService.createUser(dto, userSub);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<UserResponseDTO> getCurrentUser(
            @AuthenticationPrincipal Jwt jwt
    ) throws EntityNotFoundException {

        String userSub = jwt.getSubject();
        UserResponseDTO responseDTO = userService.getUserById(userSub);

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping()
    public ResponseEntity<UserResponseDTO> updateUser(
            @Valid @RequestBody UserUpdateDTO dto,
            @AuthenticationPrincipal Jwt jwt
    ) throws EntityNotFoundException {

        String userSub = jwt.getSubject();
        UserResponseDTO responseDTO = userService.updateUser(dto, userSub);

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(
            @AuthenticationPrincipal Jwt jwt
    ) {

        String userSub = jwt.getSubject();
        userService.deleteCurrentUser(userSub);

        return ResponseEntity.noContent().build();
    }
}
