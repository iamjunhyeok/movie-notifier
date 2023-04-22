package com.iamjunhyeok.MovieNotifier.service;

import com.iamjunhyeok.MovieNotifier.domain.User;
import com.iamjunhyeok.MovieNotifier.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;

    public List<User> getUsersByNotificationTime(LocalTime timestamp) {
        return userRepository.findAllByNotificationTime(LocalTime.of(timestamp.getHour(), timestamp.getMinute()));
    }
}
