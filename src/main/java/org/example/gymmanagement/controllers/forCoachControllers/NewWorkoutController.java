package org.example.gymmanagement.controllers.forCoachControllers;

import javafx.fxml.Initializable;
import org.example.gymmanagement.interfaces.Controller;
import org.example.gymmanagement.models.ModelUsers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class NewWorkoutController implements Controller, Initializable {

    private ModelUsers currentUser;

    @Override
    public void startPage(ModelUsers currentUser) throws SQLException, ClassNotFoundException {
        this.currentUser = currentUser;
        System.out.println(currentUser.getFio());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
