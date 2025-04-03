package com.hamitmizrak.ibb_ecodation_javafx.controller;

import com.hamitmizrak.ibb_ecodation_javafx.dao.UserDAO;
import com.hamitmizrak.ibb_ecodation_javafx.dto.UserDTO;

import com.hamitmizrak.ibb_ecodation_javafx.enums.NotificationType;
import com.hamitmizrak.ibb_ecodation_javafx.utils.NotificationUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.Optional;

import static com.hamitmizrak.ibb_ecodation_javafx.utils.SessionManager.currentUser;

public class ProfileController {

    private final UserDAO userDAO = new UserDAO();

    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField oldPasswordField;
    @FXML
    private Label username;
    @FXML
    private Label email;
    @FXML
    private Label role;

    public void setUser(UserDTO user) {
        // Veritabanından güncel halini alalım
        System.out.println("ProfileController#setUser gelen user: " + user);
        //  this.currentUser = user;
        Optional<UserDTO> dbUser = getUserProfile(user.getId());

        if (dbUser.isPresent()) {
            UserDTO fresh = dbUser.get();
            username.setText(fresh.getUsername());
            email.setText(fresh.getEmail());
            role.setText(fresh.getRole().toString());
        } else {
            username.setText(user.getUsername());
            email.setText(user.getEmail());
            role.setText(user.getRole().toString());
        }
    }

    public Optional<UserDTO> getUserProfile(int requestedUserId) {
        return userDAO.findById(requestedUserId);
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) username.getScene().getWindow();
        stage.close();
    }

    //  private UserDTO currentUser;


    @FXML
    public void changePassword() throws SQLException {
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        setUser(currentUser);
        String oldPassword = currentUser.getPassword();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            NotificationUtil.showNotification("Şifre boş olmaz", NotificationType.WARNING);
            return;
        }
        // 3. Eşleşiyor mu kontrol et
        if (!newPassword.equals(confirmPassword)) {
            NotificationUtil.showNotification("\"Yeni şifreler uyuşmuyor!\"", NotificationType.WARNING);
            return;
        }

        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        currentUser.setPassword(hashedPassword);
       // currentUser.setPassword(newPassword);
        System.out.println("Şifre değişti");
        System.out.println("🟢 Güncellenecek kullanıcı: " + currentUser.getUsername());
        System.out.println("🟢 Yeni şifre (hash'li): " + hashedPassword);

        NotificationUtil.showNotification("Şifre değişti", NotificationType.SUCCESS);
       // confirmPasswordField.setText(newPassword);

       userDAO.updatePassword(currentUser);

        closeWindow();


    }
}
