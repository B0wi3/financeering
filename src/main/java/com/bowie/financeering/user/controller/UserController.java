package com.bowie.financeering.user.controller;

import com.bowie.financeering.user.dto.UserCreateDTO;
import com.bowie.financeering.user.dto.UserResponseDTO;
import com.bowie.financeering.user.dto.UserUpdateDTO;
import com.bowie.financeering.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(
            @AuthenticationPrincipal Jwt jwt
    ) {

        String userSub = jwt.getSubject();
        List<UserResponseDTO> responseDTO = userService.getAllUsers(userSub);

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @PathVariable String userSub,
            @AuthenticationPrincipal Jwt jwt
    ) throws EntityNotFoundException {

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

//    @DeleteMapping("/{id}")
//    public ResponseEntity<UserResponseDTO> deleteUser(
//            @PathVariable String userSub,
//            @AuthenticationPrincipal Jwt jwt
//    ) throws AccessDeniedException {
//
//        String requestSub = jwt.getSubject();
//        userService.deleteUser(userSub, requestSub);
//
//        return ResponseEntity.noContent().build();
//    }
}
