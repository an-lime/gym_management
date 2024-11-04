package org.example.courseproject.DAOS;

import org.example.courseproject.models.ModelUsers;

import java.sql.*;

public class DBUser {
    private final String HOST = "localhost";
    private final String PORT = "5432";
    private final String DB_NAME = "courseProject";
    private final String LOGIN = "postgres";
    private final String PASS = "root";

    public ModelUsers getUser(String log, String pass) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM users where login=? and password=?;";
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setString(1, log);
            statement.setString(2, pass);
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                ModelUsers user = new ModelUsers(
                        res.getInt("id_user"),
                        res.getString("login"),
                        res.getString("password"),
                        res.getString("fio"),
                        res.getString("telephone"),
                        res.getInt("id_role"));

                return user;
            }
            return null;
        }
    }

    public String getUserFio(int idUser) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM users where id_user=?;";
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, idUser);
            ResultSet res = statement.executeQuery();

            res.next();

            return res.getString("fio");


        }
    }
}