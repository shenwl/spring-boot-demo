package com.service;

import com.entity.User;
import com.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    BCryptPasswordEncoder mockEncoder;
    @Mock
    UserMapper mockUserMapper;
    @InjectMocks
    UserService userService;

    @Test
    void save() {
        Mockito.when(mockEncoder.encode("myPwd")).thenReturn("myEncodedPwd");

        // 调用userService,验证其将请求转发给了userMapper
        userService.save("test", "myPwd", "avatar");
        Mockito.verify(mockUserMapper).insertOne(
                "test",
                "myEncodedPwd",
                "avatar"
        );
    }

    @Test
    void getUserById() {
        userService.getUserById(1);
        Mockito.verify(mockUserMapper).findUserById(1);
    }

    @Test
    void getUserByUserName() {
        userService.getUserByUserName("testUser");
        Mockito.verify(mockUserMapper).findUserByUsername("testUser");
    }

    @Test
    void getPassword() {
        Mockito.when(mockUserMapper.findUserByUsername(("testUser")))
                .thenReturn(new User(123, "testUser", "encodedPassword"));
        userService.getPassword("testUser");
        Mockito.verify(mockUserMapper).findUserByUsername("testUser");
    }

    @Test
    void loadUserByUsername() {
        Mockito.when(mockUserMapper.findUserByUsername(("testUser")))
                .thenReturn(new User(123, "testUser", "encodedPassword"));
        userService.loadUserByUsername("testUser");
        Mockito.verify(mockUserMapper).findUserByUsername("testUser");
    }

    @Test
    void throwExceptionWhenUserNotFound() {
        Mockito.when(mockUserMapper.findUserByUsername(("testUser"))).thenReturn(null);
        Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("testUser")
        );
    }

    @Test
    void returnUserDetailsWhenUserFound() {
        Mockito.when(mockUserMapper.findUserByUsername(("testUser")))
                .thenReturn(new User(123, "testUser", "encodedPassword"));

        UserDetails userDetails = userService.loadUserByUsername("testUser");
        Assertions.assertEquals("testUser", userDetails.getUsername());
        Assertions.assertEquals("encodedPassword", userDetails.getPassword());
    }
}