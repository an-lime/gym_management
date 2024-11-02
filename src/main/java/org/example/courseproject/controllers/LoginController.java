package org.example.courseproject.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.courseproject.DAOS.DBUser;
import org.example.courseproject.StartApplication;
import org.example.courseproject.models.Users;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class LoginController implements Initializable {

    @FXML
    private CheckBox checkShowPassword;

    @FXML
    private Label error;

    @FXML
    private Button loginBtn;

    @FXML
    private TextField loginTxt;

    @FXML
    private TextField password;

    @FXML
    private PasswordField passwordField;

    DBUser dbUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbUser = new DBUser();
    }

    @FXML
    void enter() {
        String enterPassword = "";
        if (password.isVisible()) {
            enterPassword = password.getText();
        } else if (passwordField.isVisible()) {
            enterPassword = passwordField.getText();
        }

        try {

            if (!loginTxt.getText().isEmpty() & !enterPassword.isEmpty()) {

                Users user = dbUser.getUser(loginTxt.getText(), enterPassword);
                if (user == null) {
                    error.setVisible(true);
                    error.setText("Ошибка входа");
                    error.setTextFill(Color.RED);

                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        public void run() {
                            error.setVisible(false);
                        }
                    };

                    timer.schedule(task, 2000);
                }
                if (user != null) {

                    FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("fxml/main-page.fxml"));

                    Scene scene = new Scene(fxmlLoader.load());

                    MainPageController mainPageController = fxmlLoader.getController();
                    mainPageController.setIdCurrentUser(dbUser.getUser(loginTxt.getText(), enterPassword).getIdUser());

                    Stage stage = (Stage) loginBtn.getScene().getWindow();
                    stage.setTitle("Добро пожаловать");
                    stage.setScene(scene);
                    stage.show();

                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void toggleVisiblePassword() {
        if (!checkShowPassword.isSelected()) {
            passwordField.setText(password.getText());
            password.setVisible(false);
            passwordField.setVisible(true);
            return;
        }
        password.setText(passwordField.getText());
        passwordField.setVisible(false);
        password.setVisible(true);

    }

}