package com.example.footrade.service;

public interface NotificationService {
    void sendNotification(String title, String message, String deviceToken);
}
