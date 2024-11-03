package org.example.courseproject.DAOS;

import org.example.courseproject.models.Workouts;

import java.sql.*;
import java.util.*;

public class DBWorkout {
    private final String HOST = "localhost";
    private final String PORT = "5432";
    private final String DB_NAME = "courseProject";
    private final String LOGIN = "postgres";
    private final String PASS = "root";

    public List<Workouts> getWorkout(int idRole) throws SQLException, ClassNotFoundException {
        String sql = "select * from workouts;";
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");

        List<Workouts> workoutsList = new ArrayList<Workouts>();
        DBUser dbUser = new DBUser();

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                Workouts workout = new Workouts(
                        res.getInt("id_workout"),
                        dbUser.getUserFio(res.getInt("id_user_coach")),
                        res.getString("training_date"),
                        getList(res, "id_clients"),
                        (getList(res, "id_clients").size() == 1) ? "Индивидуальная" : "Групповая",
                        getList(res, "exercises")
                );

                workoutsList.add(workout);

            }
            return workoutsList;
        }
    }

    public ArrayList<Integer> getList(ResultSet rs, String column) throws SQLException {
        ArrayList<Integer> newList = new ArrayList<>();
        java.sql.Array sqlList = rs.getArray(column);
        if (sqlList != null) {

            for (Object obj : (Object[]) sqlList.getArray()) {

                Integer arr = (Integer) obj;
                newList.add(arr);

            }
        }
        return newList;
    }
}