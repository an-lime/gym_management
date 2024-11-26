package org.example.gymmanagement.DAOS;

import org.example.gymmanagement.models.ModelRequest;
import org.example.gymmanagement.models.ModelUsers;

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

    // добавление новой заявки на групповую тренировку
    public void addNewRequest(int idClient, int idCoach, LocalDate date, Integer time, String type) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("org.postgresql.Driver");
        String sql = "insert into requests values (default, ?, ?, ?, ?);";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {

            LocalDateTime dateTime = date.atStartOfDay();
            dateTime = dateTime.plusHours(time);
            Timestamp timestamp = Timestamp.valueOf(dateTime);

            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, idClient);
            statement.setInt(2, idCoach);
            statement.setTimestamp(3, timestamp);
            statement.setString(4, type);
            statement.executeUpdate();
        }
    }

    // добавление новой заявки на индивидуальную тренировку
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

    // получение числа заявок от клиента на текущее время
    public int cntRequestFromClient(int idClient, LocalDate date, Integer time) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("org.postgresql.Driver");
        String sql = "Select * from requests where id_user_client = ? and training_date_request = ?;";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {

            LocalDateTime dateTime = date.atStartOfDay();
            dateTime = dateTime.plusHours(time);
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

    // получение всех запросов от текущего клиента
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

    // получение всех заявок к текущему тренеру
    public List<ModelRequest> getRequestCurrentCoach(int idCoach) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");

        List<ModelRequest> modelRequestList = new ArrayList<ModelRequest>();
        DBExercises dbExercises = new DBExercises();
        DBUser dbUser = new DBUser();
        String sql = "select * from requests where id_user_coach = ?;";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, idCoach);
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                ModelRequest request = new ModelRequest();

                String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(res.getTimestamp("training_date_request"));

                request.setIdRequest(res.getInt("id_request"));
                request.setIdClient(res.getInt("id_user_client"));
                request.setIdCoach(res.getInt("id_user_coach"));
                request.setClient(dbUser.getUserFio(res.getInt("id_user_client")));
                request.setDate(formattedDate);
                request.setType(res.getString("type_workout"));
                request.setExercise(dbExercises.getExerciseInRequest(res.getArray("exercises")));
                request.setExerciseArray(res.getArray("exercises"));

                modelRequestList.add(request);

            }
            return modelRequestList;
        }
    }

    // получение всех заявок на текущкю дату к текущему тренеру
    public List<ModelUsers> getAllRequestsOnCurrentDate(int idCoach, LocalDate date) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("org.postgresql.Driver");
        String sql = "select DISTINCT id_user_client from requests where id_user_coach = ? and training_date_request::date = ?";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {

            Date sqlDate = Date.valueOf(date);
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, idCoach);
            statement.setDate(2, sqlDate);
            ResultSet res = statement.executeQuery();

            ArrayList<ModelUsers> modelUsersArrayList = new ArrayList<>();

            DBUser dbUser = new DBUser();

            while (res.next()) {
                ModelUsers modelUsers = new ModelUsers(res.getInt("id_user_client"), dbUser.getUserFio(res.getInt("id_user_client")));
                modelUsersArrayList.add(modelUsers);
            }
            return modelUsersArrayList;
        }
    }

    // удаление заявки
    public void deleteRequest(int idRequest) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("org.postgresql.Driver");
        String sql = "delete from requests where id_request = ?";
        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, idRequest);
            statement.execute();
        }
    }

    // получение даты заявки
    public Timestamp getRequestDate(int idRequest) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("org.postgresql.Driver");
        String sql = "select training_date_request from requests where id_request = ?";
        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, idRequest);
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                return res.getTimestamp("training_date_request");
            }
            return null;
        }
    }

    // получение списка упражнений в текущей заявке
    public Array getExercises(int idRequest) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("org.postgresql.Driver");
        String sql = "select exercises from requests where id_request = ?";
        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, idRequest);
            ResultSet res = statement.executeQuery();
            while (res.next()) {
                return res.getArray("exercises");
            }
            return null;

        }
    }

    // процедура удаления старых заявок
    public void deleteAllRequest() throws ClassNotFoundException, SQLException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("org.postgresql.Driver");
        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            CallableStatement callableStatement = dbConn.prepareCall("call deleteRequest()");
            callableStatement.execute();
            callableStatement.close();
        }
    }
}


