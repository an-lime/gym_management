package org.example.gymmanagement.controllers.forClientControllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.example.gymmanagement.DAOS.DBRequests;
import org.example.gymmanagement.interfaces.Controller;
import org.example.gymmanagement.models.ModelExercises;
import org.example.gymmanagement.models.ModelRequest;
import org.example.gymmanagement.models.ModelUsers;
import org.example.gymmanagement.utils.ChangeTblColumn;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ShowSendRequestController extends ChangeTblColumn implements Controller, Initializable {

    @FXML
    private TableView<ModelRequest> tblRequests;

    private ModelUsers currentUser;

    private DBRequests dbRequests;

    @Override
    public void startPage(ModelUsers currentUser) throws SQLException, ClassNotFoundException {
        this.currentUser = currentUser;
        initializeTable();
    }

    public void initializeTable() throws SQLException, ClassNotFoundException {
        tblRequests.setItems(FXCollections.observableArrayList(dbRequests.getRequestCurrentClient(currentUser.getIdUser())));
        tblRequests.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        TableColumn<ModelRequest, String> coach = new TableColumn<>("Тренер");
        coach.setCellValueFactory(new PropertyValueFactory<>("coach"));
        coach.getStyleClass().add("cell");
        changeColumnRequest(coach);

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

        tblRequests.getColumns().addAll(coach, date, type, exercise);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbRequests = new DBRequests();
    }
}