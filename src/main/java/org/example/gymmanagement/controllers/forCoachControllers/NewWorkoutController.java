package org.example.gymmanagement.controllers.forCoachControllers;

import javafx.collections.FXCollections;
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
import org.example.gymmanagement.DAOS.DBGroupCells;
import org.example.gymmanagement.DAOS.DBRequests;
import org.example.gymmanagement.DAOS.DBUser;
import org.example.gymmanagement.DAOS.DBWorkout;
import org.example.gymmanagement.StartApplication;
import org.example.gymmanagement.controllers.MainPageController;
import org.example.gymmanagement.controllers.forClientControllers.ShowSendRequestController;
import org.example.gymmanagement.interfaces.Controller;
import org.example.gymmanagement.models.ModelUsers;
import org.example.gymmanagement.models.ModelWorkouts;

import java.io.IO;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class NewWorkoutController implements Controller, Initializable {

    @FXML
    private Button btnBack;

    @FXML
    private ListView<ModelUsers> listAllClient;

    @FXML
    private ListView<ModelUsers> listClientOnWorkout;

    @FXML
    private DatePicker datePicker;

    @FXML
    private HBox boxDateTime;

    @FXML
    private ComboBox<String> comboTypeWorkout;

    @FXML
    private ComboBox<Integer> comboTime;

    @FXML
    private Label pointer;

    @FXML
    private Button btnAddWorkout;

    @FXML
    private HBox boxClient;

    @FXML
    private Label lblError;

    @FXML
    private Label lblWorkoutAdd;

    @FXML
    private Label lblRequests;

    @FXML
    private ListView<ModelUsers> listRequests;

    private ModelUsers currentUser;

    private DBUser dbUser;
    private DBWorkout dbWorkout;
    private DBGroupCells dbGroupCells;
    private DBRequests dbRequests;

    private ArrayList<Integer> hoursWorkout = new ArrayList<>();

    @Override
    public void startPage(ModelUsers currentUser) throws SQLException, ClassNotFoundException {
        this.currentUser = currentUser;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        dbUser = new DBUser();
        dbWorkout = new DBWorkout();
        dbGroupCells = new DBGroupCells();
        dbRequests = new DBRequests();

        boxDateTime.setVisible(false);
        boxDateTime.setManaged(false);

        boxClient.setVisible(false);
        boxClient.setManaged(false);

        lblError.setVisible(false);
        lblError.setManaged(false);
        lblWorkoutAdd.setVisible(false);
        lblRequests.setVisible(false);
        lblRequests.setManaged(false);
        listRequests.setVisible(false);
        listRequests.setManaged(false);

        datePicker.setDayCellFactory(_ -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || today.isAfter(date));
            }
        });

        comboTypeWorkout.setItems(FXCollections.observableArrayList("Индивидуальная", "Групповая"));

    }

    @FXML
    void addClientToWorkout() {

        listAllClient.setCellFactory(lv -> {
            ListCell<ModelUsers> cell = new ListCell<>() {
                @Override
                protected void updateItem(ModelUsers item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };

            cell.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && listAllClient.getSelectionModel().getSelectedItem() != null
                        && ((comboTypeWorkout.getSelectionModel().getSelectedItem().equals("Индивидуальная") && listClientOnWorkout.getItems().isEmpty()
                        || comboTypeWorkout.getSelectionModel().getSelectedItem().equals("Групповая"))) ) {
                    listClientOnWorkout.getItems().add(listAllClient.getSelectionModel().getSelectedItem());
                    listAllClient.getItems().remove(listAllClient.getSelectionModel().getSelectedItem());
                    pointer.setText("--->");
                }
            });
            return cell;
        });
    }

    @FXML
    void deleteClientFromWorkout() {

        listClientOnWorkout.setCellFactory(lv -> {
            ListCell<ModelUsers> cell = new ListCell<>() {
                @Override
                protected void updateItem(ModelUsers item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };

            cell.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && listClientOnWorkout.getSelectionModel().getSelectedItem() != null) {
                    listAllClient.getItems().add(listClientOnWorkout.getSelectionModel().getSelectedItem());
                    listClientOnWorkout.getItems().remove(listClientOnWorkout.getSelectionModel().getSelectedItem());
                    pointer.setText("<---");
                }
            });
            return cell;
        });

    }

    @FXML
    void showDate() throws ClassNotFoundException, SQLException {
        if (comboTypeWorkout.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        listClientOnWorkout.getItems().clear();
        listAllClient.getItems().clear();
//        listAllClient.setItems(FXCollections.observableArrayList(dbUser.getAllClient()));
        boxClient.setVisible(false);
        boxClient.setManaged(false);

        boxDateTime.setVisible(true);
        boxDateTime.setManaged(true);

        comboTime.getItems().clear();

        if (comboTypeWorkout.getSelectionModel().getSelectedItem().equals("Индивидуальная") && datePicker.getValue() != null) {

            hoursWorkout.clear();
            for (int i = 10; i < 20; i++) {
                hoursWorkout.add(i);
            }

            hoursWorkout.removeAll(dbWorkout.getHoursWorkoutForCoach(currentUser.getIdUser(), datePicker.getValue()));
            hoursWorkout.removeAll(dbWorkout.getHoursRequestForCoach(currentUser.getIdUser(), datePicker.getValue()));
            hoursWorkout.removeAll(dbGroupCells.getGroupHoursRequest());
            comboTime.setItems(FXCollections.observableArrayList(hoursWorkout));

        } else if (comboTypeWorkout.getSelectionModel().getSelectedItem().equals("Групповая") && datePicker.getValue() != null) {

            comboTime.setItems(FXCollections.observableList(dbGroupCells.getGroupHoursRequest()));

        }

    }

    @FXML
    void setTime() throws ClassNotFoundException, SQLException {
        lblRequests.setVisible(false);
        lblRequests.setManaged(false);
        listRequests.setVisible(false);
        listRequests.setManaged(false);

        if (comboTypeWorkout.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        if (comboTypeWorkout.getSelectionModel().getSelectedItem().equals("Индивидуальная") && datePicker.getValue() != null) {

            hoursWorkout.clear();
            for (int i = 10; i < 20; i++) {
                hoursWorkout.add(i);
            }

            hoursWorkout.removeAll(dbWorkout.getHoursWorkoutForCoach(currentUser.getIdUser(), datePicker.getValue()));
            hoursWorkout.removeAll(dbWorkout.getHoursRequestForCoach(currentUser.getIdUser(), datePicker.getValue()));
            hoursWorkout.removeAll(dbGroupCells.getGroupHoursRequest());
            comboTime.setItems(FXCollections.observableArrayList(hoursWorkout));

        } else if (comboTypeWorkout.getSelectionModel().getSelectedItem().equals("Групповая") && datePicker.getValue() != null) {

            comboTime.setItems(FXCollections.observableList(dbGroupCells.getGroupHoursRequest()));

        }

        if (datePicker.getValue() != null && comboTime.getSelectionModel().getSelectedItem() != null) {
            listAllClient.getItems().clear();
            listAllClient.setItems(FXCollections.observableArrayList(dbUser.getClientWhichNotDoRequestAndNotHasWorkout(datePicker.getValue(), comboTime.getSelectionModel().getSelectedItem())));
            boxClient.setVisible(true);
            boxClient.setManaged(true);
        }

        if (datePicker.getValue() != null) {

            lblRequests.setVisible(false);
            lblRequests.setManaged(false);

            listRequests.setVisible(false);
            listRequests.setManaged(false);

            listRequests.setItems(FXCollections.observableArrayList(dbRequests.getAllRequestsOnCurrentDate(currentUser.getIdUser(), datePicker.getValue())));
            if (!listRequests.getItems().isEmpty()) {
                lblRequests.setVisible(true);
                lblRequests.setManaged(true);

                listRequests.setVisible(true);
                listRequests.setManaged(true);
            }

        }
    }

    @FXML
    void showBoxClient() throws ClassNotFoundException, SQLException {
        listAllClient.getItems().clear();
        if (datePicker.getValue() != null && comboTime.getSelectionModel().getSelectedItem() != null) {
            listAllClient.setItems(FXCollections.observableArrayList(dbUser.getClientWhichNotDoRequestAndNotHasWorkout(datePicker.getValue(), comboTime.getSelectionModel().getSelectedItem())));

            listClientOnWorkout.setItems(FXCollections.observableArrayList(dbUser.getClientModelFromArray(dbWorkout.getClientInCurrentWorkout(currentUser.getIdUser(), datePicker.getValue(), comboTime.getSelectionModel().getSelectedItem()))));

            for (ModelUsers modelUsers : listClientOnWorkout.getItems()) {
                listAllClient.getItems().remove(modelUsers);
            }

            boxClient.setVisible(true);
            boxClient.setManaged(true);
        }
    }

    @FXML
    void doAddWorkout() throws ClassNotFoundException, SQLException, IOException {

        if (listClientOnWorkout.getItems().isEmpty()) {

            lblError.setText("Заполните список клиентов!");
            lblError.setVisible(true);
            lblError.setManaged(true);
            lblError.setTextFill(Color.RED);

            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                public void run() {
                    lblError.setManaged(false);
                    lblError.setVisible(false);
                }
            };

            timer.schedule(task, 2000);

            return;

        } else if (comboTypeWorkout.getSelectionModel().getSelectedItem() != null && comboTypeWorkout.getSelectionModel().getSelectedItem().equals("Индивидуальная") && listClientOnWorkout.getItems().size() > 1) {
            lblError.setText("Выбрана индивидуальная тренировка!");
            lblError.setVisible(true);
            lblError.setManaged(true);
            lblError.setTextFill(Color.RED);

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

        Integer[] clientsArr = new Integer[listClientOnWorkout.getItems().size()];
        int i = 0;
        for (ModelUsers modelUsers : listClientOnWorkout.getItems()) {
            clientsArr[i] = modelUsers.getIdUser();
            i++;
        }

        if (dbWorkout.getClientInCurrentWorkout(currentUser.getIdUser(), datePicker.getValue(), comboTime.getSelectionModel().getSelectedItem()) == null) {
            dbWorkout.addNewWorkout(currentUser.getIdUser(), datePicker.getValue(), comboTime.getSelectionModel().getSelectedItem(), clientsArr);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(StartApplication.class.getResource("fxml/forCoach/training-plan.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Планы тренировок");
            stage.setScene(new Scene(root));

            TrainingPlanController trainingPlanController = loader.getController();
            trainingPlanController.startPage(currentUser);

            for (ModelWorkouts modelWorkouts : trainingPlanController.getComboTrainingDate().getItems()) {

                LocalDateTime dateTime = datePicker.getValue().atStartOfDay();
                dateTime = dateTime.plusHours(comboTime.getSelectionModel().getSelectedItem());
                Timestamp timestamp = Timestamp.valueOf(dateTime);

                if (modelWorkouts.getTrainingDate().equals(timestamp.toString().substring(0, timestamp.toString().length() - 2))) {
                    trainingPlanController.getComboTrainingDate().getSelectionModel().select(modelWorkouts);
                    trainingPlanController.showExercises();
                }
            }

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(this.btnAddWorkout.getScene().getWindow());

            stage.showAndWait();

        } else {
            dbWorkout.updateWorkout(currentUser.getIdUser(), datePicker.getValue(), comboTime.getSelectionModel().getSelectedItem(), clientsArr);
        }

        listClientOnWorkout.getItems().clear();
        pointer.setText("--->");
        boxClient.setVisible(false);
        comboTime.getSelectionModel().clearSelection();
        comboTypeWorkout.getSelectionModel().clearSelection();
        boxDateTime.setVisible(false);
        datePicker.setValue(null);

        lblWorkoutAdd.setVisible(true);
        lblRequests.setVisible(false);
        lblRequests.setManaged(false);
        listRequests.setVisible(false);
        listRequests.setManaged(false);

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                lblWorkoutAdd.setVisible(false);
            }
        };

        timer.schedule(task, 2000);

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
}
