package org.example.gymmanagement.models;

import java.sql.Timestamp;
import java.util.Objects;

public class ModelRequest {
    String coach;
    String date;
    String type;
    String exercise;

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
