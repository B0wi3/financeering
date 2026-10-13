package com.bowie.financeering.user.service;

import com.bowie.financeering.user.dto.UserCreateDTO;
import com.bowie.financeering.user.dto.UserResponseDTO;
import com.bowie.financeering.user.dto.UserUpdateDTO;
import com.bowie.financeering.user.model.User;
import com.bowie.financeering.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponseDTO getUserById(String userSub) throws EntityNotFoundException, AccessDeniedException {
        User user = userRepository.findById(userSub).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );

        return toResponseDto(user);
    }

    public UserResponseDTO createUser(UserCreateDTO dto, String userSub) {
        if (userRepository.existsById(userSub)) {
            throw new IllegalArgumentException("User already exists");
        }

        User user = new User(
                userSub,
                dto.getCurrency(),
                dto.getLocale()
        );

        userRepository.save(user);
        return toResponseDto(user);
    }

    public UserResponseDTO updateUser(UserUpdateDTO dto, String userSub) throws EntityNotFoundException {
        User user = userRepository.findById(userSub).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );

        if (!user.getUserSub().equals(userSub)) {
            throw new AccessDeniedException("Access denied");
        }

        user.setCurrency(dto.getCurrency());
        user.setLocale(dto.getLocale());

        userRepository.save(user);

        return toResponseDto(user);
    }

    public void deleteCurrentUser(String userSub) throws  EntityNotFoundException {
        User user = userRepository.findById(userSub).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );

        userRepository.delete(user);
    }

    private UserResponseDTO toResponseDto(User user) {
        return new UserResponseDTO(
                user.getUserSub(),
                user.getCurrency(),
                user.getLocale()
        );
    }
}
