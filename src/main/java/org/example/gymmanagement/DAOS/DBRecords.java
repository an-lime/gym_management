package org.example.gymmanagement.DAOS;

import javafx.scene.control.TableView;
import org.example.gymmanagement.models.ModelRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBRecords {

    private final String HOST = "localhost";
    private final String PORT = "5432";
    private final String DB_NAME = "gymManagement";
    private final String LOGIN = "postgres";
    private final String PASS = "root";

    public void addRecords(TableView<ModelRecord> table) throws ClassNotFoundException, SQLException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("org.postgresql.Driver");

        String sql = "insert into records values (default, ?, ?, ?, ?, ?);";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {

            PreparedStatement statement = dbConn.prepareStatement(sql);
            for (ModelRecord item: table.getItems()) {
                statement.setInt(1, item.getId_workout());
                statement.setInt(2, item.getId_client());
                statement.setInt(3, item.getIdExercise());
                statement.setInt(4, Integer.parseInt(item.getWeight()));
                statement.setInt(5, Integer.parseInt(item.getRepetitions()));
                statement.executeUpdate();
            }

        }


    }

    public List<ModelRecord> getRecordsForCoachOnly(int idCoach) throws ClassNotFoundException, SQLException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("org.postgresql.Driver");
        String sql = "select * from records where id_workout = any (select id_workout from workouts where id_user_coach = ?);";
        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, idCoach);

            DBWorkout dbWorkout = new DBWorkout();
            DBUser dbUser = new DBUser();
            DBExercises dbExercises = new DBExercises();

            ResultSet resultSet = statement.executeQuery();
            List<ModelRecord> records = new ArrayList<>();
            while (resultSet.next()) {
                ModelRecord record = new ModelRecord();
                record.setId_workout(resultSet.getInt("id_workout"));
                record.setTraining_date(dbWorkout.getDateCurrentWorkout(resultSet.getInt("id_workout")));
                record.setNameClient(dbUser.getUserFio(resultSet.getInt("id_client")));
                record.setNameExercise(dbExercises.getCurrentExercise(resultSet.getInt("exercise")));
                record.setWeight(resultSet.getString("weight"));
                record.setRepetitions(resultSet.getString("repetitions"));
                records.add(record);
            }
            return records;
        }
    }

    public List<ModelRecord> getRecordsForClient(int idClient) throws ClassNotFoundException, SQLException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("org.postgresql.Driver");
        String sql = "select * from records where id_client = ?;";
        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, idClient);

            DBWorkout dbWorkout = new DBWorkout();
            DBUser dbUser = new DBUser();
            DBExercises dbExercises = new DBExercises();

            ResultSet resultSet = statement.executeQuery();
            List<ModelRecord> records = new ArrayList<>();
            while (resultSet.next()) {
                ModelRecord record = new ModelRecord();
                record.setId_workout(resultSet.getInt("id_workout"));
                record.setTraining_date(dbWorkout.getDateCurrentWorkout(resultSet.getInt("id_workout")));
                record.setNameClient(dbUser.getUserFio(resultSet.getInt("id_client")));
                record.setNameExercise(dbExercises.getCurrentExercise(resultSet.getInt("exercise")));
                record.setWeight(resultSet.getString("weight"));
                record.setRepetitions(resultSet.getString("repetitions"));
                records.add(record);
            }
            return records;
        }
    }

}
