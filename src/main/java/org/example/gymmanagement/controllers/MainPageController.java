package org.example.gymmanagement.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.gymmanagement.DAOS.DBWorkout;
import org.example.gymmanagement.StartApplication;
import org.example.gymmanagement.models.ModelUsers;
import org.example.gymmanagement.models.ModelWorkouts;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {

    @FXML
    private Label lblWelcome;

    @FXML
    private Button btnClientList;

    @FXML
    private Button btnExit;

    @FXML
    private TableView<ModelWorkouts> tblWorkout;

    @FXML
    private ToggleButton btnAllCouch;

    @FXML
    private Button btnTrainingPlan;

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

        if (currentUser.getIdRole() == 1) {
            btnAllCouch.setVisible(false);
            btnClientList.setVisible(false);
        }
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
    void showAllCouch() throws SQLException, ClassNotFoundException {
        tblWorkout.getColumns().clear();
        if (btnAllCouch.isSelected()) {
            btnAllCouch.setText("Скрыть полную информацию");

            ObservableList<ModelWorkouts> list = FXCollections.observableArrayList(dbWorkout.getWorkoutForCoachAll());
            tblWorkout.setItems(list);

            tblWorkout.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

            TableColumn<ModelWorkouts, String> coach = new TableColumn<>("Тренер");
            coach.setCellValueFactory(new PropertyValueFactory<>("coach"));

            TableColumn<ModelWorkouts, String> trainingDate = new TableColumn<>("Дата тренировки");
            trainingDate.setCellValueFactory(new PropertyValueFactory<>("trainingDate"));

            TableColumn<ModelWorkouts, String> trainingStructure = new TableColumn<>("Состав тренировки");
            trainingStructure.setCellValueFactory(new PropertyValueFactory<>("nameClient"));

            tblWorkout.getColumns().addAll(coach, trainingDate, trainingStructure);

        } else {
            btnAllCouch.setSelected(false);
            btnAllCouch.setText("Отобразить всю информацию");
            initializeTable();
        }
    }

    @FXML
    void goClientList() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("fxml/client-list.fxml"));

            Scene scene = new Scene(fxmlLoader.load());

            ClientListController clientListController = fxmlLoader.getController();
            clientListController.startPage(currentUser);

            Stage stage = (Stage) btnClientList.getScene().getWindow();
            stage.setTitle("Список клиентов");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            System.out.println(e);
        }


    }

    @FXML
    void doExit() {
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