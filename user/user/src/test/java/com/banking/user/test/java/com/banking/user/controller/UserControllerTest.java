package com.banking.user.controller;

import com.banking.user.dto.UserRequestDto;
import com.banking.user.dto.UserResponseDto;
import com.banking.user.service.UserService;
import com.banking.user.util.ConstantUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUserTest() {
        UserRequestDto requestDto = new UserRequestDto("John Doe", "john@example.com", "password", true, false);
        UserResponseDto responseDto = new UserResponseDto(1L, "john@example.com", "John Doe", true, false);

        when(userService.registerUser(requestDto)).thenReturn(responseDto);

        ResponseEntity<UserResponseDto> response = userController.registerUser(requestDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
    }

    @Test
    void loginUserTest() {
        UserRequestDto requestDto = new UserRequestDto("John Doe", "john@example.com", "password", true, false);
        UserResponseDto responseDto = new UserResponseDto(1L, "john@example.com", "John Doe", true, false);

        when(userService.loginUser(requestDto)).thenReturn(responseDto);

        ResponseEntity<UserResponseDto> response = userController.loginUser(requestDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
    }


    @Test
    void searchUsersTest() {
        UserResponseDto user1 = new UserResponseDto(1L, "john@example.com", "John Doe", true, false);
        List<UserResponseDto> users = List.of(user1);

        when(userService.searchUsers("john@example.com", true, false)).thenReturn(users);

        ResponseEntity<?> response = userController.searchUsers("john@example.com", true, false);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    void logoutTest() {
        String logoutMessage = ConstantUtil.LOGGED_OUT;
        when(userService.logout()).thenReturn(logoutMessage);

        ResponseEntity<String> response = userController.logout();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(logoutMessage, response.getBody());
    }
}
