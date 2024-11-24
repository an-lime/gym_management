package org.example.gymmanagement.DAOS;

import java.sql.*;
import java.util.ArrayList;

public class DBGroupCells {

    private final String HOST = "localhost";
    private final String PORT = "5432";
    private final String DB_NAME = "gymManagement";
    private final String LOGIN = "postgres";
    private final String PASS = "root";

    // получение времени групповых тренировок
    public ArrayList<Integer> getGroupHoursRequest() throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");
        String sql = "SELECT EXTRACT(HOUR FROM cell) AS hour FROM group_training_cells";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {

            PreparedStatement statement = dbConn.prepareStatement(sql);
            ResultSet res = statement.executeQuery();
            ArrayList<Integer> hoursWorkout = new ArrayList<>();

            while (res.next()) {
                hoursWorkout.add(res.getInt("hour"));
            }
            return hoursWorkout;
        }


    }

}
