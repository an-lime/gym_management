package org.example.gymmanagement.controllers;

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
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.gymmanagement.DAOS.DBClubCard;
import org.example.gymmanagement.DAOS.DBRequests;
import org.example.gymmanagement.DAOS.DBWorkout;
import org.example.gymmanagement.StartApplication;
import org.example.gymmanagement.controllers.forClientControllers.RequestController;
import org.example.gymmanagement.controllers.forCoachControllers.*;
import org.example.gymmanagement.interfaces.StartController;
import org.example.gymmanagement.models.ModelUsers;
import org.example.gymmanagement.models.ModelWorkouts;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainPageController implements Initializable, StartController {

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

    @FXML
    private Button btnDeleteWorkout;

    @FXML
    private Button btnResult;

    @FXML
    private VBox boxClubCard;

    @FXML
    private Label textClubCard;

    private ModelUsers currentUser;

    DBWorkout dbWorkout;
    DBRequests dbRequests;
    DBClubCard dbClubCard;

    // инициализиция некоторых объектов
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbWorkout = new DBWorkout();
        dbRequests = new DBRequests();
        dbClubCard = new DBClubCard();

        tblWorkout.setPlaceholder(new Label("Таблица тренировок пуста"));
    }

    // метод для передачи данны о текущем пользователе
    // и инициализация всех объектов, зависящих от текущего пользователя
    @Override
    public void startPage(ModelUsers currentUser) throws SQLException, ClassNotFoundException {
        this.currentUser = currentUser;
        lblWelcome.setText(lblWelcome.getText() + currentUser.getFio() + '!');
        initializeTable();

        // сокрытие большей части функционала,
        // если авторизованный пользователь -- клиент
        if (currentUser.getIdRole() == 1) {

            textClubCard.setText(textClubCard.getText() + dbClubCard.getBalance(currentUser.getIdUser()) + " руб.");

            btnAllCouch.setVisible(false);
            btnClientList.setVisible(false);

            btnDeleteWorkout.setVisible(false);
            btnDeleteWorkout.setManaged(false);
            btnRequestFromClient.setVisible(false);
            btnRequestFromClient.setManaged(false);
            btnTrainingPlan.setVisible(false);
            btnTrainingPlan.setManaged(false);
            btnAddNewWorkout.setVisible(false);
            btnAddNewWorkout.setManaged(false);
            btnStartWorkout.setVisible(false);
            btnStartWorkout.setManaged(false);

        } else {
            // сокрытие лишь части функционала,
            // если авторизованный пользователь -- тренер
            btnRequestToCoach.setManaged(false);
            btnRequestToCoach.setVisible(false);
            boxClubCard.setVisible(false);
        }

        try {
            if (dbRequests.getRequestCurrentCoach(currentUser.getIdUser()).isEmpty()) {
                btnRequestFromClient.setDisable(true);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void initializeTable() throws SQLException, ClassNotFoundException {

        dbWorkout.deleteAllWorkout();

        // если авторизованный пользователь -- клиент,
        // то он видит такую таблицу:
        if (currentUser.getIdRole() == 1) {

            ObservableList<ModelWorkouts> list = FXCollections.observableArrayList(dbWorkout.getWorkoutForClient(currentUser));
            tblWorkout.setItems(list);

            tblWorkout.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

            TableColumn<ModelWorkouts, String> coach = new TableColumn<>("Тренер");
            coach.setCellValueFactory(new PropertyValueFactory<>("coach"));
            coach.getStyleClass().add("cell");

            TableColumn<ModelWorkouts, String> trainingDate = new TableColumn<>("Дата тренировки");
            trainingDate.setCellValueFactory(new PropertyValueFactory<>("trainingDate"));


            TableColumn<ModelWorkouts, String> trainingType = new TableColumn<>("Вид тренировки");
            trainingType.setCellValueFactory(new PropertyValueFactory<>("trainingType"));

            coach.getStyleClass().add("fontMedium");
            trainingDate.getStyleClass().add("fontMedium");
            trainingType.getStyleClass().add("fontMedium");


            tblWorkout.getColumns().addAll(coach, trainingDate, trainingType);

        } else {
            // если авторизованный пользователь -- тренер,
            // то он видит такую таблицу:

            ObservableList<ModelWorkouts> list = FXCollections.observableArrayList(dbWorkout.getWorkoutForCoachOnly(currentUser));
            tblWorkout.setItems(list);

            tblWorkout.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

            TableColumn<ModelWorkouts, String> trainingDate = new TableColumn<>("Дата тренировки");
            trainingDate.setCellValueFactory(new PropertyValueFactory<>("trainingDate"));

            TableColumn<ModelWorkouts, String> trainingStructure = new TableColumn<>("Состав тренировки");
            trainingStructure.setCellValueFactory(new PropertyValueFactory<>("nameClient"));

            trainingDate.getStyleClass().add("fontMedium");
            trainingStructure.getStyleClass().add("fontMedium");

            tblWorkout.getSortOrder().add(trainingDate);
            trainingDate.setSortType(TableColumn.SortType.ASCENDING);

            tblWorkout.getColumns().addAll(trainingDate, trainingStructure);


        }


    }

    // отображение информации тренировок все тренеров
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

            coach.getStyleClass().add("fontMedium");
            trainingDate.getStyleClass().add("fontMedium");
            trainingStructure.getStyleClass().add("fontMedium");

            tblWorkout.getColumns().addAll(coach, trainingDate, trainingStructure);

        } else {
            btnAllCouch.setSelected(false);
            btnAllCouch.setText("Отобразить всю информацию");
            initializeTable();
        }
    }

    // удаление выбранной тренировки в таблице
    @FXML
    void doDeleteWorkout() throws ClassNotFoundException, SQLException {
        if (tblWorkout.getSelectionModel().getSelectedItem() != null) {
            dbWorkout.deleteCurrentWorkout(tblWorkout.getSelectionModel().getSelectedItem().getId_workout());
            tblWorkout.getColumns().clear();
            initializeTable();
        }
    }

    // переход на окно всего списка клиентов
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

    // переход на окно планов тренировок
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

    // переход на окно клиента, где составляются запросы
    @FXML
    void goRequest() throws ClassNotFoundException, SQLException, IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("fxml/forClient/do-request.fxml"));

        Scene scene = new Scene(fxmlLoader.load());

        RequestController requestController = fxmlLoader.getController();
        requestController.startPage(currentUser);

        Stage stage = (Stage) btnTrainingPlan.getScene().getWindow();
        stage.setTitle("Новый запрос");

        stage.setResizable(false);

        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        stage.setScene(scene);
        stage.setFullScreen(true);

        stage.show();

    }

    // переход на окно добавления новой тренировки
    @FXML
    void goAddNewWorkout() throws ClassNotFoundException, SQLException, IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("fxml/forCoach/new-workout.fxml"));

        Scene scene = new Scene(fxmlLoader.load());

        NewWorkoutController newWorkoutController = fxmlLoader.getController();
        newWorkoutController.startPage(currentUser);

        Stage stage = (Stage) btnAddNewWorkout.getScene().getWindow();
        stage.setTitle("Новые тренировки");

        stage.setResizable(false);

        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        stage.setScene(scene);
        stage.setFullScreen(true);

        stage.show();

    }

    // переход на окно тренера, где он видит отправленные ему заявки
    @FXML
    void goRequestsForCoach() throws ClassNotFoundException, SQLException, IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(StartApplication.class.getResource("fxml/forCoach/request-for-coach.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Заявки от клиентов");
        stage.setScene(new Scene(root));

        RequestsForCoachController requestsForCoachController = loader.getController();
        requestsForCoachController.startPage(currentUser);

        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(this.btnRequestFromClient.getScene().getWindow());

        stage.showAndWait();

    }

    // переход на окно начала тренировки
    @FXML
    void goStartWorkout() throws ClassNotFoundException, SQLException, IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("fxml/forCoach/start-workout.fxml"));

        Scene scene = new Scene(fxmlLoader.load());

        StartWorkoutController startWorkoutController = fxmlLoader.getController();
        startWorkoutController.startPage(currentUser);

        Stage stage = (Stage) btnAddNewWorkout.getScene().getWindow();
        stage.setTitle("Проведение тренировки");

        stage.setResizable(false);

        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        stage.setScene(scene);
        stage.setFullScreen(true);

        stage.show();
    }

    // переход на окно результатов тренировок
    @FXML
    void goRecords() throws ClassNotFoundException, SQLException, IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(StartApplication.class.getResource("fxml/records-page.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Результаты тренировок");
        stage.setScene(new Scene(root));

        RecordsController resultsController = loader.getController();
        resultsController.startPage(currentUser);

        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(this.btnResult.getScene().getWindow());

        stage.showAndWait();
    }

    // переход на окно авторизации
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

