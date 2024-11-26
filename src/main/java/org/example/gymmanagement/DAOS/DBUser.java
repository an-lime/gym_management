package org.example.gymmanagement.DAOS;

import org.example.gymmanagement.models.ModelUsers;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DBUser {
    private final String HOST = "localhost";
    private final String PORT = "5432";
    private final String DB_NAME = "gymManagement";
    private final String LOGIN = "postgres";
    private final String PASS = "root";

    // получение пользователя по логину и паролю
    public ModelUsers getCurrentUser(String log, String pass) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM users where login=? and password=?;";
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setString(1, log);
            statement.setString(2, pass);
            ResultSet res = statement.executeQuery();

            while (res.next()) {

                return new ModelUsers(res.getInt("id_user"), res.getString("login"),
                        res.getString("password"), res.getString("fio"),
                        res.getString("telephone"), res.getInt("id_role"));
            }
            return null;
        }
    }

    // существует ли пользователь с таким логином?
    public int getCurrentUserFromLogin(String log) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM users where login=?;";
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setString(1, log);
            ResultSet res = statement.executeQuery();

            while (res.next()) {

                return 1;
            }
            return 0;
        }
    }

    // получение фио пользователя
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

    // получение списка имён пользователей на тренировке
    public String getUserFioOnWorkout(Array idUsers) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");

        String sql = "SELECT fio FROM users WHERE id_user = any (?);";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setArray(1, idUsers);
            ResultSet res = statement.executeQuery();

            ArrayList<String> fioArr = new ArrayList<>();
            String fioStr;

            while (res.next()) {
                fioArr.add(res.getString("fio"));
            }

            fioStr = (toString(fioArr));

            return fioStr;
        }
    }

    // получение списка всех пользователей
    public List<ModelUsers> getAllClient() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM users where id_role = 1;";
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");

        List<ModelUsers> modelUsersList = new ArrayList<>();

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                ModelUsers user = new ModelUsers(res.getInt("id_user"), res.getString("login"), res.getString("password"), res.getString("fio"));
                modelUsersList.add(user);
            }
            return modelUsersList;
        }
    }

    // получение всех тренеров
    public List<ModelUsers> getAllCoach() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM users where id_role = 2;";
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");

        List<ModelUsers> modelUsersList = new ArrayList<>();

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                ModelUsers user = new ModelUsers(res.getInt("id_user"), res.getString("login"), res.getString("password"), res.getString("fio"));
                modelUsersList.add(user);
            }
            return modelUsersList;
        }
    }

    // обновление данных пользователя
    public void updateUsers(int id, String fio, String login, String password) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");

        String sql = "UPDATE users set fio = ?, login = ?, password = ? where id_user = ?;";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setString(1, fio);
            statement.setString(2, login);
            statement.setString(3, password);
            statement.setInt(4, id);
            statement.executeUpdate();
        }
    }

    // получение пользователей, у которых нет записи на текущую тренировку
    // и так же на неё нет заявок
    public List<ModelUsers> getClientWhichNotDoRequestAndNotHasWorkout(LocalDate date, Integer time) throws SQLException, ClassNotFoundException {
        String sql = "select * from users where id_user not in (select id_user_client from requests\n" + "where training_date_request::date = ?) " + "and ARRAY[id_user] != all (select id_clients from workouts where training_date = ?)" + "and id_role = 1;";
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");

        List<ModelUsers> modelUsersList = new ArrayList<>();

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {

            LocalDateTime dateTime = date.atStartOfDay();
            dateTime = dateTime.plusHours(time);
            Timestamp timestamp = Timestamp.valueOf(dateTime);

            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setDate(1, Date.valueOf(date));
            statement.setTimestamp(2, timestamp);
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                ModelUsers user = new ModelUsers(res.getInt("id_user"), res.getString("fio"));
                modelUsersList.add(user);
            }
            return modelUsersList;
        }
    }

    // получение пользователей из массива id
    public List<ModelUsers> getClientModelFromArray(Array idUsers) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");

        String sql = "SELECT * FROM users WHERE id_user = any (?);";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {

            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setArray(1, idUsers);
            ResultSet res = statement.executeQuery();

            ArrayList<ModelUsers> usersList = new ArrayList<>();

            while (res.next()) {
                ModelUsers user = new ModelUsers(res.getInt("id_user"), res.getString("fio"));
                usersList.add(user);
            }

            return usersList;
        }
    }

    // добавление нового пользователя
    public void addNewUser(String login, String password, String fio, String phone) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");

        String sql = "insert into users values (DEFAULT, ?, ?, ?, ?, 1);";
        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setString(1, login);
            statement.setString(2, password);
            statement.setString(3, fio);
            statement.setString(4, phone);
            statement.execute();
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