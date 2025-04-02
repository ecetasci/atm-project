package com.hamitmizrak.ibb_ecodation_javafx.dao;

import com.hamitmizrak.ibb_ecodation_javafx.dto.Notification;
import com.hamitmizrak.ibb_ecodation_javafx.enums.NotificationType;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    public void addNotification(String message, NotificationType notificationType) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setNotificationType(notificationType);
        notification.setTimestamp(LocalDateTime.now());
        notification.saveToFile(message);
        notifications.add(notification);

    }
    private List<Notification> notifications = new ArrayList<>();

    public List<Notification> getAllNotifications() {
        return notifications;
    }
}
