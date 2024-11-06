package org.example.gymmanagement.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.gymmanagement.StartApplication;
import org.example.gymmanagement.interfaces.Controller;
import org.example.gymmanagement.models.ModelUsers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TrainingPlanController implements Initializable, Controller {

    @FXML
    private Button btnBack;

    private ModelUsers currentUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void startPage(ModelUsers currentUser) throws SQLException, ClassNotFoundException {
        this.currentUser = currentUser;
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
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
