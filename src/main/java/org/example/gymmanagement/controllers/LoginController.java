package org.example.gymmanagement.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.gymmanagement.DAOS.DBUser;
import org.example.gymmanagement.StartApplication;
import org.example.gymmanagement.models.ModelUsers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class LoginController implements Initializable {

    @FXML
    private Button btnClose;

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

                ModelUsers user = dbUser.getCurrentUser(loginTxt.getText(), enterPassword);
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
                    mainPageController.startPage(user);

                    Stage stage = (Stage) loginBtn.getScene().getWindow();
                    stage.setTitle("Спортзал \"Штангелина\"");

                    stage.setResizable(false);

                    stage.setFullScreenExitHint("");
                    stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

                    stage.setScene(scene);
                    stage.setFullScreen(true);

                    stage.show();

                }

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
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

    @FXML
    void closeApp() {
        Platform.exit();
    }

}