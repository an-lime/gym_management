package org.example.gymmanagement.models;

import java.util.ArrayList;
import java.util.Objects;

public class ModelWorkouts {
    private int id_workout;
    private String coach;
    private String trainingDate;
    private ArrayList<Integer> id_client;
    private String nameClient;
    private String trainingType;
    private ArrayList<Integer> exercises;

    @Override
    public String toString() {
        return this.trainingDate;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelWorkouts workouts = (ModelWorkouts) o;
        return id_workout == workouts.id_workout && Objects.equals(coach, workouts.coach) && Objects.equals(trainingDate, workouts.trainingDate) && Objects.equals(id_client, workouts.id_client) && Objects.equals(nameClient, workouts.nameClient) && Objects.equals(trainingType, workouts.trainingType) && Objects.equals(exercises, workouts.exercises);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_workout, coach, trainingDate, id_client, nameClient, trainingType, exercises);
    }
}
