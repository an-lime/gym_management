package org.example.gymmanagement.DAOS;

import org.example.gymmanagement.models.ModelExercises;

import java.sql.*;
import java.util.*;

// класс обращений к таблице exercises
public class DBExercises {

    private final String HOST = "localhost";
    private final String PORT = "5432";
    private final String DB_NAME = "gymManagement";
    private final String LOGIN = "postgres";
    private final String PASS = "root";

    // получиьт список всех упражнений
    public List<ModelExercises> getExercises() throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");

        List<ModelExercises> exercisesList = new ArrayList<>();
        String sql = "select * from exercises;";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            ResultSet res = statement.executeQuery();
            while (res.next()) {
                ModelExercises modelExercises = new ModelExercises(res.getInt("id_exercise"), res.getString("title"));
                exercisesList.add(modelExercises);
            }
            return exercisesList;
        }

    }

    // добавление нового упражнения
    public void addNewExercise(String exercise) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("org.postgresql.Driver");
        String sql = "insert into exercises VALUES (DEFAULT, ?);";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setString(1, exercise);
            statement.executeUpdate();
        }
    }

    // удаление конкретного упражнения
    public void deleteExercise(int id) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("org.postgresql.Driver");
        String sql = "delete from exercises where exercises.id_exercise = ?";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    // получение упражнений в в такущем плане тренировки
    public ArrayList<ModelExercises> getNameExercisesFromArray(Array idExercise) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");

        String sql = "SELECT id_exercise, title FROM exercises WHERE id_exercise = any (?);";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setArray(1, idExercise);
            ResultSet res = statement.executeQuery();

            ArrayList<ModelExercises> exerciseArr = new ArrayList<>();

            while (res.next()) {
                ModelExercises modelExercises = new ModelExercises(res.getInt("id_exercise"), res.getString("title"));
                exerciseArr.add(modelExercises);
            }

            return exerciseArr;

        }
    }

    // получение названий упражнений из массива id
    public String getExerciseInRequest(Array idExercises) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");

        String sql = "SELECT title FROM exercises WHERE id_exercise = any (?);";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setArray(1, idExercises);
            ResultSet res = statement.executeQuery();

            ArrayList<String> exerciseArr = new ArrayList<>();
            String exerciseStr;

            while (res.next()) {
                exerciseArr.add(res.getString("title"));
            }

            exerciseStr = (toString(exerciseArr));

            return exerciseStr;


        }
    }

    // получение конкретного упражнения
    public String getCurrentExercise(int idExercise) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("org.postgresql.Driver");
        String sql = "SELECT title FROM exercises WHERE id_exercise = ?;";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, idExercise);
            ResultSet res = statement.executeQuery();

            res.next();

            return res.getString("title");
        }
    }

    public String toString(ArrayList<String> arrayList) {
        StringBuilder str = new StringBuilder();
        for (String str1 : arrayList) {
            str.append(str1).append("\n");
        }
        return str.toString();
    }

}
