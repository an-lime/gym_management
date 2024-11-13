package org.example.gymmanagement.DAOS;

import java.sql.*;
import java.time.LocalDate;

public class DBRequests {

    private final String HOST = "localhost";
    private final String PORT = "5432";
    private final String DB_NAME = "gymManagement";
    private final String LOGIN = "postgres";
    private final String PASS = "root";

    public void addNewRequest(int idClient, int idCoach, LocalDate date, String type) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("org.postgresql.Driver");
        String sql = "insert into requests values (default, ?, ?, ?, ?);";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {

            Date sqlDate = Date.valueOf(date);

            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, idClient);
            statement.setInt(2, idCoach);
            statement.setDate(3, sqlDate);
            statement.setString(4, type);
            statement.executeUpdate();
        }
    }

    public void addNewRequest(int idClient, int idCoach, LocalDate date, String type, Integer[] exercisesArr) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("org.postgresql.Driver");
        String sql = "insert into requests values (default, ?, ?, ?, ?, ?);";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {

            Date sqlDate = Date.valueOf(date);
            Array sqlArr = dbConn.createArrayOf("int", exercisesArr);

            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, idClient);
            statement.setInt(2, idCoach);
            statement.setDate(3, sqlDate);
            statement.setString(4, type);
            statement.setArray(5, sqlArr);
            statement.executeUpdate();
        }
    }

    public int cntRequestFromClient(int idClient, LocalDate date) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("org.postgresql.Driver");
        String sql = "Select * from requests where id_user_client = ? and training_date_request = ?;";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {

            Date sqlDate = Date.valueOf(date);

            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, idClient);
            statement.setDate(2, sqlDate);
            statement.execute();
            ResultSet rs = statement.getResultSet();

            int cnt = 0;

            while (rs.next()) {
                cnt++;
            }
            return cnt;
        }
    }

}
