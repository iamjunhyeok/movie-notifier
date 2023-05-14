package com.iamjunhyeok.MovieNotifier.service;

import com.iamjunhyeok.MovieNotifier.domain.User;
import com.iamjunhyeok.MovieNotifier.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("알림 시간으로 사용자 조회")
    void testGetUsersByNotificationTime() {
        // Arrange
        LocalTime notificationTime = LocalTime.of(17, 0);
        List<User> users = Arrays.asList(new User("jeon", "1231231", "01012345678"));
        when(userRepository.findAllByNotificationTime(notificationTime)).thenReturn(users);

        // Act
        List<User> result = userService.getUsersByNotificationTime(notificationTime);

        // Assert
        assertEquals(users.size(), result.size());
        assertEquals(users.get(0).getId(), result.get(0).getId());
    }

}