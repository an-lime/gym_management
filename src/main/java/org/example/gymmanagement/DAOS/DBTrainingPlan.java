package org.example.gymmanagement.DAOS;

import javafx.collections.ObservableList;
import org.example.gymmanagement.models.ModelExercises;

import java.sql.*;

public class DBTrainingPlan {

    private final String HOST = "localhost";
    private final String PORT = "5432";
    private final String DB_NAME = "gymManagement";
    private final String LOGIN = "postgres";
    private final String PASS = "root";

    public void addPlan(int id_workout, int idCoach, Integer[] exercisesArr) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");

        String sql = "insert into training_plan VALUES (DEFAULT, ?, ?, ?);";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {

            Array sqlArr = dbConn.createArrayOf("int", exercisesArr);
            sqlArr.getBaseTypeName();

            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, idCoach);
            statement.setArray(2, sqlArr);
            statement.setInt(3, id_workout);
            statement.execute();
        }

    }
}
