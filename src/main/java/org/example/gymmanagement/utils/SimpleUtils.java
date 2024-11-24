package org.example.gymmanagement.utils;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.example.gymmanagement.models.ModelExercises;

import java.time.LocalDate;

public class SimpleUtils {

    // метод для инициализации ComboBox с типами тренировок
    public void createComboTypeWorkouts(DatePicker datePicker, ComboBox<String> comboTypeWorkout) {
        datePicker.setDayCellFactory(_ -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || today.isAfter(date));
            }
        });

        comboTypeWorkout.setItems(FXCollections.observableArrayList("Индивидуальная", "Групповая"));
    }

    // метод для отображения контекстного меню с кнопкой "удалить"
    public void showContextDelete(ListView<ModelExercises> listExercises, ContextMenu contextMenu, MenuItem itemDelete) {

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
                    itemDelete.setOnAction(_ -> listExercises.getItems().remove(listExercises.getSelectionModel().getSelectedItem()));
                }
            });
            return cell;
        });

    }
}
