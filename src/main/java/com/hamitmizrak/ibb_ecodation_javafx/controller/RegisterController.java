package com.hamitmizrak.ibb_ecodation_javafx.controller;

import com.hamitmizrak.ibb_ecodation_javafx.dao.UserDAO;
import com.hamitmizrak.ibb_ecodation_javafx.dto.UserDTO;
import com.hamitmizrak.ibb_ecodation_javafx.utils.SpecialColor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.Optional;

public class RegisterController {

    // Injection
    // Veri tabanı işlemleri için)
    private UserDAO userDAO;

    // Parametresiz Constructor
    public RegisterController() {
        userDAO = new UserDAO();
    }


    /// /////////////////////////////////////////////////////////////////////////////
    ///  FXML Field
    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField emailField;


    /// //////////////////////////////////////////////////////////////////////////////////////
    // ShowAlert (Kullanıcıya bilgi veya hata mesajları göstermek için kullanılan yardımcı metot)
    // INFORMATION:BILGI,  ERROR: HATA
    private void showAlert(String title, String message, Alert.AlertType type) {
        // Alert nesnesi oluşturuyoruz ve parametre olarak alınan başlık, mesaj ve tipi ayarlıyoruz
        Alert alert = new Alert(type);

        // Title
        alert.setTitle(title);

        // Message
        alert.setContentText(message);

        // Alert penceresini gösteriyoruz ve kullanıcıdan bir yanıt bekliyoruz
        alert.showAndWait();
    } //end showAlert

    /// //////////////////////////////////////////////////////////////////////////////////////
    // Klavyeden ENTER tuşuna bastığımda giriş yapsın
    @FXML
    private void specialOnEnterPressed(KeyEvent keyEvent) {
        // Eğer basılan tuş ENTER ise
        if (keyEvent.getCode() == KeyCode.ENTER) {
            // Eğer Enter'a basarsam login() sayfasına gitsin
            register();
        }
    } // onEnterPressed


    /// /////////////////////////////////////////////////////////////////////////////////////
    // Register ( Kullanıcı giriş işlemini gerçekleştiren metot)
    @FXML
    public void register() {
        // Kullanıcı Giriş Yaparken Username, Password Almak
        String username, password,email;
        username = usernameField.getText();
        password = passwordField.getText();
        email = emailField.getText();

        // optionalUserDTO(Veri tabanına ekle)
        Optional<UserDTO> optionalRegisterUserDTO = Optional.ofNullable(UserDTO.builder()
                .id(0) // Create
                .username(username)
                .password(password)
                .email(email)
                .build());

        // Eğer Veri Boş değilse
        if (optionalRegisterUserDTO.isPresent()) {
            // UserDTO Verisini almak
            UserDTO userDTO = optionalRegisterUserDTO.get();

            // Eğer başarılıysa Pop-up göster
            showAlert("Başarılı", "Kayıt Başarılı", Alert.AlertType.INFORMATION);
            userDAO.create(userDTO);//ece
            // Kayıt başarılı ise Admin Panelkine geçiş sağla
            switchToLoginPane();

        } else {
            // Eğer bilgiler yanlışsa, database kayıt olmamışsa
            showAlert("Başarısız", "Giriş Başarısız", Alert.AlertType.ERROR);
        }
    }

    /// //////////////////////////////////////////////////////////////////////////////////////
    // Sayfalar Arasında Geçiş (LOGIN -> REGISTER)
    // Login (Switch)
    //  // Eğer Kayıt işlemi başarılıysa Login ekranına gitsin
    @FXML
    private void switchToLoginPane() {
        try {
            // FXML Dosyalarını Yükle (Kayıt ekranının FXML dosyasını yüklüyoruz)
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/hamitmizrak/ibb_ecodation_javafx/view/login.fxml"));
            Parent parent = fxmlLoader.load();

            // Var olan sahneyi alıp ve değiştirmek
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(parent));

            // Pencere başlığını 'Kayıt Ol' olarak ayarlıyalım
            stage.setTitle("Giriş Yap");

            // Sahneyi göster
            stage.show();
        } catch (Exception e) {
            //throw new RuntimeException(e);
            System.out.println(SpecialColor.RED + "Login Sayfasında yönlendirilmedi" + SpecialColor.RESET);
            e.printStackTrace();
            showAlert("Hata", "Login Ekranı Yüklenemedi", Alert.AlertType.ERROR);
        }
    } //end switchToLogin

} // end RegisterController
