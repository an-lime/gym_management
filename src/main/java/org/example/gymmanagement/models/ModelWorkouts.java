package org.example.gymmanagement.models;

import java.util.ArrayList;

public class ModelWorkouts {
    private int id_workout;
    private String coach;
    private String trainingDate;
    private ArrayList<Integer> id_client;
    private String nameClient;
    private String trainingType;
    private ArrayList<Integer> exercises;

    public int getId_workout() {
        return id_workout;
    }

    public void setId_workout(int id_workout) {
        this.id_workout = id_workout;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public String getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(String trainingDate) {
        this.trainingDate = trainingDate;
    }

    public ArrayList<Integer> getId_client() {
        return id_client;
    }

    public void setId_client(ArrayList<Integer> id_client) {
        this.id_client = id_client;
    }

    public String getNameClient() {
        return nameClient;
    }

    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }

    public String getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(String trainingType) {
        this.trainingType = trainingType;
    }

    public ArrayList<Integer> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<Integer> exercises) {
        this.exercises = exercises;
    }
}
