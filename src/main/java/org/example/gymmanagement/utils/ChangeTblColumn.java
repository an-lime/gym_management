package org.example.gymmanagement.utils;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.text.Font;
import javafx.util.Callback;
import org.example.gymmanagement.models.ModelWorkouts;

public class ChangeTblColumn {

    public void changeColumn(TableColumn<ModelWorkouts, String> column) {

        column.setCellFactory(new Callback<>() {
            @Override
            public TableCell<ModelWorkouts, String> call(TableColumn<ModelWorkouts, String> param) {
                return new TableCell<ModelWorkouts, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setFont(Font.font("Verdana", 15));
                            setText(item);
                        }
                    }
                };
            }

        });

    }
}
