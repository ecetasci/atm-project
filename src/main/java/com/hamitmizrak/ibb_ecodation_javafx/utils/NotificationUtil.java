package com.hamitmizrak.ibb_ecodation_javafx.utils;

import com.hamitmizrak.ibb_ecodation_javafx.dao.NotificationDAO;
import com.hamitmizrak.ibb_ecodation_javafx.dto.Notification;
import com.hamitmizrak.ibb_ecodation_javafx.enums.NotificationType;
import javafx.scene.control.Alert;

import java.util.List;

import static com.hamitmizrak.ibb_ecodation_javafx.enums.NotificationType.*;

public class NotificationUtil {

    private static final NotificationDAO notificationDAO;

    static {
        notificationDAO = new NotificationDAO();
    }

    public static void showNotification(String message, NotificationType type) {
        // DAO'ya bildirimi ekle
        notificationDAO.addNotification(message, type);

        // Pop-up göster
        Alert alert;
        switch (type) {
            case SUCCESS -> alert = new Alert(Alert.AlertType.INFORMATION);
            case ERROR -> alert = new Alert(Alert.AlertType.ERROR);
            case WARNING -> alert = new Alert(Alert.AlertType.WARNING);
            default -> alert = new Alert(Alert.AlertType.NONE);
        }

        alert.setTitle("Bildirim");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static List<Notification> getAll() {
        return notificationDAO.getAllNotifications();
    }
}
