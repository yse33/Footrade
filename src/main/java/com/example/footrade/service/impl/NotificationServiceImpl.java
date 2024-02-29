package com.example.footrade.service.impl;

import com.example.footrade.service.NotificationService;
import com.example.footrade.service.ShoeService;
import com.example.footrade.service.UserService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final UserService userService;
    private final ShoeService shoeService;

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
            throw new RuntimeException("Failed to send notification to device with token: " + deviceToken);
        }
    }

    @Override
    public void sendNotificationForUpdatedShoes(String title, String message, Date date) {
        List<ObjectId> updatedShoeIds = shoeService.getShoeIdsForNotification(date);
        List<String> userDeviceTokens = userService.getDeviceTokensByShoeIds(updatedShoeIds);

        for (String deviceToken : userDeviceTokens) {
            sendNotification(title, message, deviceToken);
        }
    }
}
