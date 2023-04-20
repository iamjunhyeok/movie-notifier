package com.iamjunhyeok.MovieNotifier.service;

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

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final DefaultMessageService messageService;

    @Value("${sms.sender}")
    private String sender;

    @Transactional
    public void sendNotification(Movie movie, List<User> users) {
        for (User user : users) {
            Message message = new Message();
            message.setFrom(sender);
            message.setTo(user.getPhoneNumber());
            message.setText(getMessage(movie, user));
            SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
            if (response.getStatusCode().equals("2000")) {
                Notification notification = new Notification(message.getText(), user, movie);
                notificationRepository.save(notification);
            }
        }
    }

    public String getMessage(Movie movie, User user) {
        return movie.toString();
    }

    public boolean isAlreadySent(Movie movie, User user) {
        return notificationRepository.existsByUserAndMovie(user, movie);
    }
}
