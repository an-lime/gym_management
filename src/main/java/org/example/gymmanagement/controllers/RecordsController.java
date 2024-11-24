package org.example.gymmanagement.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.gymmanagement.DAOS.DBRecords;
import org.example.gymmanagement.interfaces.StartController;
import org.example.gymmanagement.models.ModelRecord;
import org.example.gymmanagement.models.ModelUsers;
import org.example.gymmanagement.utils.ChangeTblColumn;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RecordsController extends ChangeTblColumn implements StartController, Initializable {

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
    }

    // инициализиция некоторых объектов
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbRecords = new DBRecords();
        tblRecords.setPlaceholder(new Label("Таблица рекордов пуста"));
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
            changeColumnRecord(dateCol);

            TableColumn<ModelRecord, String> clientCol = new TableColumn<>("Клиент");
            clientCol.setCellValueFactory(new PropertyValueFactory<>("nameClient"));
            changeColumnRecord(clientCol);

            TableColumn<ModelRecord, String> exerciseCol = new TableColumn<>("Упражнение");
            exerciseCol.setCellValueFactory(new PropertyValueFactory<>("nameExercise"));
            changeColumnRecord(exerciseCol);

            TableColumn<ModelRecord, String> weightCol = new TableColumn<>("Вес");
            weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
            changeColumnRecord(weightCol);

            TableColumn<ModelRecord, String> repetitionCol = new TableColumn<>("Повторения");
            repetitionCol.setCellValueFactory(new PropertyValueFactory<>("repetitions"));
            changeColumnRecord(repetitionCol);

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
            changeColumnRecord(dateCol);

            TableColumn<ModelRecord, String> exerciseCol = new TableColumn<>("Упражнение");
            exerciseCol.setCellValueFactory(new PropertyValueFactory<>("nameExercise"));
            changeColumnRecord(exerciseCol);

            TableColumn<ModelRecord, String> weightCol = new TableColumn<>("Вес");
            weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
            changeColumnRecord(weightCol);

            TableColumn<ModelRecord, String> repetitionCol = new TableColumn<>("Повторения");
            repetitionCol.setCellValueFactory(new PropertyValueFactory<>("repetitions"));
            changeColumnRecord(repetitionCol);

            dateCol.getStyleClass().add("fontMedium");
            exerciseCol.getStyleClass().add("fontMedium");
            weightCol.getStyleClass().add("fontMedium");
            repetitionCol.getStyleClass().add("fontMedium");

            tblRecords.getColumns().addAll(dateCol, exerciseCol, weightCol, repetitionCol);

        }
    }
}
