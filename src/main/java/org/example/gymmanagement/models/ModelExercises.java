package org.example.gymmanagement.models;

import java.util.Objects;

public class ModelExercises {

    private int id;
    private String exercise;

    public ModelExercises(int id, String exercise) {
        this.id = id;
        this.exercise = exercise;
    }

    public ModelExercises() {
    }

    public int getId() {
        return id;
    }

    public String getExercise() {
        return exercise;
    }

    @Override
    public String toString() {
        return this.exercise;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelExercises that = (ModelExercises) o;
        return id == that.id && Objects.equals(exercise, that.exercise);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, exercise);
    }
}
