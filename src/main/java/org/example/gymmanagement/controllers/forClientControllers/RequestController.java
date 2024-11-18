package org.example.gymmanagement.controllers.forClientControllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.gymmanagement.DAOS.*;
import org.example.gymmanagement.StartApplication;
import org.example.gymmanagement.controllers.MainPageController;
import org.example.gymmanagement.interfaces.Controller;
import org.example.gymmanagement.models.ModelExercises;
import org.example.gymmanagement.models.ModelUsers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class RequestController implements Controller, Initializable {

    @FXML
    private Button btnBack;

    @FXML
    private HBox boxExercises;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ListView<ModelExercises> listExercises;

    @FXML
    private ComboBox<String> comboTypeWorkout;

    @FXML
    private ComboBox<ModelExercises> comboExercises;

    @FXML
    private Button btnAddRequest;

    @FXML
    private Label lblError;

    @FXML
    private ComboBox<ModelUsers> comboCoach;

    @FXML
    private Button btnGoSendRequests;

    @FXML
    private ComboBox<Integer> comboTime;

    private ModelUsers currentUser;

    private DBExercises dbExercises;
    private DBUser dbUser;
    private DBRequests dbRequests;
    private DBWorkout dbWorkout;
    private DBGroupCells dbGroupCells;

    private final ContextMenu contextMenu = new ContextMenu();
    private final MenuItem itemDelete = new MenuItem("Удалить упражнение");

    private ArrayList<Integer> hoursWorkout = new ArrayList<>();

    @Override
    public void startPage(ModelUsers currentUser) throws SQLException, ClassNotFoundException {
        this.currentUser = currentUser;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        lblError.setVisible(false);
        lblError.setManaged(false);

        dbExercises = new DBExercises();
        dbUser = new DBUser();
        dbRequests = new DBRequests();
        dbWorkout = new DBWorkout();
        dbGroupCells = new DBGroupCells();

        boxExercises.setVisible(false);
        boxExercises.setManaged(false);

        contextMenu.getItems().addAll(itemDelete);

        btnAddRequest.setVisible(false);
        btnAddRequest.setManaged(false);

        try {
            comboExercises.setItems(FXCollections.observableArrayList(dbExercises.getExercises()));
            comboCoach.setItems(FXCollections.observableArrayList(dbUser.getAllCoach()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        datePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || today.isAfter(date));
            }
        });

        comboTypeWorkout.setItems(FXCollections.observableArrayList("Индивидуальная", "Групповая"));

    }

    @FXML
    void setAvailableTime(ActionEvent event) throws SQLException, ClassNotFoundException {

        if (comboTypeWorkout.getSelectionModel().getSelectedItem() != null &&
                comboTypeWorkout.getSelectionModel().getSelectedItem().equals("Индивидуальная"))
        {
            hoursWorkout.clear();
            for (int i = 10; i < 20; i++) {
                hoursWorkout.add(i);
            }

            if (datePicker.getValue() != null && comboCoach.getSelectionModel().getSelectedItem() != null) {

                hoursWorkout.removeAll(dbWorkout.getHoursWorkoutForClient(
                        comboCoach.getSelectionModel().getSelectedItem().getIdUser(),
                        datePicker.getValue()));
                hoursWorkout.removeAll(dbWorkout.getHoursRequestForClient(
                        comboCoach.getSelectionModel().getSelectedItem().getIdUser(),
                        datePicker.getValue()));
                hoursWorkout.removeAll(dbGroupCells.getGroupHoursRequest());
                comboTime.setItems(FXCollections.observableArrayList(hoursWorkout));

            }
        }

    }

    @FXML
    void showContext() {

        listExercises.setCellFactory(lv -> {
            ListCell<ModelExercises> cell = new ListCell<>() {
                @Override
                protected void updateItem(ModelExercises item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };

            cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                if (event.getButton() == MouseButton.SECONDARY && (!cell.isEmpty())) {
                    contextMenu.show(listExercises, event.getScreenX(), event.getScreenY());
                    itemDelete.setOnAction(_ -> listExercises.getItems().remove(listExercises.getSelectionModel().getSelectedItem()));
                }
            });
            return cell;
        });

    }

    @FXML
    void showExercises() throws SQLException, ClassNotFoundException {

        btnAddRequest.setVisible(true);
        btnAddRequest.setManaged(true);
        comboTime.getItems().clear();

        if (comboTypeWorkout.getSelectionModel().getSelectedItem() != null) {
            if (comboTypeWorkout.getSelectionModel().getSelectedItem().equals("Индивидуальная")) {
                boxExercises.setVisible(true);
                boxExercises.setManaged(true);

                hoursWorkout.clear();
                for (int i = 10; i < 20; i++) {
                    hoursWorkout.add(i);
                }

                if (datePicker.getValue() != null && comboCoach.getSelectionModel().getSelectedItem() != null) {

                    hoursWorkout.removeAll(dbWorkout.getHoursWorkoutForClient(
                            comboCoach.getSelectionModel().getSelectedItem().getIdUser(),
                            datePicker.getValue()));
                    hoursWorkout.removeAll(dbWorkout.getHoursRequestForClient(
                            comboCoach.getSelectionModel().getSelectedItem().getIdUser(),
                            datePicker.getValue()));
                    hoursWorkout.removeAll(dbGroupCells.getGroupHoursRequest());
                    comboTime.setItems(FXCollections.observableArrayList(hoursWorkout));

                }

            } else {
                comboTime.setItems(FXCollections.observableArrayList(dbGroupCells.getGroupHoursRequest()));
                boxExercises.setVisible(false);
                boxExercises.setManaged(false);
            }

        }


    }

    @FXML
    void addExercises() {
        if (comboExercises.getSelectionModel().getSelectedItem() != null) {
            listExercises.getItems().add(comboExercises.getSelectionModel().getSelectedItem());
            comboExercises.getSelectionModel().clearSelection();
        }


    }

    @FXML
    void doAddRequest() throws SQLException, ClassNotFoundException {
        if (datePicker.getValue() == null || comboTime.getSelectionModel().getSelectedItem() == null
                || comboCoach.getSelectionModel().getSelectedItem() == null
                || comboTypeWorkout.getSelectionModel().getSelectedItem() == null)
        {

            lblError.setTextFill(Color.RED);
            lblError.setText("Заполните все поля!");
            lblError.setVisible(true);
            lblError.setManaged(true);

            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                public void run() {
                    lblError.setManaged(false);
                    lblError.setVisible(false);
                }
            };

            timer.schedule(task, 2000);

            return;

        }

        if (comboTypeWorkout.getSelectionModel().getSelectedItem().equals("Индивидуальная")) {
            if (dbRequests.cntRequestFromClient(currentUser.getIdUser(), datePicker.getValue(), comboTime.getSelectionModel().getSelectedItem()) == 1)
            {

                lblError.setTextFill(Color.RED);
                lblError.setText("На данную дату и время уже есть заявка!");
                lblError.setVisible(true);
                lblError.setManaged(true);

                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    public void run() {
                        lblError.setManaged(false);
                        lblError.setVisible(false);
                    }
                };

                timer.schedule(task, 2000);

                return;

            }

        }

        if (comboTypeWorkout.getSelectionModel().getSelectedItem().equals("Групповая")) {

            dbRequests.addNewRequest(currentUser.getIdUser(), comboCoach.getSelectionModel().getSelectedItem().getIdUser(),
                    datePicker.getValue(), comboTime.getSelectionModel().getSelectedItem(),
                    comboTypeWorkout.getSelectionModel().getSelectedItem());

        } else if (comboTypeWorkout.getSelectionModel().getSelectedItem().equals("Индивидуальная")) {

            Integer[] exercisesArr = new Integer[listExercises.getItems().size()];
            int i = 0;
            for (ModelExercises modelExercises : listExercises.getItems()) {
                exercisesArr[i] = modelExercises.getId();
                i++;
            }

            dbRequests.addNewRequest(currentUser.getIdUser(), comboCoach.getSelectionModel().getSelectedItem().getIdUser(),
                    datePicker.getValue(), comboTime.getSelectionModel().getSelectedItem(), comboTypeWorkout.getSelectionModel().getSelectedItem(), exercisesArr);
        }

        datePicker.setValue(null);
        comboTime.getSelectionModel().clearSelection();
        comboTime.getItems().clear();
        comboCoach.getSelectionModel().clearSelection();
        comboTypeWorkout.getSelectionModel().clearSelection();
        listExercises.getItems().clear();

        lblError.setVisible(true);
        lblError.setManaged(true);
        lblError.setTextFill(Color.BLACK);
        lblError.setText("Заявка отправлена!");

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {

                lblError.setManaged(false);
                lblError.setVisible(false);
            }
        };
        timer.schedule(task, 2000);

    }

    @FXML
    void goSendRequests() throws ClassNotFoundException, SQLException, IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(StartApplication.class.getResource("fxml/forClient/show-send-request.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Отправленные заявки");
        stage.setScene(new Scene(root));

        ShowSendRequestController showSendRequestController = loader.getController();
        showSendRequestController.startPage(currentUser);

        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(this.btnGoSendRequests.getScene().getWindow());

        stage.showAndWait();

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
            System.out.println(e.getMessage());
        }
    }
}
