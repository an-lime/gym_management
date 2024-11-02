package org.example.courseproject.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.courseproject.StartApplication;
import org.example.courseproject.models.Users;

import java.net.URL;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {

    private Users currentUser;

    @FXML
    private Label lblWelcome;

    @FXML
    private Button btnExit;

    public Users getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Users currentUser) {
        this.currentUser = currentUser;
        lblWelcome.setText(lblWelcome.getText() + currentUser.getFio() + '!');
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    void doExit(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("fxml/login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) btnExit.getScene().getWindow();
            stage.setTitle("Авторизуйтесь в системе");
            stage.setScene(scene);
            stage.show();

        } catch (
                Exception e) {
            System.out.println(e);
        }
    }
}