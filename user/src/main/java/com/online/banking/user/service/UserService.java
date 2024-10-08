package com.online.banking.user.service;

import com.online.banking.user.dto.UserRequestDto;
import com.online.banking.user.dto.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserResponseDto registerUser(UserRequestDto userRequestDto);
    UserResponseDto loginUser(UserRequestDto userRequestDto);
    String forgotPassword(String email);
    UserResponseDto changePassword(UserRequestDto userRequestDto);
    Page<UserResponseDto> getAllUsers(Pageable pageable);
    UserResponseDto getUserById(Long userId);
    List<UserResponseDto> searchUsers(String email, boolean isActive, boolean isBlocked);
    String logout();

}
