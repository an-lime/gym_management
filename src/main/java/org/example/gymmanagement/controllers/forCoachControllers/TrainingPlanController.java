package org.example.gymmanagement.controllers.forCoachControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.gymmanagement.DAOS.DBExercises;
import org.example.gymmanagement.DAOS.DBTrainingPlan;
import org.example.gymmanagement.DAOS.DBWorkout;
import org.example.gymmanagement.StartApplication;
import org.example.gymmanagement.controllers.MainPageController;
import org.example.gymmanagement.interfaces.StartController;
import org.example.gymmanagement.models.ModelExercises;
import org.example.gymmanagement.models.ModelUsers;
import org.example.gymmanagement.models.ModelWorkouts;
import org.example.gymmanagement.utils.SimpleUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class TrainingPlanController implements Initializable, StartController {

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

    @FXML
    private CheckBox checkAllPlan;

    @FXML
    private TableView<ModelWorkouts> tblWorkoutStructure;

    @FXML
    private VBox vBoxStructure;

    private ModelUsers currentUser;

    DBExercises dbExercises;
    DBWorkout dbWorkout;
    DBTrainingPlan dbTrainingPlan;

    ContextMenu contextMenu = new ContextMenu();
    MenuItem itemDelete = new MenuItem("Удалить упражнение");

    public ComboBox<ModelWorkouts> getComboTrainingDate() {
        return comboTrainingDate;
    }

    public ListView<ModelExercises> getListExercises() {
        return listExercises;
    }

    // инициализиция и сокрытие некоторых объектов
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        vBoxStructure.setVisible(false);
        vBoxStructure.setManaged(false);

        contextMenu.getItems().add(itemDelete);

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

    // метод для передачи данны о текущем пользователе
    // и инициализация всех объектов, зависящих от текущего пользователя
    @Override
    public void startPage(ModelUsers currentUser) throws SQLException, ClassNotFoundException {

        Stage stage = (Stage) btnBack.getScene().getWindow();
        if (stage != null && !stage.isFullScreen()) {
            btnBack.setVisible(false);
        }

        this.currentUser = currentUser;

        dbTrainingPlan.deleteAllPlan();

        if (checkAllPlan.isSelected()) {
            comboTrainingDate.setItems(FXCollections.observableArrayList(dbWorkout.getWorkoutHavePlan(currentUser)));
        } else {
            comboTrainingDate.setItems(FXCollections.observableArrayList(dbWorkout.getWorkoutNeedPlan(currentUser)));
        }

        comboExercises.setItems(FXCollections.observableArrayList(dbExercises.getExercises()));
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

    // отображение добавленных упражнений и клиентов в уже составленный план тренировки
    @FXML
    void showExercises() throws SQLException, ClassNotFoundException {


        // отображение клиентов
        if (comboTrainingDate.getSelectionModel().getSelectedItem() != null) {
            tblWorkoutStructure.getColumns().clear();
            vBoxStructure.setVisible(true);
            vBoxStructure.setManaged(true);
            ObservableList<ModelWorkouts> list = FXCollections.observableArrayList(dbWorkout.getCurrentWorkoutForCoachOnly(comboTrainingDate.getSelectionModel().getSelectedItem().getId_workout()));
            tblWorkoutStructure.setItems(list);
            tblWorkoutStructure.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
            TableColumn<ModelWorkouts, String> trainingStructure = new TableColumn<>("Состав тренировки");
            trainingStructure.setCellValueFactory(new PropertyValueFactory<>("nameClient"));
            trainingStructure.getStyleClass().add("fontMedium");
            tblWorkoutStructure.getColumns().addAll(trainingStructure);
        }


        hBoxExercises.setVisible(true);
        hBoxExercises.setVisible(true);

        // отображение упражнений
        if (checkAllPlan.isSelected() && comboTrainingDate.getSelectionModel().getSelectedItem() != null) {
            listExercises.setItems(FXCollections.observableArrayList(dbExercises.getNameExercisesFromArray(dbTrainingPlan.getIdExercisesInPlan(comboTrainingDate.getSelectionModel().getSelectedItem().getId_workout()))));
        }
    }

    // добвление упражнения в таблицу
    @FXML
    void addExercises() {
        if (comboExercises.getSelectionModel().getSelectedItem() != null) {
            listExercises.getItems().add(comboExercises.getSelectionModel().getSelectedItem());
            comboExercises.getSelectionModel().clearSelection();
        }
    }

    // отображение кнопки "удалить" при клике пкм по упражнению в таблице
    @FXML
    void showContext() {

        SimpleUtils simpleUtils = new SimpleUtils();
        simpleUtils.showContextDelete(listExercises, contextMenu, itemDelete);

    }

    // непосредственное добавление / изменение плана в базе данных
    @FXML
    void doAddPlan() throws SQLException, ClassNotFoundException {

        if (!listExercises.getItems().isEmpty()) {

            Integer[] exercisesArr = new Integer[listExercises.getItems().size()];
            int i = 0;
            for (ModelExercises modelExercises : listExercises.getItems()) {
                exercisesArr[i] = modelExercises.getId();
                i++;
            }

            if (btnAddPlan.getText().equals("Добавить план")) {
                dbTrainingPlan.addPlan(comboTrainingDate.getSelectionModel().getSelectedItem().getId_workout(), currentUser.getIdUser(), exercisesArr);

            } else if (btnAddPlan.getText().equals("Изменить план")) {
                dbTrainingPlan.changePlan(comboTrainingDate.getSelectionModel().getSelectedItem().getId_workout(), exercisesArr);

            }


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

            comboTrainingDate.getSelectionModel().clearSelection();

            listExercises.getItems().clear();
            startPage(currentUser);
            hBoxExercises.setVisible(false);

            tblWorkoutStructure.getColumns().clear();
            vBoxStructure.setVisible(false);
            vBoxStructure.setManaged(false);
        }


    }

    // переход на окно добавления новых упражнений
    // в общий список упражнений
    @FXML
    void doAddNewExercise() throws ClassNotFoundException, SQLException, IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(StartApplication.class.getResource("fxml/forCoach/exercises.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Список упражнений");
        stage.setScene(new Scene(root));

        ExercisesController exercisesController = loader.getController();
        exercisesController.startPage(currentUser);

        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(this.btnAddNewExercise.getScene().getWindow());

        stage.showAndWait();

        comboExercises.setItems(FXCollections.observableArrayList(dbExercises.getExercises()));

    }

    // отображение уже составленных планов
    @FXML
    void showAllPlan() throws ClassNotFoundException, SQLException {
        vBoxStructure.setVisible(false);
        vBoxStructure.setManaged(false);
        tblWorkoutStructure.getColumns().clear();
        // кнопка "добавить план" меняется на "изменить план",
        // а содержимое ComboBox меняется
        if (checkAllPlan.isSelected()) {

            btnAddPlan.setText("Изменить план");

            listExercises.getItems().clear();
            comboExercises.getSelectionModel().clearSelection();

            comboTrainingDate.setItems(FXCollections.observableArrayList(dbWorkout.getWorkoutHavePlan(currentUser)));

            hBoxExercises.setVisible(false);
        } else {

            // кнопки и содержимое меняются обратно

            vBoxStructure.setVisible(false);
            vBoxStructure.setManaged(false);
            tblWorkoutStructure.getColumns().clear();

            btnAddPlan.setText("Добавить план");

            listExercises.getItems().clear();
            comboExercises.getSelectionModel().clearSelection();

            comboTrainingDate.setItems(FXCollections.observableArrayList(dbWorkout.getWorkoutNeedPlan(currentUser)));

            hBoxExercises.setVisible(false);
        }
    }
}
