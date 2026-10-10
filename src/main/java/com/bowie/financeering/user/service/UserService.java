package com.bowie.financeering.user.service;

import com.bowie.financeering.user.dto.UserCreateDTO;
import com.bowie.financeering.user.dto.UserResponseDTO;
import com.bowie.financeering.user.model.User;
import com.bowie.financeering.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public UserResponseDTO getUserById(String userSub) throws EntityNotFoundException {
        User user = userRepository.findById(userSub).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );

        return toResponseDto(user);
    }

    public UserResponseDTO createUser(UserCreateDTO dto) {
        User user = new User(
                dto.getUserSub(),
                dto.getCurrency(),
                dto.getLocale()
        );

        userRepository.save(user);
        return toResponseDto(user);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(User user) {
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
