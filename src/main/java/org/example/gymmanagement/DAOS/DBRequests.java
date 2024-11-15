package org.example.gymmanagement.DAOS;

import org.example.gymmanagement.models.ModelRequest;
import org.example.gymmanagement.models.ModelUsers;
import org.example.gymmanagement.models.ModelWorkouts;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DBRequests {

    private final String HOST = "localhost";
    private final String PORT = "5432";
    private final String DB_NAME = "gymManagement";
    private final String LOGIN = "postgres";
    private final String PASS = "root";

    public void addNewRequest(int idClient, int idCoach, LocalDate date, Integer time, String type) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("org.postgresql.Driver");
        String sql = "insert into requests values (default, ?, ?, ?, ?);";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {

            LocalDateTime dateTime = date.atStartOfDay();
            dateTime = dateTime.plusHours(time);
            Timestamp timestamp = Timestamp.valueOf(dateTime);
            Date sqlDate = Date.valueOf(date);

            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, idClient);
            statement.setInt(2, idCoach);
            statement.setTimestamp(3, timestamp);
            statement.setString(4, type);
            statement.executeUpdate();
        }
    }

    public void addNewRequest(int idClient, int idCoach, LocalDate date, Integer time, String type, Integer[] exercisesArr) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("org.postgresql.Driver");
        String sql = "insert into requests values (default, ?, ?, ?, ?, ?);";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {

            LocalDateTime dateTime = date.atStartOfDay();
            dateTime = dateTime.plusHours(time);
            Timestamp timestamp = Timestamp.valueOf(dateTime);

            Array sqlArr = dbConn.createArrayOf("int", exercisesArr);

            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, idClient);
            statement.setInt(2, idCoach);
            statement.setTimestamp(3, timestamp);
            statement.setString(4, type);
            statement.setArray(5, sqlArr);
            statement.executeUpdate();
        }
    }

    public int cntRequestFromClient(int idClient, LocalDate date, Integer time) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("org.postgresql.Driver");
        String sql = "Select * from requests where id_user_client = ? and training_date_request = ?;";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {

            LocalDateTime dateTime = date.atTime(time, 0, 0);
            Timestamp timestamp = Timestamp.valueOf(dateTime);

            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, idClient);
            statement.setTimestamp(2, timestamp);
            statement.execute();
            ResultSet rs = statement.getResultSet();

            int cnt = 0;

            while (rs.next()) {
                cnt++;
            }
            return cnt;
        }
    }

    public List<ModelRequest> getRequestCurrentClient(int idClient) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");

        List<ModelRequest> modelRequestList = new ArrayList<ModelRequest>();
        DBExercises dbExercises = new DBExercises();
        DBUser dbUser = new DBUser();
        String sql = "select * from requests where id_user_client = ?;";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, idClient);
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                ModelRequest request = new ModelRequest();

                String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(res.getTimestamp("training_date_request"));

                request.setCoach(dbUser.getUserFio(res.getInt("id_user_coach")));
                request.setDate(formattedDate);
                request.setType(res.getString("type_workout"));
                request.setExercise(dbExercises.getExerciseInRequest(res.getArray("exercises")));

                modelRequestList.add(request);

            }
            return modelRequestList;
        }
    }

}
