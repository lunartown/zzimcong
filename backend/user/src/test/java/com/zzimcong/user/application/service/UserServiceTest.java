//package com.zzimcong.user.application.service;
//
//import com.zzimcong.user.application.dto.SignupRequest;
//import com.zzimcong.user.application.dto.UserModifyRequest;
//import com.zzimcong.user.application.dto.UserResponse;
//import com.zzimcong.user.common.exception.ConflictException;
//import com.zzimcong.user.common.exception.NotFoundException;
//import com.zzimcong.user.common.util.AESUtil;
//import com.zzimcong.user.domain.entity.User;
//import com.zzimcong.user.domain.entity.UserRole;
//import com.zzimcong.user.domain.mapper.UserMapper;
//import com.zzimcong.user.domain.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.argThat;
//import static org.mockito.Mockito.*;
//
//class UserServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private UserMapper userMapper;
//
//    @Mock
//    private AESUtil aesUtil;
//
//    @InjectMocks
//    private UserService userService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testIsEmailAvailable() {
//        when(aesUtil.encrypt("test@example.com")).thenReturn("encryptedEmail");
//        when(userRepository.existsByEmail("encryptedEmail")).thenReturn(false);
//
//        assertTrue(userService.isEmailAvailable("test@example.com"));
//
//        verify(aesUtil).encrypt("test@example.com");
//        verify(userRepository).existsByEmail("encryptedEmail");
//    }
//
//    @Test
//    void testCreateUser() {
//        SignupRequest signupRequest = new SignupRequest("test@example.com", "Test User", "1234567890", "password");
//        User user = User.builder()
//                .id(1L)
//                .email("encryptedEmail")
//                .name("encryptedName")
//                .phone("encryptedPhone")
//                .password("encodedPassword")
//                .role(UserRole.USER)
//                .build();
//
//        when(aesUtil.encrypt("test@example.com")).thenReturn("encryptedEmail");
//        when(userRepository.existsByEmail("encryptedEmail")).thenReturn(false);
//        when(userMapper.toEntity(signupRequest)).thenReturn(user);
//        when(userRepository.save(user)).thenReturn(user);
//        when(userMapper.toDto(user)).thenReturn(new UserResponse(1L, "test@example.com", "Test User", "1234567890"));
//
//        UserResponse result = userService.createUser(signupRequest);
//
//        assertNotNull(result);
//        assertEquals(1L, result.id());
//        assertEquals("test@example.com", result.email());
//
//        verify(userRepository).existsByEmail("encryptedEmail");
//        verify(userMapper).toEntity(signupRequest);
//        verify(userRepository).save(user);
//        verify(userMapper).toDto(user);
//    }
//
//    @Test
//    void testCreateUserDuplicateEmail() {
//        SignupRequest signupRequest = new SignupRequest("test@example.com", "Test User", "1234567890", "password");
//
//        when(aesUtil.encrypt("test@example.com")).thenReturn("encryptedEmail");
//        when(userRepository.existsByEmail("encryptedEmail")).thenReturn(true);
//
//        assertThrows(ConflictException.class, () -> userService.createUser(signupRequest));
//
//        verify(userRepository).existsByEmail("encryptedEmail");
//        verify(userMapper, never()).toEntity(any());
//        verify(userRepository, never()).save(any());
//    }
//
//    @Test
//    void testGetUserById() {
//        User user = User.builder()
//                .id(1L)
//                .email("encryptedEmail")
//                .name("encryptedName")
//                .phone("encryptedPhone")
//                .password("encodedPassword")
//                .role(UserRole.USER)
//                .build();
//
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//        when(userMapper.toDto(user)).thenReturn(new UserResponse(1L, "test@example.com", "Test User", "1234567890"));
//
//        UserResponse result = userService.getUserById(1L);
//
//        assertNotNull(result);
//        assertEquals(1L, result.id());
//
//        verify(userRepository).findById(1L);
//        verify(userMapper).toDto(user);
//    }
//
//    @Test
//    void testGetUserByIdNotFound() {
//        when(userRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class, () -> userService.getUserById(1L));
//
//        verify(userRepository).findById(1L);
//    }
//
//    @Test
//    void testUpdateUser() {
//        UserModifyRequest userModifyRequest = new UserModifyRequest("Updated User", "9876543210", "newpassword");
//        User existingUser = User.builder()
//                .id(1L)
//                .email("encryptedEmail")
//                .name("encryptedName")
//                .phone("encryptedPhone")
//                .password("encodedPassword")
//                .role(UserRole.USER)
//                .build();
//
//        User updatedUser = User.builder()
//                .id(1L)
//                .email("encryptedEmail")
//                .name("encryptedUpdatedName")
//                .phone("encryptedUpdatedPhone")
//                .password("encodedNewPassword")
//                .role(UserRole.USER)
//                .build();
//
//        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
//        when(userMapper.updateFromDto(userModifyRequest, existingUser)).thenReturn(updatedUser);
//        when(userRepository.save(updatedUser)).thenReturn(updatedUser);
//        when(userMapper.toDto(updatedUser)).thenReturn(new UserResponse(1L, "test@example.com", "Updated User", "9876543210"));
//
//        UserResponse result = userService.updateUser(1L, userModifyRequest);
//
//        assertNotNull(result);
//        assertEquals("Updated User", result.username());
//        assertEquals("9876543210", result.phone());
//
//        verify(userRepository).findById(1L);
//        verify(userMapper).updateFromDto(userModifyRequest, existingUser);
//        verify(userRepository).save(updatedUser);
//        verify(userMapper).toDto(updatedUser);
//    }
//
//    @Test
//    void testSignoutUser() {
//        User user = User.builder()
//                .id(1L)
//                .email("encryptedEmail")
//                .name("encryptedName")
//                .phone("encryptedPhone")
//                .password("encodedPassword")
//                .role(UserRole.USER)
//                .signout(false)
//                .build();
//
//        User signedOutUser = User.builder()
//                .id(1L)
//                .email("encryptedEmail")
//                .name("encryptedName")
//                .phone("encryptedPhone")
//                .password("encodedPassword")
//                .role(UserRole.USER)
//                .signout(true)
//                .build();
//
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//        when(userRepository.save(any(User.class))).thenReturn(signedOutUser);
//
//        userService.signoutUser(1L);
//
//        verify(userRepository).findById(1L);
//        verify(userRepository).save(argThat(savedUser -> savedUser.getSignout()));
//    }
//}