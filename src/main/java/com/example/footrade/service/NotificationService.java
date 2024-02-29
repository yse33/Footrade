package com.example.footrade.service;

import java.util.Date;

public interface NotificationService {
    void sendNotification(String title, String message, String deviceToken);
    void sendNotificationForUpdatedShoes(String title, String message, Date date);
}
