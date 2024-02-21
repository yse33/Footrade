package com.example.footrade.service.impl;

import com.example.footrade.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    @Override
    public void sendNotification(String title, String message, String deviceToken) {
        Message msg = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(message)
                        .build())
                .setToken(deviceToken)
                .build();
        try {
            FirebaseMessaging.getInstance().send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
