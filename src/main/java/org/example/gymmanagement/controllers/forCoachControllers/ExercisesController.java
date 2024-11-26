package org.example.gymmanagement.controllers.forCoachControllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.example.gymmanagement.DAOS.DBExercises;
import org.example.gymmanagement.interfaces.StartController;
import org.example.gymmanagement.models.ModelExercises;
import org.example.gymmanagement.models.ModelUsers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ExercisesController implements Initializable, StartController {

    @FXML
    private Button btnBack;

    @FXML
    private ListView<ModelExercises> listExercises;

    @FXML
    private TextField textNewExercise;

    private DBExercises dbExercises;

    ContextMenu contextMenu = new ContextMenu();
    MenuItem itemDelete = new MenuItem("Удалить упражнение");

    ModelExercises modelExercises = new ModelExercises();


    @Override
    public void startPage(ModelUsers currentUser) throws SQLException, ClassNotFoundException {
        listExercises.setItems(FXCollections.observableArrayList(dbExercises.getExercises()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        contextMenu.getItems().addAll(itemDelete);
        dbExercises = new DBExercises();
    }

    // добавление нового упражнения в базу
    @FXML
    void addNewExercise() throws ClassNotFoundException, SQLException {
        if (!textNewExercise.getText().trim().isEmpty()) {
            dbExercises.addNewExercise(textNewExercise.getText());
            textNewExercise.clear();
            listExercises.setItems(FXCollections.observableArrayList(dbExercises.getExercises()));
        }
    }

    // удаление упражнения из базы
    @FXML
    void deleteExercise() {

        listExercises.setCellFactory(_ -> {
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
                    itemDelete.setOnAction(_ -> {
                        modelExercises = listExercises.getSelectionModel().getSelectedItem();
                        if (modelExercises != null) {
                            try {

                                dbExercises.deleteExercise(modelExercises.getId());
                                listExercises.getItems().clear();
                                listExercises.setItems(FXCollections.observableArrayList(dbExercises.getExercises()));

                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    });
                }
            });
            return cell;
        });
    }
}
