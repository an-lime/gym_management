package org.example.gymmanagement.controllers.forCoachControllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.gymmanagement.DAOS.DBExercises;
import org.example.gymmanagement.DAOS.DBRequests;
import org.example.gymmanagement.DAOS.DBWorkout;
import org.example.gymmanagement.StartApplication;
import org.example.gymmanagement.interfaces.StartController;
import org.example.gymmanagement.models.ModelRequest;
import org.example.gymmanagement.models.ModelUsers;
import org.example.gymmanagement.models.ModelWorkouts;
import org.example.gymmanagement.utils.ChangeTblColumn;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ResourceBundle;

public class RequestsForCoachController extends ChangeTblColumn implements StartController, Initializable {

    @FXML
    private TableView<ModelRequest> tblListRequests;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnRej;

    ModelUsers currentUser = new ModelUsers();

    DBRequests dbRequests;
    DBWorkout dbWorkout;
    DBExercises dbExercises;

    @Override
    public void startPage(ModelUsers currentUser) throws SQLException, ClassNotFoundException {
        this.currentUser = currentUser;
        initializeTable();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbRequests = new DBRequests();
        dbWorkout = new DBWorkout();
        dbExercises = new DBExercises();
        btnAdd.setDisable(true);
        btnRej.setDisable(true);

        tblListRequests.setPlaceholder(new Label("Таблица заявок пуста"));
    }

    public void initializeTable() throws SQLException, ClassNotFoundException {
        dbRequests.deleteAllRequest();
        tblListRequests.getItems().clear();
        tblListRequests.getColumns().clear();

        tblListRequests.setItems(FXCollections.observableArrayList(dbRequests.getRequestCurrentCoach(currentUser.getIdUser())));
        tblListRequests.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        TableColumn<ModelRequest, String> client = new TableColumn<>("Клиент");
        client.setCellValueFactory(new PropertyValueFactory<>("client"));
        client.getStyleClass().add("cell");
        changeColumnRequest(client);

        TableColumn<ModelRequest, String> date = new TableColumn<>("Дата тренировки");
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        date.getStyleClass().add("cell");
        changeColumnRequest(date);

        TableColumn<ModelRequest, String> type = new TableColumn<>("Тип тренировки");
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        type.getStyleClass().add("cell");
        changeColumnRequest(type);

        TableColumn<ModelRequest, String> exercise = new TableColumn<>("Упражнения");
        exercise.setCellValueFactory(new PropertyValueFactory<>("exercise"));
        exercise.getStyleClass().add("cell");
        changeColumnRequest(exercise);

        tblListRequests.getColumns().addAll(client, date, type, exercise);
    }

    @FXML
    void showBtn() {

        if (tblListRequests.getSelectionModel().getSelectedItem() != null) {
            btnAdd.setDisable(false);
            btnRej.setDisable(false);
        }

    }

    @FXML
    void doAdd() throws ClassNotFoundException, SQLException, ParseException, IOException {
        if (dbWorkout.getExistWorkout(tblListRequests.getSelectionModel().getSelectedItem().getIdRequest()) == 1) {
            ModelRequest modelRequest = tblListRequests.getSelectionModel().getSelectedItem();
            dbWorkout.updateArray(modelRequest.getIdClient(), modelRequest.getIdCoach(), dbRequests.getRequestDate(modelRequest.getIdRequest()));
            doRej();
            initializeTable();
        } else {
            Integer[] clientsArr = new Integer[1];
            clientsArr[0] = tblListRequests.getSelectionModel().getSelectedItem().getIdClient();
            dbWorkout.addNewWorkout(currentUser.getIdUser(), dbRequests.getRequestDate(tblListRequests.getSelectionModel().getSelectedItem().getIdRequest()), clientsArr);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(StartApplication.class.getResource("fxml/forCoach/training-plan.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Планы тренировок");
            stage.setScene(new Scene(root));

            TrainingPlanController trainingPlanController = loader.getController();
            trainingPlanController.startPage(currentUser);

            for (ModelWorkouts modelWorkouts : trainingPlanController.getComboTrainingDate().getItems()) {

                if (modelWorkouts.getTrainingDate().equals(tblListRequests.getSelectionModel().getSelectedItem().getDate())) {
                    trainingPlanController.getComboTrainingDate().getSelectionModel().select(modelWorkouts);
                    trainingPlanController.showExercises();
                    trainingPlanController.getListExercises().setItems(FXCollections.observableArrayList(dbExercises.getNameExercisesModelWorkout(dbRequests.getExersices(tblListRequests.getSelectionModel().getSelectedItem().getIdRequest()))));
                }
            }

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(this.btnAdd.getScene().getWindow());

            stage.showAndWait();

            doRej();
            initializeTable();

        }
    }

    @FXML
    void doRej() throws ClassNotFoundException, SQLException {
        dbRequests.deleteRequest(tblListRequests.getSelectionModel().getSelectedItem().getIdRequest());
        initializeTable();
    }

}
