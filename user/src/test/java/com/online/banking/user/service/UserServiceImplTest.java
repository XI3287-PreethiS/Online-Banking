//package com.online.banking.user.service;
//
//import com.online.banking.user.dto.UserRequestDto;
//import com.online.banking.user.dto.UserResponseDto;
//import com.online.banking.user.exception.*;
//import com.online.banking.user.model.User;
//import com.online.banking.user.repository.UserRepository;
//import com.online.banking.user.service.UserServiceImpl;
//import com.online.banking.user.util.ConstantUtil;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.modelmapper.ModelMapper;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class UserServiceImplTest {
//
//    @InjectMocks
//    private UserServiceImpl userService;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private ModelMapper modelMapper;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void registerUserTest() {
//        UserRequestDto requestDto = new UserRequestDto("John Doe", "john@example.com", "password", true, false);
//        User user = new User(1L, "john@example.com", "password", "John Doe", true, false);
//        UserResponseDto responseDto = new UserResponseDto(1L, "John Doe", "john@example.com", true, false);
////        System.out.println("user response "+responseDto.getName());
//        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.empty());
//        when(modelMapper.map(requestDto, User.class)).thenReturn(user);
////        System.out.println("user is "+user.getName());
//        when(userRepository.save(user)).thenReturn(user);
//       // System.out.println(" "+ when(userRepository.save(user)).thenReturn(user));
//        when(modelMapper.map(user, UserResponseDto.class)).thenReturn(responseDto);
//        System.out.println("user response dto "+responseDto.getName());
//
//        UserResponseDto result = userService.registerUser(requestDto);
//        assertEquals(responseDto, result);
//    }
//
//    @Test
//    void loginUserTest() {
//        UserRequestDto requestDto = new UserRequestDto("John Doe", "john@example.com", "password", true, false);
//        User user = new User(1L, "john@example.com", "password", "John Doe", true, false);
//        UserResponseDto responseDto = new UserResponseDto(1L, "john@example.com", "John Doe", true, false);
//
//        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.of(user));
//        when(modelMapper.map(user, UserResponseDto.class)).thenReturn(responseDto);
//
//        UserResponseDto result = userService.loginUser(requestDto);
//        assertEquals(responseDto, result);
//    }
//
//    @Test
//    void forgotPasswordTest() {
//        String email = "john@example.com";
//        User user = new User(1L, email, "password", "John Doe", true, false);
//
//        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
//
//        String result = userService.forgotPassword(email);
//        assertNull(result); // Assuming password reset URL logic is skipped
//    }
//
//    @Test
//    void changePasswordTest() {
//        UserRequestDto requestDto = new UserRequestDto("john@example.com", "password", "newpassword", "John Doe", true, false);
//        User user = new User(1L, "john@example.com", "password", "John Doe", true, false);
//        UserResponseDto responseDto = new UserResponseDto(1L, "john Doe", "john@example.com", true, false);
//
//        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.of(user));
//        when(modelMapper.map(user, UserResponseDto.class)).thenReturn(responseDto);
//
//        UserResponseDto result = userService.changePassword(requestDto);
//        assertEquals(responseDto, result);
//        assertEquals("newpassword", user.getPassword());
//    }
//
//    @Test
//    void getAllUsersTest() {
//        User user1 = new User(1L, "john@example.com", "password", "John Doe", true, false);
//        User user2 = new User(2L, "jane@example.com", "password", "Jane Doe", true, false);
//        List<User> users = List.of(user1, user2);
//        Page<User> userPage = new PageImpl<>(users);
//        Page<UserResponseDto> responsePage = new PageImpl<>(List.of(
//                new UserResponseDto(1L, "john@example.com", "John Doe", true, false),
//                new UserResponseDto(2L, "jane@example.com", "Jane Doe", true, false)
//        ));
//
//        Pageable pageable = PageRequest.of(0, 10);
//        when(userRepository.findAll(pageable)).thenReturn(userPage);
//        when(modelMapper.map(user1, UserResponseDto.class)).thenReturn(responsePage.getContent().get(0));
//        when(modelMapper.map(user2, UserResponseDto.class)).thenReturn(responsePage.getContent().get(1));
//
//        Page<UserResponseDto> result = userService.getAllUsers(pageable);
//        assertEquals(responsePage, result);
//    }
//
//    @Test
//    void searchUsersTest() {
//        User user = new User(1L, "john@example.com", "password", "John Doe", true, false);
//        List<User> users = List.of(user);
//        List<UserResponseDto> responseDtos = List.of(new UserResponseDto(1L, "john@example.com", "John Doe", true, false));
//
//        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
//        when(modelMapper.map(user, UserResponseDto.class)).thenReturn(responseDtos.get(0));
//
//        List<UserResponseDto> result = userService.searchUsers("john@example.com", true, false);
//        assertEquals(responseDtos, result);
//    }
//
//    @Test
//    void testLogout() {
//        String response = userService.logout();
//
//        assertEquals(ConstantUtil.LOGGED_OUT, response);
//    }
//}
