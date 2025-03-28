package com.hamitmizrak.ibb_ecodation_javafx;

import com.hamitmizrak.ibb_ecodation_javafx.database.SingletonDBConnection;
import com.hamitmizrak.ibb_ecodation_javafx.database.SingletonPropertiesDBConnection;
import com.hamitmizrak.ibb_ecodation_javafx.utils.SpecialColor;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class HelloApplication extends Application {

    // Proje Açıldığında İlk Açılacak Sayfa
    @Override
    public void start(Stage stage) throws IOException {
        // PROJE AYAĞA KALKALKEN DATABASE(H2DB) ÇALIŞSIN
        initializeDatabase();

        // Caused by: java.lang.IllegalStateException: Location is not set.
        // Yukarıdaki hatanın anlamı sayfayı bulamıyor.
        /*
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("view/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
         */

        // Başlangıçta Login Ekranı Gelsin
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("view/login.fxml"));
        Parent parent= fxmlLoader.load();
        stage.setTitle("Kullanıcı Yönetimi Login Sayfası");
        stage.setScene(new Scene(parent));
        stage.show();
    }

    /// //////////////////////////////////////////////////////////////////////////
    /// DATABASE
    // Proje ayağa kalkarken veritabanından örnek veriler eklesin
    // Database Başlangıçtaki değeri
    private void initializeDatabase() {
         try {
            Connection conn = SingletonDBConnection.getInstance().getConnection(); // STATIC BAĞLANTI ALINDI
            Statement stmt = conn.createStatement();

            String createTableSQL =
                    "-- User login\n" +
                            "CREATE TABLE IF NOT EXISTS users (\n" +
                            "    id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,\n" +
                            "    username VARCHAR(50) NOT NULL UNIQUE,\n" +
                            "    password VARCHAR(255) NOT NULL,\n" +
                            "    email VARCHAR(100) NOT NULL UNIQUE\n" +
                            ");\n" +

                            "-- Fişler Ekle\n" +
                            "CREATE TABLE  IF NOT EXISTS receipts (\n" +
                            "    id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,\n" +
                            "    receipt_number VARCHAR(50) NOT NULL UNIQUE,\n" +
                            "    receipt_date DATE NOT NULL,\n" +
                            "    tax_number VARCHAR(20) NOT NULL,\n" +
                            "    company_name VARCHAR(100) NOT NULL,\n" +
                            "    customer_name VARCHAR(100) NOT NULL,\n" +
                            "    description TEXT,\n" +
                            "    created_by VARCHAR(100) NOT NULL,\n" +
                            "    account_code VARCHAR(50) NOT NULL,\n" +
                            "    receipt_type VARCHAR(20) NOT NULL CHECK (receipt_type IN ('Ödeme', 'Tahsilat', 'Masraf', 'Gelir')),\n" +
                            "    amount DECIMAL(10,2) NOT NULL,\n" +
                            "    vat_rate DECIMAL(5,2) NOT NULL,\n" +
                            "    total_amount DECIMAL(10,2) NOT NULL,\n" +
                            "    payment_type VARCHAR(20) NOT NULL CHECK (payment_type IN ('Nakit', 'Kredi Kartı', 'Havale', 'Çek')),\n" +
                            "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP\n" +
                            ");";

            stmt.execute(createTableSQL);

            String insertDataSQL =
                    "MERGE INTO users (id, username, password, email) " +
                            "KEY(id) VALUES (1, 'HamitMızrak', 'root', 'hamitmizrak@gmail.com');";

            stmt.execute(insertDataSQL);

            System.out.println(SpecialColor.CYAN + "H2DB Veritabanı başarıyla oluşturuldu ve veri eklendi." + SpecialColor.RESET);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /// //////////////////////////////////////////////////////////////////////////
    /// PSVM
    public static void main(String[] args) {
        launch();
    }
}