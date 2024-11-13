package org.example.gymmanagement.controllers.forCoachControllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.gymmanagement.DAOS.DBUser;
import org.example.gymmanagement.StartApplication;
import org.example.gymmanagement.controllers.MainPageController;
import org.example.gymmanagement.interfaces.Controller;
import org.example.gymmanagement.models.ModelUsers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class ClientListController implements Initializable, Controller {

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

    @FXML
    private Pane paneConfirm;

    @FXML
    private Label lblConfirmAction;

    @FXML
    private TextField textPassword;

    private ModelUsers currentUser;

    private int step = 0;

    DBUser dbUser;

    public void initialize(URL url, ResourceBundle rb) {
        dbUser = new DBUser();
        paneConfirm.setManaged(false);
        paneConfirm.setVisible(false);
    }

    @Override
    public void startPage(ModelUsers currentUser) throws SQLException, ClassNotFoundException {
        this.currentUser = currentUser;
        initializeComboBox();
    }

    @FXML
    void goBack() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("fxml/main-page.fxml"));

            Scene scene = new Scene(fxmlLoader.load());

            MainPageController mainPageController = fxmlLoader.getController();
            mainPageController.startPage(currentUser);

            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setTitle("Спортзал \"Штангелина\"");

            stage.setResizable(false);

            stage.setFullScreenExitHint("");
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

            stage.setScene(scene);
            stage.setFullScreen(true);

            stage.show();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void initializeComboBox() throws ClassNotFoundException, SQLException {
        comboClient.setItems(FXCollections.observableArrayList(dbUser.getAllClient()));
        comboClient.getSelectionModel().selectFirst();

        lblFio.setText(comboClient.getSelectionModel().getSelectedItem().getFio());
        lblLogin.setText(comboClient.getSelectionModel().getSelectedItem().getLogin());
        lblPassword.setText(comboClient.getSelectionModel().getSelectedItem().getPassword());

        comboClient.setStyle("-fx-font: 15px \"Verdana\";");

    }

    @FXML
    void showClientInfo() {
        paneConfirm.setManaged(false);
        textPassword.setText("");
        step = 0;
        lblConfirmAction.setVisible(true);
        paneConfirm.setVisible(false);
        lblFio.setText(comboClient.getSelectionModel().getSelectedItem().getFio());
        lblLogin.setText(comboClient.getSelectionModel().getSelectedItem().getLogin());
        lblPassword.setText(comboClient.getSelectionModel().getSelectedItem().getPassword());
    }

    @FXML
    void doSave() throws SQLException, ClassNotFoundException {

        if (step == 0) {
            paneConfirm.setManaged(true);
            paneConfirm.setVisible(true);

            step = 1;
        } else if (step == 1) {

            if (textPassword.getText().equals(currentUser.getPassword())) {

                dbUser.updateUsers(comboClient.getSelectionModel().getSelectedItem().getIdUser(),
                        lblFio.getText(), lblLogin.getText(), lblPassword.getText());

                lblSaveGood.setText("Изменения прошли успешно!");
                lblSaveGood.setVisible(true);

                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    public void run() {
                        lblSaveGood.setVisible(false);
                    }
                };

                timer.schedule(task, 2000);

                textPassword.setText("");
                lblConfirmAction.setVisible(true);
                paneConfirm.setManaged(false);
                paneConfirm.setVisible(false);
                step = 0;
            } else {
                lblSaveGood.setVisible(true);
                lblSaveGood.setText("Проверьте правильность пароля!");

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

    @FXML
    void hideConfirmLbl(MouseEvent event) {
        lblConfirmAction.setVisible(false);
        textPassword.requestFocus();
    }

}
