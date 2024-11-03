package org.example.courseproject.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.courseproject.DAOS.DBWorkout;
import org.example.courseproject.StartApplication;
import org.example.courseproject.models.Users;
import org.example.courseproject.models.Workouts;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {

    @FXML
    private Label lblWelcome;

    @FXML
    private Button btnExit;

    @FXML
    private TableView<Workouts> tblWorkout;

    @FXML
    private TableColumn<Workouts, String> coach;

    @FXML
    private TableColumn<Workouts, String> trainingDate;

    @FXML
    private TableColumn<Workouts, String> trainingType;

    private Users currentUser;

    DBWorkout dbWorkout;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbWorkout = new DBWorkout();
    }

    public void startPage(Users currentUser) throws SQLException, ClassNotFoundException {
        this.currentUser = currentUser;
        lblWelcome.setText(lblWelcome.getText() + currentUser.getFio() + '!');
        initializeTable();
    }

    public void initializeTable() throws SQLException, ClassNotFoundException {
        ObservableList<Workouts> list = FXCollections.observableArrayList(dbWorkout.getWorkout(currentUser.getIdRole()));
        tblWorkout.setItems(list);

        coach.setCellValueFactory(new PropertyValueFactory<>("coach"));
        trainingDate.setCellValueFactory(new PropertyValueFactory<>("trainingDate"));
        trainingType.setCellValueFactory(new PropertyValueFactory<>("trainingType"));
    }

    @FXML
    void doExit(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("fxml/login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) btnExit.getScene().getWindow();
            stage.setTitle("Авторизуйтесь в системе");
            stage.setScene(scene);
            stage.show();

        } catch (
                Exception e) {
            System.out.println(e);
        }
    }
}