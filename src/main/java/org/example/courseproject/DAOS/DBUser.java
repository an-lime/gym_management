package org.example.courseproject.DAOS;

import org.example.courseproject.models.Users;

import java.sql.*;

public class DBUser {
    private final String HOST = "localhost";
    private final String PORT = "5432";
    private final String DB_NAME = "courseProject";
    private final String LOGIN = "postgres";
    private final String PASS = "root";

    public Users getUser(String log, String pass) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM users where login=? and password=?;";
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME+"?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");

        try(Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)){
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setString(1, log);
            statement.setString(2, pass);
            ResultSet res = statement.executeQuery();

            while(res.next()) {
                Users user = new Users();
                user.setIdUser(res.getInt("id_user"));
                user.setLogin(res.getString("login"));
                user.setPassword(res.getString("password"));
                user.setFio(res.getString("fio"));
                user.setTelephone(res.getString("telephone"));
                user.setIdRole(res.getInt("id_role"));
                return user;
            }
        }
        return null;
    }
}
