package org.example.gymmanagement.models;

import java.sql.Array;
import java.util.Objects;

public class ModelRequest {
    private int idRequest;
    private int idClient;
    private int idCoach;
    private String client;
    private String coach;
    private String date;
    private String type;
    private String exercise;
    private Array exerciseArray;

    public int getIdRequest() {
        return idRequest;
    }

    public void setIdRequest(int idRequest) {
        this.idRequest = idRequest;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public int getIdCoach() {
        return idCoach;
    }

    public void setIdCoach(int idCoach) {
        this.idCoach = idCoach;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Array getExerciseArray() {
        return exerciseArray;
    }

    public void setExerciseArray(Array exerciseArray) {
        this.exerciseArray = exerciseArray;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelRequest request = (ModelRequest) o;
        return Objects.equals(coach, request.coach) && Objects.equals(date, request.date) && Objects.equals(type, request.type) && Objects.equals(exercise, request.exercise);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coach, date, type, exercise);
    }
}
