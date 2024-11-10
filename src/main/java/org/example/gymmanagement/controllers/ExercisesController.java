package org.example.gymmanagement.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.gymmanagement.DAOS.DBExercises;
import org.example.gymmanagement.StartApplication;
import org.example.gymmanagement.interfaces.Controller;
import org.example.gymmanagement.models.ModelExercises;
import org.example.gymmanagement.models.ModelUsers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ExercisesController implements Initializable, Controller {

    @FXML
    private Button btnBack;

    @FXML
    private ListView<ModelExercises> listExercises;

    @FXML
    private TextField textNewExercise;

    private ModelUsers currentUser;

    private DBExercises dbExercises;

    ContextMenu contextMenu = new ContextMenu();
    MenuItem itemDelete = new MenuItem("Удалить упражнение");

    ModelExercises modelExercises = new ModelExercises();


    @Override
    public void startPage(ModelUsers currentUser) throws SQLException, ClassNotFoundException {
        this.currentUser = currentUser;
        listExercises.setItems(FXCollections.observableArrayList(dbExercises.getExercises()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        contextMenu.getItems().addAll(itemDelete);
        dbExercises = new DBExercises();
    }

    @FXML
    void addNewExercise() throws ClassNotFoundException, SQLException {
        if (!textNewExercise.getText().trim().equals("")) {
            dbExercises.addNewExercise(textNewExercise.getText());
            textNewExercise.clear();
            listExercises.setItems(FXCollections.observableArrayList(dbExercises.getExercises()));
        }
    }

    @FXML
    void deleteExercise(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) {
            contextMenu.show(listExercises, event.getScreenX(), event.getScreenY());
            itemDelete.setOnAction(_ -> {
                modelExercises = listExercises.getSelectionModel().getSelectedItem();
                if (modelExercises != null) {
                    try {

                        dbExercises.deleteExercise(modelExercises.getId());
                        listExercises.getItems().clear();
                        listExercises.setItems(FXCollections.observableArrayList(dbExercises.getExercises()));

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            });
        }

    }

    @FXML
    void goBack() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("fxml/training-plan.fxml"));

            Scene scene = new Scene(fxmlLoader.load());

            TrainingPlanController trainingPlanController = fxmlLoader.getController();
            trainingPlanController.startPage(currentUser);

            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setTitle("Планы тренировок");

            stage.setResizable(false);

            stage.setFullScreenExitHint("");
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

            stage.setScene(scene);
            stage.setFullScreen(true);

            stage.show();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


}
