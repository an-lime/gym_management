package org.example.gymmanagement.controllers.forCoachControllers;

import javafx.collections.FXCollections;
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
import org.example.gymmanagement.interfaces.StartController;
import org.example.gymmanagement.models.ModelExercises;
import org.example.gymmanagement.models.ModelRecord;
import org.example.gymmanagement.models.ModelUsers;
import org.example.gymmanagement.models.ModelWorkouts;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class StartWorkoutController implements StartController, Initializable {

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

    // метод для передачи данны о текущем пользователе
    // и инициализация всех объектов, зависящих от текущего пользователя
    @Override
    public void startPage(ModelUsers currentUser) throws SQLException, ClassNotFoundException {
        this.currentUser = currentUser;

        comboDate.setItems(FXCollections.observableArrayList(dbWorkout.getWorkoutsToMayStart(currentUser.getIdUser())));
    }

    // инициализиция и сокрытие некоторых объектов
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

        //инициализация таблицы с упражнениями на текущей тренировке
        tblClientsRecord.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        TableColumn<ModelRecord, String> client = new TableColumn<>("Клиент");
        client.setCellValueFactory(new PropertyValueFactory<>("nameClient"));

        TableColumn<ModelRecord, String> exercise = new TableColumn<>("Упражнение");
        exercise.setCellValueFactory(new PropertyValueFactory<>("nameExercise"));

        TableColumn<ModelRecord, String> weight = new TableColumn<>("Вес (кг)");
        weight.setCellValueFactory(new PropertyValueFactory<>("weight"));

        TableColumn<ModelRecord, String> repetitions = new TableColumn<>("Повторения");
        repetitions.setCellValueFactory(new PropertyValueFactory<>("repetitions"));

        client.getStyleClass().add("fontMedium");
        exercise.getStyleClass().add("fontMedium");
        weight.getStyleClass().add("fontMedium");
        repetitions.getStyleClass().add("fontMedium");

        tblClientsRecord.getColumns().addAll(client, exercise, weight, repetitions);

        // установка паттерна ввода только чисел в поля
        // веса и количества повторений
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

    // загрузка плана тренировки, если он был составлен
    @FXML
    void setTrainingPlan() throws SQLException, ClassNotFoundException {
        if (comboDate.getSelectionModel().getSelectedItem() != null) {

            tblClientsRecord.getItems().clear();

            listExercises.setItems(FXCollections.observableArrayList(dbExercises.getNameExercisesFromArray(dbTrainingPlan.getIdExercisesInPlan(comboDate.getSelectionModel().getSelectedItem().getId_workout()))));

            comboClients.setItems(FXCollections.observableArrayList(dbUser.getClientModelFromArray(dbWorkout.getClientInCurrentWorkout(comboDate.getSelectionModel().getSelectedItem().getId_workout()))));

        }
    }

    // добавление результата подхода в таблицу
    @FXML
    void doAddRecord() {
        if (comboDate.getSelectionModel().getSelectedItem() != null && comboExercises.getSelectionModel().getSelectedItem() != null && comboClients.getSelectionModel().getSelectedItem() != null && !textWeight.getText().isBlank() && !textRepetition.getText().isBlank() && (Integer.parseInt(textWeight.getText())) > 0 && (Integer.parseInt(textRepetition.getText()) > 0)) {

            // создание модели результата
            // и добавление её в таблицу
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

    // при завершении тренировки
    // все упражнения добавляются в поле текущей тренировки в базе
    @FXML
    void doEndWorkout() throws SQLException, ClassNotFoundException {
        if (tblClientsRecord.getItems().size() > 0) {
            dbWorkout.doEndWorkout(tblClientsRecord);
            textWeight.setText("0");
            textRepetition.setText("0");
            comboExercises.getSelectionModel().clearSelection();
            tblClientsRecord.getItems().clear();
            comboDate.setItems(FXCollections.observableArrayList(dbWorkout.getWorkoutsToMayStart(currentUser.getIdUser())));
            listExercises.getItems().clear();
            comboClients.getItems().clear();
        }

    }

    // переход на главный экран
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
            System.out.println(e.getMessage());
        }
    }


}
