package org.example.courseproject.DAOS;

import org.example.courseproject.models.ModelUsers;
import org.example.courseproject.models.ModelWorkouts;

import java.sql.*;
import java.util.*;

public class DBWorkout {
    private final String HOST = "localhost";
    private final String PORT = "5432";
    private final String DB_NAME = "courseProject";
    private final String LOGIN = "postgres";
    private final String PASS = "root";

    public List<ModelWorkouts> getWorkout(ModelUsers user) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");

        List<ModelWorkouts> modelWorkoutsList = new ArrayList<ModelWorkouts>();
        DBUser dbUser = new DBUser();
        String sql = "";

        if (user.getIdRole() == 2) {
            sql = "select * from workouts where id_user_coach = ?;";

        } else {
            sql = "SELECT * FROM workouts WHERE ? = any (id_clients);";
        }


        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, user.getIdUser());
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                ModelWorkouts workout = new ModelWorkouts(
                        res.getInt("id_workout"),
                        dbUser.getUserFio(res.getInt("id_user_coach")),
                        res.getString("training_date"),
                        getList(res, "id_clients"),
                        (getList(res, "id_clients").size() == 1) ? "Индивидуальная" : "Групповая",
                        getList(res, "exercises")
                );

                String sqlForFio = "SELECT fio FROM users WHERE id_user = any (?);";
                PreparedStatement statementForFio = dbConn.prepareStatement(sqlForFio);
                statementForFio.setArray(1, res.getArray("id_clients"));
                ResultSet resForFio = statementForFio.executeQuery();

                while (resForFio.next()) {
                    workout.getNameClientArr().add(resForFio.getString("fio"));
                }
                workout.setNameClientStr(toString(workout.getNameClientArr()));

                modelWorkoutsList.add(workout);

            }
            return modelWorkoutsList;
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


    public String toString(ArrayList<String> arrayList) {
        StringBuilder str = new StringBuilder();
        for (String str1 : arrayList) {
            str.append(str1).append("\n");
        }
        return str.toString();
    }
}