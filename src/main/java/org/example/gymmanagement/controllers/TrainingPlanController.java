package org.example.gymmanagement.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.gymmanagement.DAOS.DBExercises;
import org.example.gymmanagement.DAOS.DBTrainingPlan;
import org.example.gymmanagement.DAOS.DBWorkout;
import org.example.gymmanagement.StartApplication;
import org.example.gymmanagement.interfaces.Controller;
import org.example.gymmanagement.models.ModelExercises;
import org.example.gymmanagement.models.ModelUsers;
import org.example.gymmanagement.models.ModelWorkouts;

import java.net.URL;
import java.sql.Array;
import java.sql.SQLException;
import java.util.*;

public class TrainingPlanController implements Initializable, Controller {

    @FXML
    private Button btnBack;

    @FXML
    private Button btnAddPlan;

    @FXML
    private ComboBox<ModelExercises> comboExercises;

    @FXML
    private ComboBox<ModelWorkouts> comboTrainingDate;

    @FXML
    private ListView<ModelExercises> listExercises;

    @FXML
    private HBox hBoxExercises;

    @FXML
    private Label lblPlanDone;

    @FXML
    private Button btnAddNewExercise;

    private ModelUsers currentUser;

    DBExercises dbExercises;
    DBWorkout dbWorkout;
    DBTrainingPlan dbTrainingPlan;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        hBoxExercises.setVisible(false);
        lblPlanDone.setVisible(false);
        lblPlanDone.setManaged(false);

        try {
            dbExercises = new DBExercises();
            dbWorkout = new DBWorkout();
            dbTrainingPlan = new DBTrainingPlan();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void startPage(ModelUsers currentUser) throws SQLException, ClassNotFoundException {
        this.currentUser = currentUser;
        comboTrainingDate.setItems(FXCollections.observableArrayList(dbWorkout.getWorkoutNeedPlan(currentUser)));

        comboExercises.setItems(FXCollections.observableArrayList(dbExercises.getExercises()));
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

    @FXML
    void showExercises() {
        hBoxExercises.setVisible(true);
    }

    @FXML
    void addExercises() {
        if (comboExercises.getSelectionModel().getSelectedItem() != null) {
            listExercises.getItems().add(comboExercises.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    void doAddPlan() throws SQLException, ClassNotFoundException {

        if (!listExercises.getItems().isEmpty()) {

            Integer[] exercisesArr = new Integer[listExercises.getItems().size()];
            int i = 0;
            for (ModelExercises modelExercises : listExercises.getItems()) {
                exercisesArr[i] = modelExercises.getId();
                i++;
            }

            dbTrainingPlan.addPlan(comboTrainingDate.getSelectionModel().getSelectedItem().getId_workout(),
                    currentUser.getIdUser(), exercisesArr);

            lblPlanDone.setVisible(true);
            lblPlanDone.setManaged(true);

            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                public void run() {
                    lblPlanDone.setVisible(false);
                    lblPlanDone.setManaged(false);
                }
            };

            timer.schedule(task, 2000);

            listExercises.getItems().clear();
            startPage(currentUser);
            hBoxExercises.setVisible(false);
        }


    }

    @FXML
    void doAddNewExercise() {
        //TODO возможность добавления новых упражнений

    }
}
