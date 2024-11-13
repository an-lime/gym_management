package org.example.gymmanagement.models;

import java.util.ArrayList;

public class ModelExercises {

    private int id;
    private String exercise;

    public ModelExercises(int id, String exercise) {
        this.id = id;
        this.exercise = exercise;
    }

    public ModelExercises() {}

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
}
