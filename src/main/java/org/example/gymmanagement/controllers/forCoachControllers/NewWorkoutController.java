package org.example.gymmanagement.controllers.forCoachControllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.gymmanagement.DAOS.DBUser;
import org.example.gymmanagement.StartApplication;
import org.example.gymmanagement.controllers.MainPageController;
import org.example.gymmanagement.interfaces.Controller;
import org.example.gymmanagement.models.ModelExercises;
import org.example.gymmanagement.models.ModelUsers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class NewWorkoutController implements Controller, Initializable {

    @FXML
    private Button btnBack;

    @FXML
    private ListView<ModelUsers> listAllClient;

    @FXML
    private ListView<ModelUsers> listClientOnWorkout;

    @FXML
    private Label lblClientHasRequest;

    private ModelUsers currentUser;

    private DBUser dbUser;

    @Override
    public void startPage(ModelUsers currentUser) throws SQLException, ClassNotFoundException {
        this.currentUser = currentUser;
        System.out.println(currentUser.getFio());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbUser = new DBUser();

        lblClientHasRequest.setVisible(false);
        lblClientHasRequest.setManaged(false);

        try {
            listAllClient.setItems(FXCollections.observableArrayList(dbUser.getAllClient()));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2
                        && listAllClient.getSelectionModel().getSelectedItem() != null) {
                    listClientOnWorkout.getItems().add(listAllClient.getSelectionModel().getSelectedItem());
                    listAllClient.getItems().remove(listAllClient.getSelectionModel().getSelectedItem());
                }
            });
            return cell;
        });
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
