package org.example.gymmanagement.DAOS;

import org.example.gymmanagement.models.ModelUsers;
import org.example.gymmanagement.models.ModelWorkouts;

import java.sql.*;
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
        String sql = "select * from workouts where id_user_coach = ? and training_date > CURRENT_TIMESTAMP(0)\n" +
                "and id_workout not in (select id_workout from training_plan);";

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
        String sql = "select * from workouts where id_user_coach = ? \n" +
                "and id_workout in (select id_workout from training_plan);";

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