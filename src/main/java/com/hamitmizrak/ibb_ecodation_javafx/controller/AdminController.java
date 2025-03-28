package com.hamitmizrak.ibb_ecodation_javafx.controller;

import com.hamitmizrak.ibb_ecodation_javafx.dao.UserDAO;
import com.hamitmizrak.ibb_ecodation_javafx.dto.UserDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

// Admin Controller
public class AdminController {


    // Injection
    // Injection
    // Veri tabanı işlemleri için)
    private UserDAO userDAO;
    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    // Parametresiz Constructor
    public AdminController() {
        userDAO = new UserDAO();
    }

    /// ////////////////////////////////////////////////////////////////////////////
    // Kullanıcı tablosunu ve Sutunlarını tanımlamak
    // Dikkat: TableView => import javafx.scene.control.TableView;
    @FXML
    private TableView<UserDTO> userTable;

    @FXML
    private TableColumn<UserDTO, Integer> idColumn;

    @FXML
    private TableColumn<UserDTO, String> usernameColumn;

    @FXML
    private TableColumn<UserDTO, String> emailColumn;

    @FXML
    private TableColumn<UserDTO, String> passwordColumn;

    /// ////////////////////////////////////////////////////////////////////////////
    /// Admin sayfası açılırken hazır olsun
    @FXML
    public void initialize() {
        // Tablo sütunlarını UserDTO nesnesinin ilgili alanlarına bağlıyoruz
        // Dikkat: buradaki id, username, email,password
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Şifre sütununu maskeleme özelliği ekleniyor
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        passwordColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String password, boolean empty) {
                super.updateItem(password, empty);
                if (empty || password == null) {
                    setText(null);
                } else {
                    setText("******"); // Şifreleri gizlemek için yıldız işareti kullanılıyor
                }
            }
        });

        // Kullanıcı listesini tabloya yükleme
        refreshTable();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Kullanıcı listesini veritabanından alıp tabloyu günceller
    @FXML
    private void refreshTable() {
        // Kulalnıcı Listesini Optional olarak Al
        Optional<List<UserDTO>> optionalUsers = userDAO.list(); // Kullanıcı listesini getir

        // Optional al ve eğer boşsa bir liste kullan
        List<UserDTO> userDTOList= optionalUsers.orElseGet(List::of);

        // ObservableList Çevir
        ObservableList<UserDTO> userObservableList= FXCollections.observableArrayList(userDTOList);

        // Tabloyu Yükle
        userTable.setItems(userObservableList);

        // Show Alert
        showAlert("Bilgi", "Tablo başarıyla yenilendi!", Alert.AlertType.INFORMATION);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Kullanıcıya mesaj göstermek için genel bir metod
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Kullanıcı oturumu kapatma işlemi
    @FXML
    private void logout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Çıkış Yap");
        alert.setHeaderText("Oturumdan çıkmak istiyor musunuz?");
        alert.setContentText("Emin misiniz?");

        Optional<ButtonType> result = alert.showAndWait();
        // Eğer kullanıcıdan OK gelmişse
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hamitmizrak/ibb_ecodation_javafx/view/Login.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) userTable.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                showAlert("Hata", "Giriş sayfasına yönlendirme başarısız!", Alert.AlertType.ERROR);
            }
        }
    }

    /// ////////////////////////////////////////////////////////////////////////////
    /// EKLEME
    @FXML
    public void addUser(ActionEvent actionEvent) {
        TextInputDialog usernameDialog= new TextInputDialog();
        usernameDialog.setTitle("Yeni Kullanıcı Ekle");
        usernameDialog.setHeaderText("Yeni Kullanıcı Ekle");
        usernameDialog.setContentText("Kullanıcı Adı:");

        // Username verisi varsa
        Optional<String> optionalUsername= usernameDialog.showAndWait();

        // Text Input içine veri girilmişse
        if(optionalUsername.isPresent()){
            String username= optionalUsername.get();

            // Şifre için input
            TextInputDialog passwordDialog= new TextInputDialog();
            passwordDialog.setTitle("Yeni Kullanıcı Şifre");
            passwordDialog.setHeaderText("Yeni Kullanıcı Şifre");
            passwordDialog.setContentText("Kullanıcı Şifre:");

            // Username verisi varsa
            Optional<String> optionalPassword= usernameDialog.showAndWait();

            // Text Input içine veri girilmişse
            if(optionalPassword.isPresent()){
                String password= optionalPassword.get();

                // Email için input
                TextInputDialog emailDialog= new TextInputDialog();
                emailDialog.setTitle("Yeni Kullanıcı Email");
                emailDialog.setHeaderText("Yeni Kullanıcı Email");
                emailDialog.setContentText("Kullanıcı Email:");

                // Username verisi varsa
                Optional<String> optionalEmail= usernameDialog.showAndWait();

                // Text Input içine veri girilmişse
                if(optionalEmail.isPresent()) {
                    String email = optionalPassword.get();

                    // Kullanıcıyı Ekle
                    Optional<UserDTO> newUser = Optional.of(new UserDTO(0, username, password, email));

                    // Optional içindeki değeri almak istiyorsak
                    newUser.ifPresent(user->{
                        Optional<UserDTO> createdUser = userDAO.create(user);

                        // createdUser varsa => boolean dönderirir.
                        if(createdUser.isPresent()){
                            showAlert("Başarılı","Kullanıcı Başarıyla Eklendi",Alert.AlertType.INFORMATION);
                            refreshTable();
                        }else{
                            showAlert("Başarısız","Kullanıcı Eklerken hata alındı Eklendi",Alert.AlertType.ERROR);
                        }
                    }); //end  newUser.ifPresent
                } //end optionalEmail.isPresent()
            } //end optionalPassword.isPresent()
        } //end optionalUsername.isPresent()
    } //end method addUser

    /// ////////////////////////////////////////////////////////////////////////////
    /// GÜNCELLEME
    @FXML
    public void updateUser(ActionEvent actionEvent) {
        // Seçilen Kullancıyı Güncelle
        // Tıklanmış kullanıcı bilgisini almak
        UserDTO selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            // Kullanıcı Güncelle
            TextInputDialog usernameDialog = new TextInputDialog();
            usernameDialog.setTitle("Kullanıcı Güncelle");
            usernameDialog.setHeaderText("Kullanıcı Güncelle");
            usernameDialog.setContentText("Kullanıcı Adı:");

            // Username verisi varsa
            Optional<String> optionalUsername = usernameDialog.showAndWait();

            // Text Input içine veri girilmişse
            if (optionalUsername.isPresent()) {
                String username = optionalUsername.get();

                // Şifre için input
                TextInputDialog passwordDialog = new TextInputDialog();
                passwordDialog.setTitle("Güncelle Kullanıcı Şifre");
                passwordDialog.setHeaderText("Güncelle Kullanıcı Şifre");
                passwordDialog.setContentText("Kullanıcı Şifre:");

                // Username verisi varsa
                Optional<String> optionalPassword = usernameDialog.showAndWait();

                // Text Input içine veri girilmişse
                if (optionalPassword.isPresent()) {
                    String password = optionalPassword.get();

                    // Email için input
                    TextInputDialog emailDialog = new TextInputDialog();
                    emailDialog.setTitle("Güncelle Kullanıcı Email");
                    emailDialog.setHeaderText("Güncelle Kullanıcı Email");
                    emailDialog.setContentText("Kullanıcı Email:");

                    // Username verisi varsa
                    Optional<String> optionalEmail = usernameDialog.showAndWait();

                    // Text Input içine veri girilmişse
                    if (optionalEmail.isPresent()) {
                        String email = optionalPassword.get();

                        // Kullanıcıyı Ekle
                        Optional<UserDTO> newUser = Optional.of(new UserDTO(0, username, password, email));

                        // Optional içindeki değeri almak istiyorsak
                        newUser.ifPresent(user -> {
                            Optional<UserDTO> createdUser = userDAO.update(selectedUser.getId(),selectedUser);

                            // createdUser varsa => boolean dönderirir.
                            if (createdUser.isPresent()) {
                                showAlert("Başarılı", "Kullanıcı Başarıyla Güncellendi", Alert.AlertType.INFORMATION);
                                refreshTable();
                            } else {
                                showAlert("Başarısız", "Kullanıcı Eklerken hata alındı Güncellenmedi", Alert.AlertType.ERROR);
                            }
                        }); //end  newUser.ifPresent
                    } //end optionalEmail.isPresent()
                } //end optionalPassword.isPresent()
            } else {
                showAlert("Uyarı", "Lütfen bir kullanıcı Seçiniz", Alert.AlertType.ERROR);
            } // else
        } // end mthod update
    }

    /// ////////////////////////////////////////////////////////////////////////////
    /// SİLME
    @FXML
    public void deleteUser(ActionEvent actionEvent) {
        // Seçilen Kullancıyı Silmek
        // Tıklanmış kullanıcı bilgisini almak
        Optional<UserDTO> selectedUser = Optional.ofNullable(userTable.getSelectionModel().getSelectedItem());
        if (selectedUser != null) {

            // Onay Pencerisi
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Silme Onayı");
            confirmationAlert.setHeaderText("Kullanıcı Silmek ister misiniz ?");
            confirmationAlert.setContentText("Silinecek kullanıcı"+ selectedUser.get().getUsername());

            // Kullanıcıyı Onayı Almak
            Optional<ButtonType> isDelete= confirmationAlert.showAndWait();
            if(isDelete.isPresent() && isDelete.get()== ButtonType.OK){
                // Optional içindeki değeri almak istiyorsak
                selectedUser.ifPresent(user -> {
                    Optional<UserDTO> deleteUser = userDAO.delete(selectedUser.get().getId());

                    // deleteUser varsa => boolean dönderirir.
                    if (deleteUser.isPresent()) {
                        showAlert("Başarılı", "Kullanıcı Başarıyla Silindi", Alert.AlertType.INFORMATION);
                        refreshTable();
                    } else {
                        showAlert("Başarısız", "Kullanıcı Eklerken hata alındı Silinmedi", Alert.AlertType.ERROR);
                    }
                }); //end  newUser.ifPresent
            }
        } // end mthod delete
    }

} //end deleteUser
