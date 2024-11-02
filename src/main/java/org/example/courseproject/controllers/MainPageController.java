package org.example.courseproject.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.example.courseproject.models.Users;

import java.net.URL;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {
    private Users currentUser;

    public Users getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Users currentUser) {
        this.currentUser = currentUser;
        lblWelcome.setText(lblWelcome.getText() + currentUser.getFio() + '!');
    }

    @FXML
    private Label lblWelcome;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
