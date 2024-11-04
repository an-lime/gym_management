package org.example.courseproject.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.courseproject.DAOS.DBWorkout;
import org.example.courseproject.StartApplication;
import org.example.courseproject.models.ModelUsers;
import org.example.courseproject.models.ModelWorkouts;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {

    @FXML
    private Label lblWelcome;

    @FXML
    private Button btnExit;

    @FXML
    private TableView<ModelWorkouts> tblWorkout;

    private ModelUsers currentUser;

    DBWorkout dbWorkout;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbWorkout = new DBWorkout();
    }

    public void startPage(ModelUsers currentUser) throws SQLException, ClassNotFoundException {
        this.currentUser = currentUser;
        lblWelcome.setText(lblWelcome.getText() + currentUser.getFio() + '!');
        initializeTable();
    }

    public void initializeTable() throws SQLException, ClassNotFoundException {

        if (currentUser.getIdRole() == 1) {

            ObservableList<ModelWorkouts> list = FXCollections.observableArrayList(dbWorkout.getWorkoutForClient(currentUser));
            tblWorkout.setItems(list);

            tblWorkout.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
            TableColumn<ModelWorkouts, String> coach = new TableColumn<>("Тренер");
            coach.setCellValueFactory(new PropertyValueFactory<>("coach"));

            TableColumn<ModelWorkouts, String> trainingDate = new TableColumn<>("Дата тренировки");
            trainingDate.setCellValueFactory(new PropertyValueFactory<>("trainingDate"));

            TableColumn<ModelWorkouts, String> trainingType = new TableColumn<>("Вид тренировки");
            trainingType.setCellValueFactory(new PropertyValueFactory<>("trainingType"));

            tblWorkout.getColumns().addAll(coach, trainingDate, trainingType);

        } else {

            ObservableList<ModelWorkouts> list = FXCollections.observableArrayList(dbWorkout.getWorkoutForCoachOnly(currentUser));
            tblWorkout.setItems(list);

            tblWorkout.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

            TableColumn<ModelWorkouts, String> trainingDate = new TableColumn<>("Дата тренировки");
            trainingDate.setCellValueFactory(new PropertyValueFactory<>("trainingDate"));

            TableColumn<ModelWorkouts, String> trainingStructure = new TableColumn<>("Состав тренировки");
            trainingStructure.setCellValueFactory(new PropertyValueFactory<>("nameClient"));

            tblWorkout.getColumns().addAll(trainingDate, trainingStructure);


        }


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

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}