package com.iamjunhyeok.MovieNotifier.service;

import com.iamjunhyeok.MovieNotifier.constant.NotificationStatus;
import com.iamjunhyeok.MovieNotifier.domain.Actor;
import com.iamjunhyeok.MovieNotifier.domain.Movie;
import com.iamjunhyeok.MovieNotifier.domain.Notification;
import com.iamjunhyeok.MovieNotifier.domain.User;
import com.iamjunhyeok.MovieNotifier.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final DefaultMessageService messageService;

    @Value("${sms.sender}")
    private String sender;

    public boolean isAlreadySent(Movie movie, User user) {
        return notificationRepository.existsByUserAndMovie(user, movie);
    }

    @Transactional
    public void save(Movie movie, List<User> users) {
        String message = getMessage(movie);
        for (User user : users) {
            Notification notification = new Notification(message, user, movie);
            notificationRepository.save(notification);
        }
    }

    @Transactional
    public void sendNotification() {
        List<Notification> notifications = notificationRepository.findAllByStatus(NotificationStatus.PENDING);
        Map<User, List<Movie>> userListMap = new HashMap<>();
        for (Notification notification : notifications) {
            notification.complete();
            userListMap.computeIfAbsent(notification.getUser(), user -> new ArrayList<>()).add(notification.getMovie());
        }

        for (Map.Entry<User, List<Movie>> userListEntry : userListMap.entrySet()) {
            User user = userListEntry.getKey();
            Iterator<Movie> iterator = userListEntry.getValue().iterator();

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(user.getUsername() + " 님의 선호 장르와 평점을 기반으로 최신 영화 정보를 알려드립니다.");
            stringBuilder.append(System.lineSeparator());
            while (iterator.hasNext()) {
                Movie movie = iterator.next();
                stringBuilder.append(getMessage(movie));
                if (iterator.hasNext()) stringBuilder.append(System.lineSeparator());
            }

            Message message = new Message();
            message.setFrom(sender);
            message.setTo(user.getPhoneNumber());
            message.setSubject("최신 영화 정보 알림");
            message.setText(stringBuilder.toString());
            SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        }
    }

    public String getMessage(Movie movie) {
        StringBuilder message = new StringBuilder();
        message.append(movie.getTitle());
        message.append(System.lineSeparator());
        message.append("장르 " + movie.getGenre().getValue() + " 러닝타임 " + movie.getRunningTime());
        message.append(System.lineSeparator());
        message.append("개봉 " + movie.getReleaseDate() + " 평점 " + movie.getRating());
        message.append(System.lineSeparator());
        message.append("출연 ");
        Iterator<Actor> iterator = movie.getActors().iterator();
        while (iterator.hasNext()) {
            Actor actor = iterator.next();
            message.append(actor.getName());
            if (iterator.hasNext()) message.append(", ");
        }
        message.append(System.lineSeparator());
        return message.toString();
    }
}
