package org.example.gymmanagement.DAOS;

import org.example.gymmanagement.models.ModelExercises;

import java.sql.*;
import java.util.*;

public class DBExercises {

    private final String HOST = "localhost";
    private final String PORT = "5432";
    private final String DB_NAME = "gymManagement";
    private final String LOGIN = "postgres";
    private final String PASS = "root";

    public List<ModelExercises> getExercises() throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");

        List<ModelExercises> exercisesList = new ArrayList<>();
        String sql = "select * from exercises;";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            ResultSet res = statement.executeQuery();
            while (res.next()) {
                ModelExercises modelExercises = new ModelExercises(
                        res.getInt("id_exercise"),
                        res.getString("title")
                );
                exercisesList.add(modelExercises);
            }
            return exercisesList;
        }

    }

}