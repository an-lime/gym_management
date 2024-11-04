package org.example.courseproject.models;

import java.util.ArrayList;

public class ModelWorkouts {
    private int id_workout;
    private String coach;
    private String trainingDate;
    private ArrayList<Integer> id_client;
    private ArrayList<String> nameClientArr = new ArrayList<>();
    private String nameClientStr;
    private String trainingType;
    private ArrayList<Integer> exercises;

    public ModelWorkouts(int id_workout, String coach, String trainingDate, ArrayList<Integer> id_client, String trainingType, ArrayList<Integer> exercises) {
        this.id_workout = id_workout;
        this.coach = coach;
        this.trainingDate = trainingDate;
        this.id_client = id_client;
        this.trainingType = trainingType;
        this.exercises = exercises;
    }

    public int getId_workout() {
        return id_workout;
    }

    public String getCoach() {
        return coach;
    }

    public String getTrainingDate() {
        return trainingDate;
    }

    public ArrayList<Integer> getId_client() {
        return id_client;
    }

    public ArrayList<String> getNameClientArr() {
        return nameClientArr;
    }

    public void setNameClientArr(ArrayList<String> nameClientArr) {
        this.nameClientArr = nameClientArr;
    }

    public void setNameClientStr(String nameClientStr) {
        this.nameClientStr = nameClientStr;
    }

    public String getNameClientStr() {
        return nameClientStr;
    }

    public String getTrainingType() {
        return trainingType;
    }

    public ArrayList<Integer> getExercises() {
        return exercises;
    }
}
