package org.example.gymmanagement.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.gymmanagement.DAOS.DBUser;
import org.example.gymmanagement.StartApplication;
import org.example.gymmanagement.models.ModelUsers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class ClientListController implements Initializable {

    @FXML
    private Button btnBack;

    @FXML
    private ComboBox<ModelUsers> comboClient;

    @FXML
    private TextField lblFio;

    @FXML
    private TextField lblLogin;

    @FXML
    private TextField lblPassword;

    @FXML
    private Label lblSaveGood;

    @FXML
    private Button btnSave;

    private ModelUsers currentUser;

    DBUser dbUser;

    public void initialize(URL url, ResourceBundle rb) {
        dbUser = new DBUser();
    }

    @FXML
    void goBack() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("fxml/main-page.fxml"));

            Scene scene = new Scene(fxmlLoader.load());

            MainPageController mainPageController = fxmlLoader.getController();
            mainPageController.startPage(currentUser);

            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setTitle("Добро пожаловать");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void startPage(ModelUsers currentUser) throws SQLException, ClassNotFoundException {
        this.currentUser = currentUser;
        initializeComboBox();
    }

    public void initializeComboBox() throws ClassNotFoundException, SQLException {
        comboClient.setItems(FXCollections.observableArrayList(dbUser.getAllClient()));
        comboClient.getSelectionModel().selectFirst();

        lblFio.setText(comboClient.getSelectionModel().getSelectedItem().getFio());
        lblLogin.setText(comboClient.getSelectionModel().getSelectedItem().getLogin());
        lblPassword.setText(comboClient.getSelectionModel().getSelectedItem().getPassword());

    }

    @FXML
    void showClientInfo() {
        lblFio.setText(comboClient.getSelectionModel().getSelectedItem().getFio());
        lblLogin.setText(comboClient.getSelectionModel().getSelectedItem().getLogin());
        lblPassword.setText(comboClient.getSelectionModel().getSelectedItem().getPassword());
    }

    @FXML
    void doSave() throws SQLException, ClassNotFoundException {
        dbUser.updateUsers(comboClient.getSelectionModel().getSelectedItem().getIdUser(),
                lblFio.getText(), lblLogin.getText(), lblPassword.getText());

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
