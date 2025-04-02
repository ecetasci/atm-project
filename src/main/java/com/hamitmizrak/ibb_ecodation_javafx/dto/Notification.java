package com.hamitmizrak.ibb_ecodation_javafx.dto;

import com.hamitmizrak.ibb_ecodation_javafx.enums.NotificationType;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Notification {

    private NotificationType notificationType;

    private String message;

    private LocalDateTime timestamp;

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public void saveToFile(String message) {
        try (FileWriter fw = new FileWriter("bildirimler.txt", true)) {
            fw.write(message + " - " + LocalDateTime.now() + System.lineSeparator());
        } catch (IOException e) {
            System.err.println("⚠️ Bildirim dosyasına yazılamadı: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
