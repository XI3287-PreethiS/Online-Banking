package com.online.banking.user.service;

import com.online.banking.user.clients.AccountClient;
import com.online.banking.user.clients.CardClient;
import com.online.banking.user.dto.*;
import com.online.banking.user.exception.*;
import com.online.banking.user.model.User;
import com.online.banking.user.repository.UserRepository;
import com.online.banking.user.util.ConstantUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final AccountClient accountClient;
    private final CardClient cardClient;

    @Override
    public UserResponseDto registerUser(UserRequestDto userRequestDto) {
        // Validate mandatory fields
        validateUserRequest(userRequestDto);

        // Check if user already exists
        if (userRepository.findByEmail(userRequestDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(ConstantUtil.USER_ALREADY_EXISTS);
        }

        // Create and save new user
        User user = modelMapper.map(userRequestDto, User.class);
        user.setActive(true);  // Default to active
        user.setBlocked(false);  // Default to not blocked
        userRepository.save(user);

        AccountRequestDto accountRequestDto = new AccountRequestDto();
            accountRequestDto.setUserId(user.getUserId());

        AccountResponseDto accountResponse = accountClient.createAccount(accountRequestDto);

        if (accountResponse == null) {
            throw new RuntimeException(ConstantUtil.ACCOUNT_CREATION_FAILED + user.getEmail());
        }

        CardRequestDto cardRequestDto = new CardRequestDto();
            cardRequestDto.setStakeholderName(user.getName());
            cardRequestDto.setAccountId(accountResponse.getId());


        CardResponseDto cardResponse = cardClient.createCard(cardRequestDto);

        if (cardResponse == null) {
            throw new RuntimeException(ConstantUtil.CARD_CREATION_FAILED + user.getEmail());
        }

        // Return user registration response
        return modelMapper.map(user, UserResponseDto.class);
    }

    private void validateUserRequest(UserRequestDto userRequestDto) {
        if (userRequestDto.getName() == null || userRequestDto.getName().isBlank()) {
            throw new IllegalArgumentException(ConstantUtil.NAME_MANDATORY);
        }
        if (userRequestDto.getEmail() == null || userRequestDto.getEmail().isBlank()) {
            throw new IllegalArgumentException(ConstantUtil.EMAIL_MANDATORY);
        }
        if (userRequestDto.getPassword() == null || userRequestDto.getPassword().isBlank()) {
            throw new IllegalArgumentException(ConstantUtil.PASSWORD_MANDATORY);
        }
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
    public UserResponseDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
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
