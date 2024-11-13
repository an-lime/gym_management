package org.example.gymmanagement.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.gymmanagement.DAOS.DBWorkout;
import org.example.gymmanagement.StartApplication;
import org.example.gymmanagement.controllers.forClientControllers.RequestController;
import org.example.gymmanagement.controllers.forCoachControllers.ClientListController;
import org.example.gymmanagement.controllers.forCoachControllers.TrainingPlanController;
import org.example.gymmanagement.interfaces.Controller;
import org.example.gymmanagement.models.ModelUsers;
import org.example.gymmanagement.models.ModelWorkouts;

import org.example.gymmanagement.utils.ChangeTblColumn;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainPageController implements Initializable, Controller {

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

    @FXML
    private VBox boxBtnForCoach;

    @FXML
    private Button btnRequestFromClient;

    @FXML
    private Button btnRequestToCoach;

    @FXML
    private Button btnStartWorkout;

    @FXML
    private Button btnAddNewWorkout;

    private ModelUsers currentUser;

    DBWorkout dbWorkout;

    ChangeTblColumn change;

    public Button getBtnClientList() {
        return btnClientList;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbWorkout = new DBWorkout();
        change = new ChangeTblColumn();
    }

    @Override
    public void startPage(ModelUsers currentUser) throws SQLException, ClassNotFoundException {
        this.currentUser = currentUser;
        lblWelcome.setText(lblWelcome.getText() + currentUser.getFio() + '!');
        initializeTable();

        if (currentUser.getIdRole() == 1) {
            btnAllCouch.setVisible(false);
            btnClientList.setVisible(false);

            boxBtnForCoach.setVisible(false);
            boxBtnForCoach.setManaged(false);
        } else {
            btnRequestToCoach.setManaged(false);
            btnRequestToCoach.setVisible(false);
        }
    }

    public void initializeTable() throws SQLException, ClassNotFoundException {

        if (currentUser.getIdRole() == 1) {

            ObservableList<ModelWorkouts> list = FXCollections.observableArrayList(dbWorkout.getWorkoutForClient(currentUser));
            tblWorkout.setItems(list);

            tblWorkout.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

            TableColumn<ModelWorkouts, String> coach = new TableColumn<>("Тренер");
            coach.setCellValueFactory(new PropertyValueFactory<>("coach"));
            coach.getStyleClass().add("cell");
            change.changeColumn(coach);

            TableColumn<ModelWorkouts, String> trainingDate = new TableColumn<>("Дата тренировки");
            trainingDate.setCellValueFactory(new PropertyValueFactory<>("trainingDate"));
            change.changeColumn(trainingDate);


            TableColumn<ModelWorkouts, String> trainingType = new TableColumn<>("Вид тренировки");
            trainingType.setCellValueFactory(new PropertyValueFactory<>("trainingType"));
            change.changeColumn(trainingType);


            tblWorkout.getColumns().addAll(coach, trainingDate, trainingType);

        } else {

            ObservableList<ModelWorkouts> list = FXCollections.observableArrayList(dbWorkout.getWorkoutForCoachOnly(currentUser));
            tblWorkout.setItems(list);

            tblWorkout.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

            TableColumn<ModelWorkouts, String> trainingDate = new TableColumn<>("Дата тренировки");
            trainingDate.setCellValueFactory(new PropertyValueFactory<>("trainingDate"));
            change.changeColumn(trainingDate);

            TableColumn<ModelWorkouts, String> trainingStructure = new TableColumn<>("Состав тренировки");
            trainingStructure.setCellValueFactory(new PropertyValueFactory<>("nameClient"));
            change.changeColumn(trainingStructure);

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
            change.changeColumn(coach);

            TableColumn<ModelWorkouts, String> trainingDate = new TableColumn<>("Дата тренировки");
            trainingDate.setCellValueFactory(new PropertyValueFactory<>("trainingDate"));
            change.changeColumn(trainingDate);

            TableColumn<ModelWorkouts, String> trainingStructure = new TableColumn<>("Состав тренировки");
            trainingStructure.setCellValueFactory(new PropertyValueFactory<>("nameClient"));
            change.changeColumn(trainingStructure);

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
            FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("fxml/forCoach/client-list.fxml"));

            Scene scene = new Scene(fxmlLoader.load());

            ClientListController clientListController = fxmlLoader.getController();
            clientListController.startPage(currentUser);

            Stage stage = (Stage) btnClientList.getScene().getWindow();
            stage.setTitle("Список клиентов");

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

    @FXML
    void goTrainingPlan() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("fxml/forCoach/training-plan.fxml"));

            Scene scene = new Scene(fxmlLoader.load());

            TrainingPlanController trainingPlanController = fxmlLoader.getController();
            trainingPlanController.startPage(currentUser);

            Stage stage = (Stage) btnTrainingPlan.getScene().getWindow();
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

    @FXML
    void goRequest() throws ClassNotFoundException, SQLException, IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("fxml/forClient/do-request.fxml"));

        Scene scene = new Scene(fxmlLoader.load());

        RequestController requestController = fxmlLoader.getController();
        requestController.startPage(currentUser);

        Stage stage = (Stage) btnTrainingPlan.getScene().getWindow();
        stage.setTitle("Планы тренировок");

        stage.setResizable(false);

        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        stage.setScene(scene);
        stage.setFullScreen(true);

        stage.show();

    }

    @FXML
    void goAddNewWorkout() throws ClassNotFoundException, SQLException, IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("fxml/forClient/do-request.fxml"));

        Scene scene = new Scene(fxmlLoader.load());

        RequestController requestController = fxmlLoader.getController();
        requestController.startPage(currentUser);

        Stage stage = (Stage) btnTrainingPlan.getScene().getWindow();
        stage.setTitle("Планы тренировок");

        stage.setResizable(false);

        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        stage.setScene(scene);
        stage.setFullScreen(true);

        stage.show();

    }

    @FXML
    void doExit() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("fxml/login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) btnExit.getScene().getWindow();
            stage.setTitle("Авторизуйтесь в системе");

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

