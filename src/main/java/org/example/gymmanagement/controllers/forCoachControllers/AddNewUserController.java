package org.example.gymmanagement.controllers.forCoachControllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.gymmanagement.DAOS.DBUser;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class AddNewUserController implements Initializable {

    @FXML
    private Button btnSave;

    @FXML
    private TextField lblFio;

    @FXML
    private TextField lblLogin;

    @FXML
    private TextField lblPassword;

    @FXML
    private TextField lblPhone;

    @FXML
    private Label lblSaveGood;

    DBUser dbUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbUser = new DBUser();
    }

    // добавление нового пользователя
    @FXML
    void doAdd() throws ClassNotFoundException, SQLException {

        // есть ли пользователь с таким логином?
        if (dbUser.getCurrentUserFromLogin(lblLogin.getText()) != 0) {

            lblSaveGood.setText("Такой пользователь уже есть!");
            lblSaveGood.setVisible(true);

            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                public void run() {
                    lblSaveGood.setVisible(false);
                }
            };

            timer.schedule(task, 2000);

            return;
        }

        // если все поля заполнены, то пользователь добавляется
        if (!lblFio.getText().trim().isEmpty() && !lblLogin.getText().trim().isEmpty() && !lblPassword.getText().trim().isEmpty() && !lblPhone.getText().trim().isEmpty()) {
            dbUser.addNewUser(lblLogin.getText(), lblPassword.getText(), lblFio.getText(), lblPhone.getText());

            lblFio.clear();
            lblLogin.clear();
            lblPassword.clear();
            lblPhone.clear();

            lblSaveGood.setText("Пользователь добавлен!");
            lblSaveGood.setVisible(true);

            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                public void run() {
                    lblSaveGood.setVisible(false);
                }
            };

            timer.schedule(task, 2000);

        } else {
            // если поля не заполнены:
            lblSaveGood.setText("Заполните все поля!");
            lblSaveGood.setVisible(true);

            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                public void run() {
                    lblSaveGood.setVisible(false);
                }
            };

            timer.schedule(task, 2000);
        }
    }
}