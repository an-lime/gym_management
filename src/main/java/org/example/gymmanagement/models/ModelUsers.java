package org.example.gymmanagement.models;

import java.util.ArrayList;
import java.util.Objects;

public class ModelUsers {

    private int idUser;
    private String login;
    private String password;
    private String fio;
    private String telephone;
    private int idRole;

    public ModelUsers() {}

    public ModelUsers(int idUser, String login, String password, String fio, String telephone, int idRole) {
        this.idUser = idUser;
        this.login = login;
        this.password = password;
        this.fio = fio;
        this.telephone = telephone;
        this.idRole = idRole;
    }

    @Override
    public String toString() {
        return this.getFio();
    }

    public ModelUsers(int idUser, String fio) {
        this.idUser = idUser;
        this.fio = fio;
    }

    public ModelUsers(int idUser, String login, String password, String fio) {
        this.idUser = idUser;
        this.login = login;
        this.password = password;
        this.fio = fio;
    }

    public int getIdUser() {
        return idUser;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFio() {
        return fio;
    }

    public String getTelephone() {
        return telephone;
    }

    public int getIdRole() {
        return idRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelUsers that = (ModelUsers) o;
        return idUser == that.idUser && idRole == that.idRole && Objects.equals(login, that.login) && Objects.equals(password, that.password) && Objects.equals(fio, that.fio) && Objects.equals(telephone, that.telephone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, login, password, fio, telephone, idRole);
    }
}
