package org.example.gymmanagement.controllers.forCoachControllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import org.example.gymmanagement.DAOS.DBExercises;
import org.example.gymmanagement.DAOS.DBTrainingPlan;
import org.example.gymmanagement.DAOS.DBUser;
import org.example.gymmanagement.DAOS.DBWorkout;
import org.example.gymmanagement.StartApplication;
import org.example.gymmanagement.controllers.MainPageController;
import org.example.gymmanagement.interfaces.Controller;
import org.example.gymmanagement.models.ModelExercises;
import org.example.gymmanagement.models.ModelRecord;
import org.example.gymmanagement.models.ModelUsers;
import org.example.gymmanagement.models.ModelWorkouts;
import org.example.gymmanagement.utils.ChangeTblColumn;

import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class StartWorkoutController extends ChangeTblColumn implements Controller, Initializable {

    @FXML
    private Button btnBack;

    @FXML
    private Button btnAddRecord;

    @FXML
    private ComboBox<ModelWorkouts> comboDate;

    @FXML
    private ListView<ModelExercises> listExercises;

    @FXML
    private TableView<ModelRecord> tblClientsRecord;

    @FXML
    private ComboBox<ModelExercises> comboExercises;

    @FXML
    private ComboBox<ModelUsers> comboClients;

    @FXML
    private TextField textRepetition;

    @FXML
    private TextField textWeight;

    private ModelUsers currentUser;

    private DBWorkout dbWorkout;
    private DBExercises dbExercises;
    private DBTrainingPlan dbTrainingPlan;
    private DBUser dbUser;

    @Override
    public void startPage(ModelUsers currentUser) throws SQLException, ClassNotFoundException {
        this.currentUser = currentUser;

        comboDate.setItems(FXCollections.observableArrayList(dbWorkout.getWorkoutsToMayStart(currentUser.getIdUser())));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbWorkout = new DBWorkout();
        dbExercises = new DBExercises();
        dbTrainingPlan = new DBTrainingPlan();
        dbUser = new DBUser();
        tblClientsRecord.setPlaceholder(new Label("Тренировка пуста"));

        try {
            comboExercises.setItems(FXCollections.observableArrayList(dbExercises.getExercises()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        tblClientsRecord.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        TableColumn<ModelRecord, String> client = new TableColumn<>("Клиент");
        client.setCellValueFactory(new PropertyValueFactory<>("nameClient"));
        changeColumnRecord(client);

        TableColumn<ModelRecord, String> exercise = new TableColumn<>("Упражнение");
        exercise.setCellValueFactory(new PropertyValueFactory<>("nameExercise"));
        changeColumnRecord(exercise);

        TableColumn<ModelRecord, String> weight = new TableColumn<>("Вес (кг)");
        weight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        changeColumnRecord(weight);

        TableColumn<ModelRecord, String> repetitions = new TableColumn<>("Повторения");
        repetitions.setCellValueFactory(new PropertyValueFactory<>("repetitions"));
        changeColumnRecord(repetitions);

        tblClientsRecord.getColumns().addAll(client, exercise, weight, repetitions);

        Pattern pattern = Pattern.compile("\\d*");

        UnaryOperator<TextFormatter.Change> filter = c -> {
            String text = c.getControlNewText();
            if (pattern.matcher(text).matches()) {
                return c;
            } else {
                return null;
            }
        };

        TextFormatter<Integer> formatterWeight = new TextFormatter<>(new IntegerStringConverter(), 0, filter);
        TextFormatter<Integer> formatterRepetitions = new TextFormatter<>(new IntegerStringConverter(), 0, filter);
        textWeight.setTextFormatter(formatterWeight);
        textRepetition.setTextFormatter(formatterRepetitions);
    }

    @FXML
    void setTrainingPlan(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (comboDate.getSelectionModel().getSelectedItem() != null) {

            listExercises.setItems(FXCollections.observableArrayList(dbExercises.getNameExercisesInPlan(dbTrainingPlan.getIdExercisesInPlan(comboDate.getSelectionModel().getSelectedItem().getId_workout()))));

            comboClients.setItems(FXCollections.observableArrayList(dbUser.getClientModelFromArray(dbWorkout.getClientInCurrentWorkout(comboDate.getSelectionModel().getSelectedItem().getId_workout()))));

        }
    }

    @FXML
    void doAddRecord(ActionEvent event) {
        if (comboDate.getSelectionModel().getSelectedItem() != null && comboExercises.getSelectionModel().getSelectedItem() != null && comboClients.getSelectionModel().getSelectedItem() != null && !textWeight.getText().isBlank() && !textRepetition.getText().isBlank() && (Integer.parseInt(textWeight.getText())) > 0 && (Integer.parseInt(textRepetition.getText()) > 0)) {

            ModelRecord modelRecord = new ModelRecord();
            modelRecord.setId_workout(comboDate.getSelectionModel().getSelectedItem().getId_workout());
            modelRecord.setId_client(comboClients.getSelectionModel().getSelectedItem().getIdUser());
            modelRecord.setNameClient(comboClients.getSelectionModel().getSelectedItem().getFio());
            modelRecord.setIdExercise(comboExercises.getSelectionModel().getSelectedItem().getId());
            modelRecord.setNameExercise(comboExercises.getSelectionModel().getSelectedItem().getExercise());
            modelRecord.setWeight(textWeight.getText());
            modelRecord.setRepetitions(textRepetition.getText());

            tblClientsRecord.getItems().add(modelRecord);

            textWeight.setText("0");
            textRepetition.setText("0");
            comboExercises.getSelectionModel().clearSelection();

        }
    }

    @FXML
    void doEndWorkout(ActionEvent event) throws SQLException, ClassNotFoundException {
        dbWorkout.doEndWorkout(tblClientsRecord);

        textWeight.setText("0");
        textRepetition.setText("0");
        comboExercises.getSelectionModel().clearSelection();
        tblClientsRecord.getItems().clear();
        comboDate.setItems(FXCollections.observableArrayList(dbWorkout.getWorkoutsToMayStart(currentUser.getIdUser())));
        listExercises.getItems().clear();
        comboClients.getItems().clear();
    }

    @FXML
    void goBack(ActionEvent event) {
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


}
