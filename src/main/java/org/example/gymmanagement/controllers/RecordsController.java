package org.example.gymmanagement.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.gymmanagement.DAOS.DBRecords;
import org.example.gymmanagement.interfaces.StartController;
import org.example.gymmanagement.models.ModelRecord;
import org.example.gymmanagement.models.ModelUsers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RecordsController implements StartController, Initializable {


    @FXML
    private ComboBox<String> comboSortBy;

    @FXML
    private ComboBox<String> comboSortItem;

    @FXML
    private TableView<ModelRecord> tblRecords;

    ModelUsers currentUser = new ModelUsers();

    DBRecords dbRecords;

    // метод для передачи данны о текущем пользователе
    // и инициализация всех объектов, зависящих от текущего пользователя
    @Override
    public void startPage(ModelUsers currentUser) throws SQLException, ClassNotFoundException {
        this.currentUser = currentUser;
        initializeTable();
        if (currentUser.getIdRole() == 2) {
            comboSortBy.getItems().add("По клиенту");
        }
    }

    // инициализиция некоторых объектов
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbRecords = new DBRecords();
        tblRecords.setPlaceholder(new Label("Таблица рекордов пуста"));

        comboSortBy.setItems(FXCollections.observableArrayList("По дате", "По упражнению"));
    }

    // инициализация таблицы с результатами трениорвок
    // в соответствии с ролью пользователя
    public void initializeTable() throws SQLException, ClassNotFoundException {

        // инициализация для тренера
        if (currentUser.getIdRole() == 2) {

            ObservableList<ModelRecord> records = FXCollections.observableArrayList(dbRecords.getRecordsForCoachOnly(currentUser.getIdUser()));
            tblRecords.setItems(records);

            TableColumn<ModelRecord, String> dateCol = new TableColumn<>("Дата тренировки");
            dateCol.setCellValueFactory(new PropertyValueFactory<>("training_date"));

            TableColumn<ModelRecord, String> clientCol = new TableColumn<>("Клиент");
            clientCol.setCellValueFactory(new PropertyValueFactory<>("nameClient"));

            TableColumn<ModelRecord, String> exerciseCol = new TableColumn<>("Упражнение");
            exerciseCol.setCellValueFactory(new PropertyValueFactory<>("nameExercise"));

            TableColumn<ModelRecord, String> weightCol = new TableColumn<>("Вес");
            weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));

            TableColumn<ModelRecord, String> repetitionCol = new TableColumn<>("Повторения");
            repetitionCol.setCellValueFactory(new PropertyValueFactory<>("repetitions"));

            dateCol.getStyleClass().add("fontMedium");
            clientCol.getStyleClass().add("fontMedium");
            exerciseCol.getStyleClass().add("fontMedium");
            weightCol.getStyleClass().add("fontMedium");
            repetitionCol.getStyleClass().add("fontMedium");

            tblRecords.getColumns().addAll(dateCol, clientCol, exerciseCol, weightCol, repetitionCol);

            // инициализация для клиента
        } else {

            ObservableList<ModelRecord> records = FXCollections.observableArrayList(dbRecords.getRecordsForClient(currentUser.getIdUser()));
            tblRecords.setItems(records);

            TableColumn<ModelRecord, String> dateCol = new TableColumn<>("Дата тренировки");
            dateCol.setCellValueFactory(new PropertyValueFactory<>("training_date"));

            TableColumn<ModelRecord, String> exerciseCol = new TableColumn<>("Упражнение");
            exerciseCol.setCellValueFactory(new PropertyValueFactory<>("nameExercise"));

            TableColumn<ModelRecord, String> weightCol = new TableColumn<>("Вес");
            weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));

            TableColumn<ModelRecord, String> repetitionCol = new TableColumn<>("Повторения");
            repetitionCol.setCellValueFactory(new PropertyValueFactory<>("repetitions"));

            dateCol.getStyleClass().add("fontMedium");
            exerciseCol.getStyleClass().add("fontMedium");
            weightCol.getStyleClass().add("fontMedium");
            repetitionCol.getStyleClass().add("fontMedium");

            tblRecords.getColumns().addAll(dateCol, exerciseCol, weightCol, repetitionCol);

        }
    }

    // в зависимости от типа сортировки
    // устанавливаются элементы
    @FXML
    void setSortItem() {
        comboSortItem.getItems().clear();
        if (comboSortBy.getSelectionModel().getSelectedItem() != null) {
            for (ModelRecord record : tblRecords.getItems()) {
                if (comboSortBy.getSelectionModel().getSelectedItem().equals("По дате")) {
                    if (!comboSortItem.getItems().contains(record.getTraining_date())) {
                        comboSortItem.getItems().add(record.getTraining_date());
                    }
                }

                if (comboSortBy.getSelectionModel().getSelectedItem().equals("По упражнению")) {
                    if (!comboSortItem.getItems().contains(record.getNameExercise())) {
                        comboSortItem.getItems().add(record.getNameExercise());
                    }

                }
                if (comboSortBy.getSelectionModel().getSelectedItem().equals("По клиенту")) {
                    if (!comboSortItem.getItems().contains(record.getNameClient())) {
                        comboSortItem.getItems().add(record.getNameClient());
                    }
                }
            }
        }
    }

    // сортируем таблицу по критерию
    @FXML
    void setSortedItem() throws SQLException, ClassNotFoundException {
        if (comboSortItem.getSelectionModel().getSelectedItem() != null){
            tblRecords.getItems().clear();
            tblRecords.getColumns().clear();
            initializeTable();
            ArrayList<ModelRecord> records = new ArrayList<>();
            for (ModelRecord record : tblRecords.getItems()) {
                if (comboSortBy.getSelectionModel().getSelectedItem().equals("По дате")) {
                    if (record.getTraining_date().equals(comboSortItem.getSelectionModel().getSelectedItem())) {
                        records.add(record);
                    }
                }

                if (comboSortBy.getSelectionModel().getSelectedItem().equals("По упражнению")) {
                    if (record.getNameExercise().equals(comboSortItem.getSelectionModel().getSelectedItem())) {
                        records.add(record);
                    }
                }

                if (comboSortBy.getSelectionModel().getSelectedItem().equals("По клиенту")) {
                    if (record.getNameClient().equals(comboSortItem.getSelectionModel().getSelectedItem())) {
                        records.add(record);
                    }
                }
            }
            tblRecords.getItems().clear();
            tblRecords.getItems().addAll(records);
        }
    }

    // установка значений сортировки таблицы по умолчанию
    @FXML
    void setDefault() throws ClassNotFoundException, SQLException {
        comboSortItem.getSelectionModel().clearSelection();
        comboSortBy.getSelectionModel().clearSelection();
        tblRecords.getItems().clear();
        tblRecords.getColumns().clear();
        initializeTable();
    }
}
