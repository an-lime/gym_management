package org.example.gymmanagement.models;

import java.util.Objects;

public class ModelRecord {

    private int id_record;
    private int id_workout;
    private String nameClient;
    private int id_client;
    private String nameExercise;
    private int IdExercise;
    private String weight;
    private String repetitions;

    public int getId_record() {
        return id_record;
    }

    public void setId_record(int id_record) {
        this.id_record = id_record;
    }

    public int getId_workout() {
        return id_workout;
    }

    public void setId_workout(int id_workout) {
        this.id_workout = id_workout;
    }

    public String getNameClient() {
        return nameClient;
    }

    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }

    public int getId_client() {
        return id_client;
    }

    public void setId_client(int id_client) {
        this.id_client = id_client;
    }

    public String getNameExercise() {
        return nameExercise;
    }

    public void setNameExercise(String nameExercise) {
        this.nameExercise = nameExercise;
    }

    public int getIdExercise() {
        return IdExercise;
    }

    public void setIdExercise(int idExercise) {
        this.IdExercise = idExercise;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(String repetitions) {
        this.repetitions = repetitions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelRecord that = (ModelRecord) o;
        return id_record == that.id_record && id_workout == that.id_workout && id_client == that.id_client && IdExercise == that.IdExercise && weight == that.weight && repetitions == that.repetitions;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_record, id_workout, id_client, IdExercise, weight, repetitions);
    }
}
