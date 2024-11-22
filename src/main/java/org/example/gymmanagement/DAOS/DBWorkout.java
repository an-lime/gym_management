package org.example.gymmanagement.DAOS;

import javafx.scene.control.TableView;
import org.example.gymmanagement.models.ModelRecord;
import org.example.gymmanagement.models.ModelUsers;
import org.example.gymmanagement.models.ModelWorkouts;

import java.sql.*;
import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class DBWorkout {
    private final String HOST = "localhost";
    private final String PORT = "5432";
    private final String DB_NAME = "gymManagement";
    private final String LOGIN = "postgres";
    private final String PASS = "root";

    public List<ModelWorkouts> getWorkoutForClient(ModelUsers user) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");

        List<ModelWorkouts> modelWorkoutsList = new ArrayList<>();
        DBUser dbUser = new DBUser();
        String sql = "SELECT * FROM workouts WHERE ? = any (id_clients);";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, user.getIdUser());
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                ModelWorkouts workout = new ModelWorkouts();

                workout.setId_workout(res.getInt("id_workout"));
                workout.setCoach(dbUser.getUserFio(res.getInt("id_user_coach")));
                workout.setTrainingDate(res.getString("training_date"));
                workout.setId_client(getList(res, "id_clients"));
                workout.setTrainingType((getList(res, "id_clients").size() == 1) ? "Индивидуальная" : "Групповая");
                workout.setExercises(getList(res, "exercises"));

                modelWorkoutsList.add(workout);

            }
            return modelWorkoutsList;
        }
    }

    public List<ModelWorkouts> getWorkoutForCoachOnly(ModelUsers user) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");

        List<ModelWorkouts> modelWorkoutsList = new ArrayList<ModelWorkouts>();
        DBUser dbUser = new DBUser();
        String sql = "select * from workouts where id_user_coach = ?;";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, user.getIdUser());
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                ModelWorkouts workout = new ModelWorkouts();

                workout.setId_workout(res.getInt("id_workout"));
                workout.setTrainingDate(res.getString("training_date"));
                workout.setId_client(getList(res, "id_clients"));
                workout.setNameClient(dbUser.getUserFioOnWorkout(res.getArray("id_clients")));
                workout.setExercises(getList(res, "exercises"));

                modelWorkoutsList.add(workout);

            }
            return modelWorkoutsList;
        }
    }

    public List<ModelWorkouts> getCurrentWorkoutForCoachOnly(int id_workout) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");

        List<ModelWorkouts> modelWorkoutsList = new ArrayList<ModelWorkouts>();
        DBUser dbUser = new DBUser();
        String sql = "select * from workouts where id_workout = ?;";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, id_workout);
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                ModelWorkouts workout = new ModelWorkouts();

                workout.setId_workout(res.getInt("id_workout"));
                workout.setTrainingDate(res.getString("training_date"));
                workout.setId_client(getList(res, "id_clients"));
                workout.setNameClient(dbUser.getUserFioOnWorkout(res.getArray("id_clients")));
                workout.setExercises(getList(res, "exercises"));

                modelWorkoutsList.add(workout);

            }
            return modelWorkoutsList;
        }
    }

    public List<ModelWorkouts> getWorkoutForCoachAll() throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");

        List<ModelWorkouts> modelWorkoutsList = new ArrayList<ModelWorkouts>();
        DBUser dbUser = new DBUser();
        String sql = "select * from workouts;";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                ModelWorkouts workout = new ModelWorkouts();

                workout.setId_workout(res.getInt("id_workout"));
                workout.setCoach(dbUser.getUserFio(res.getInt("id_user_coach")));
                workout.setTrainingDate(res.getString("training_date"));
                workout.setId_client(getList(res, "id_clients"));
                workout.setNameClient(dbUser.getUserFioOnWorkout(res.getArray("id_clients")));
                workout.setExercises(getList(res, "exercises"));

                modelWorkoutsList.add(workout);

            }
            return modelWorkoutsList;
        }
    }

    public List<ModelWorkouts> getWorkoutNeedPlan(ModelUsers user) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");

        List<ModelWorkouts> modelWorkoutsList = new ArrayList<ModelWorkouts>();
        DBUser dbUser = new DBUser();
        String sql = "select * from workouts where id_user_coach = ? and training_date > CURRENT_TIMESTAMP(0)\n" + "and id_workout not in (select id_workout from training_plan);";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, user.getIdUser());
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                ModelWorkouts workout = new ModelWorkouts();

                workout.setId_workout(res.getInt("id_workout"));
                workout.setTrainingDate(res.getString("training_date"));
                workout.setId_client(getList(res, "id_clients"));
                workout.setNameClient(dbUser.getUserFioOnWorkout(res.getArray("id_clients")));
                workout.setExercises(getList(res, "exercises"));

                modelWorkoutsList.add(workout);

            }
            return modelWorkoutsList;
        }
    }

    public List<ModelWorkouts> getWorkoutHavePlan(ModelUsers user) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");

        List<ModelWorkouts> modelWorkoutsList = new ArrayList<ModelWorkouts>();
        DBUser dbUser = new DBUser();
        String sql = "select * from workouts where id_user_coach = ? \n" + "and id_workout in (select id_workout from training_plan);";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, user.getIdUser());
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                ModelWorkouts workout = new ModelWorkouts();

                workout.setId_workout(res.getInt("id_workout"));
                workout.setTrainingDate(res.getString("training_date"));
                workout.setId_client(getList(res, "id_clients"));
                workout.setNameClient(dbUser.getUserFioOnWorkout(res.getArray("id_clients")));
                workout.setExercises(getList(res, "exercises"));

                modelWorkoutsList.add(workout);

            }
            return modelWorkoutsList;
        }
    }

    public ArrayList<Integer> getHoursWorkoutForCoach(int id, LocalDate date) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");
        String sql = "SELECT EXTRACT(HOUR FROM training_date) AS hour FROM workouts where id_user_coach = ? and training_date::date = ?";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            Date sqlDate = Date.valueOf(date);

            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, id);
            statement.setDate(2, sqlDate);
            ResultSet res = statement.executeQuery();
            ArrayList<Integer> hoursWorkout = new ArrayList<>();

            while (res.next()) {
                hoursWorkout.add(res.getInt("hour"));
            }
            return hoursWorkout;
        }


    }

    public ArrayList<Integer> getHoursRequestForCoach(int id, LocalDate date) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");
        String sql = "SELECT EXTRACT(HOUR FROM training_date_request) AS hour FROM requests where id_user_coach = ? and training_date_request::date = ?";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            Date sqlDate = Date.valueOf(date);

            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, id);
            statement.setDate(2, sqlDate);
            ResultSet res = statement.executeQuery();
            ArrayList<Integer> hoursWorkout = new ArrayList<>();

            while (res.next()) {
                hoursWorkout.add(res.getInt("hour"));
            }
            return hoursWorkout;
        }


    }

    public ArrayList<Integer> getHoursWorkoutForClient(int id, LocalDate date) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");
        String sql = "SELECT EXTRACT(HOUR FROM training_date) AS hour FROM workouts where id_user_coach = ? and training_date::date = ?";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            Date sqlDate = Date.valueOf(date);

            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, id);
            statement.setDate(2, sqlDate);
            ResultSet res = statement.executeQuery();
            ArrayList<Integer> hoursWorkout = new ArrayList<>();

            while (res.next()) {
                hoursWorkout.add(res.getInt("hour"));
            }
            return hoursWorkout;
        }


    }

    public ArrayList<Integer> getHoursRequestForClient(int id, LocalDate date) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");
        String sql = "SELECT EXTRACT(HOUR FROM training_date_request) AS hour FROM requests where id_user_coach = ? and training_date_request::date = ?";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            Date sqlDate = Date.valueOf(date);

            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, id);
            statement.setDate(2, sqlDate);
            ResultSet res = statement.executeQuery();
            ArrayList<Integer> hoursWorkout = new ArrayList<>();

            while (res.next()) {
                hoursWorkout.add(res.getInt("hour"));
            }
            return hoursWorkout;
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

    public void addNewWorkout(int idCoach, LocalDate date, Integer time, Integer[] clientsArr) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");
        String sql = "insert into workouts values (default, ?, ?, ?)";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {

            LocalDateTime dateTime = date.atStartOfDay();
            dateTime = dateTime.plusHours(time);
            Timestamp timestamp = Timestamp.valueOf(dateTime);

            Array sqlArr = dbConn.createArrayOf("int", clientsArr);

            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, idCoach);
            statement.setTimestamp(2, timestamp);
            statement.setArray(3, sqlArr);
            statement.executeUpdate();
        }
    }

    public void addNewWorkout(int idCoach, Timestamp date, Integer[] clientsArr) throws SQLException, ClassNotFoundException, ParseException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");
        String sql = "insert into workouts values (default, ?, ?, ?)";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {

            Array sqlArr = dbConn.createArrayOf("int", clientsArr);

            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, idCoach);
            statement.setTimestamp(2, date);
            statement.setArray(3, sqlArr);
            statement.executeUpdate();
        }
    }

    public void updateWorkout(int idCoach, LocalDate date, Integer time, Integer[] clientsArr) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");
        String sql = "update workouts set id_clients = ? where id_user_coach = ? and training_date = ?";

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {

            LocalDateTime dateTime = date.atStartOfDay();
            dateTime = dateTime.plusHours(time);
            Timestamp timestamp = Timestamp.valueOf(dateTime);

            Array sqlArr = dbConn.createArrayOf("int", clientsArr);

            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setArray(1, sqlArr);
            statement.setInt(2, idCoach);
            statement.setTimestamp(3, timestamp);
            statement.executeUpdate();
        }
    }

    public Array getClientInCurrentWorkout(int id, LocalDate date, Integer time) throws SQLException, ClassNotFoundException {
        String sql = "select id_clients from workouts where id_user_coach = ? and training_date = ?;";
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");

        LocalDateTime dateTime = date.atStartOfDay();
        dateTime = dateTime.plusHours(time);
        Timestamp timestamp = Timestamp.valueOf(dateTime);

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, id);
            statement.setTimestamp(2, timestamp);
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                return res.getArray("id_clients");
            }
        }
        return null;
    }

    public Array getClientInCurrentWorkout(int idWorkout) throws SQLException, ClassNotFoundException {
        String sql = "select id_clients from workouts where id_workout = ?;";
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, idWorkout);
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                return res.getArray("id_clients");
            }
        }
        return null;
    }

    public int getExistWorkout(int idRequest) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");
        DBRequests dbRequests = new DBRequests();
        String sql = "select * from workouts where training_date = (\n" + "select training_date_request from requests wh" + "ere id_request = ?\n" + ");";
        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, idRequest);
            ResultSet res = statement.executeQuery();
            while (res.next()) {
                return 1;
            }
            return 0;
        }

    }

    public void updateArray(int idClient, int idCoach, Timestamp date) throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("org.postgresql.Driver");
        String sql = "UPDATE workouts SET id_clients = id_clients || ? WHERE id_user_coach = ? and training_date = ?;";
        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, idClient);
            statement.setInt(2, idCoach);
            statement.setTimestamp(3, date);
            statement.executeUpdate();
        }

    }

    public void deleteAllWorkout() throws ClassNotFoundException, SQLException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("org.postgresql.Driver");
        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            CallableStatement callableStatement = dbConn.prepareCall("call deleteWorkout()");
            callableStatement.execute();
            callableStatement.close();
        }
    }

    public void deleteCurrentWorkout(int idWorkout) throws ClassNotFoundException, SQLException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("org.postgresql.Driver");
        String sql = "delete from workouts where id_workout = ?;";
        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, idWorkout);
            statement.executeUpdate();
        }
    }

    public List<ModelWorkouts> getWorkoutsToMayStart(int idCoach) throws ClassNotFoundException, SQLException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("org.postgresql.Driver");
        String sql = "select * from workouts where id_user_coach = ? and ARRAY_LENGTH(exercises, 1) IS NULL;";
        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, idCoach);
            ResultSet res = statement.executeQuery();
            List<ModelWorkouts> workouts = new ArrayList<>();
            while (res.next()) {
                ModelWorkouts workout = new ModelWorkouts();
                workout.setId_workout(res.getInt("id_workout"));
                workout.setTrainingDate(res.getString("training_date"));
                workouts.add(workout);
            }
            return workouts;
        }
    }

    public void doEndWorkout(TableView<ModelRecord> table) throws ClassNotFoundException, SQLException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("org.postgresql.Driver");
        String sql = "update workouts set exercises = ? where id_workout = ?;";

        ArrayList<Integer> arrIdExercises = new ArrayList<>();
        for (ModelRecord item : table.getItems()) {
            arrIdExercises.add(item.getIdExercise());
        }

        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {

            Array sqlArr = dbConn.createArrayOf("int", arrIdExercises.toArray());

            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setArray(1, sqlArr);
            statement.setInt(2, table.getItems().getFirst().getId_workout());
            statement.executeUpdate();

            DBRecords dbRecords = new DBRecords();
            dbRecords.addRecords(table);

        }

    }

    public String getDateCurrentWorkout(int idWorkout) throws ClassNotFoundException, SQLException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("org.postgresql.Driver");
        String sql = "select training_date from workouts where id_workout = ?;";
        try (Connection dbConn = DriverManager.getConnection(connStr, LOGIN, PASS)) {
            PreparedStatement statement = dbConn.prepareStatement(sql);
            statement.setInt(1, idWorkout);
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                return res.getString("training_date");
            }
            return null;
        }
    }

}