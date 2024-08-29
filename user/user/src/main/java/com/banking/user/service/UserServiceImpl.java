package com.banking.user.service;

import com.banking.user.dto.UserRequestDto;
import com.banking.user.dto.UserResponseDto;
import com.banking.user.exception.*;
import com.banking.user.model.User;
import com.banking.user.repository.UserRepository;
import com.banking.user.util.ConstantUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserResponseDto registerUser(UserRequestDto userRequestDto) {
        // Validate mandatory fields
        if (userRequestDto.getName() == null || userRequestDto.getName().isBlank()) {
            throw new IllegalArgumentException(ConstantUtil.NAME_MANDATORY);
        }
        if (userRequestDto.getEmail() == null || userRequestDto.getEmail().isBlank()) {
            throw new IllegalArgumentException(ConstantUtil.EMAIL_MANDATORY);
        }
        if (userRequestDto.getPassword() == null || userRequestDto.getPassword().isBlank()) {
            throw new IllegalArgumentException(ConstantUtil.PASSWORD_MANDATORY);
        }

        // Check if user already exists
        if (userRepository.findByEmail(userRequestDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(ConstantUtil.USER_ALREADY_EXISTS);
        }

        // Create and save new user
        User user = modelMapper.map(userRequestDto, User.class);
        user.setActive(true); // Default new users to active
        user.setBlocked(false); // Default new users to not blocked
        userRepository.save(user);

        // Return response DTO
        return modelMapper.map(user, UserResponseDto.class);
    }


    @Override
    public UserResponseDto loginUser(UserRequestDto userRequestDto) {
        User user = userRepository.findByEmail(userRequestDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException(ConstantUtil.NO_USERS_FOUND));
        if (user.isBlocked()) {
            throw new UserBlockedException(ConstantUtil.USER_BLOCKED);
        }
        if (!user.isActive()) {
            throw new UserNotActiveException(ConstantUtil.USER_NOT_ACTIVE);
        }
        if (!user.getPassword().equals(userRequestDto.getPassword())) {
            throw new InvalidPasswordException(ConstantUtil.INVALID_PASS);
        }
        return modelMapper.map(user, UserResponseDto.class);
    }

    @Override
    public String forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(ConstantUtil.NO_USERS_FOUND));
        // reset password URL (skipped here)
        return null;
    }

    @Override
    public UserResponseDto changePassword(UserRequestDto userRequestDto) {
        User user = userRepository.findByEmail(userRequestDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException(ConstantUtil.NO_USERS_FOUND));
        if (!user.getPassword().equals(userRequestDto.getPassword())) {
            throw new InvalidPasswordException(ConstantUtil.INVALID_CURRENT_PASSWORD);
        }
        user.setPassword(userRequestDto.getNewPassword());
        userRepository.save(user);
        return modelMapper.map(user, UserResponseDto.class);
    }

    @Override
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(user -> modelMapper.map(user, UserResponseDto.class));
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(ConstantUtil.NO_USERS_FOUND));
        return modelMapper.map(user, UserResponseDto.class);
    }


    @Override
    public List<UserResponseDto> searchUsers(String email, boolean isActive, boolean isBlocked) {
        List<UserResponseDto> users = userRepository.findByEmail(email).stream()
                .filter(user -> user.isActive() == isActive && user.isBlocked() == isBlocked)
                .map(this::convertToDto)
                .collect(Collectors.toList());

        if (users.isEmpty()) {
            throw new UserNotFoundException(ConstantUtil.NO_USERS_FOUND);
        }

        return users;
    }

    private UserResponseDto convertToDto(User user) {
        return modelMapper.map(user, UserResponseDto.class);
    }



    @Override
    public String logout() {
        return ConstantUtil.LOGGED_OUT;
    }
}
