package org.example.courseproject.models;

public class ModelUsers {

    private int idUser;
    private String login;
    private String password;
    private String fio;
    private String telephone;
    private int idRole;

    public ModelUsers(int idUser, String login, String password, String fio, String telephone, int idRole) {
        this.idUser = idUser;
        this.login = login;
        this.password = password;
        this.fio = fio;
        this.telephone = telephone;
        this.idRole = idRole;
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
}
