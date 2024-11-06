package org.example.gymmanagement.interfaces;

import org.example.gymmanagement.models.ModelUsers;

import java.sql.SQLException;

public interface Controller {
    void startPage(ModelUsers currentUser) throws SQLException, ClassNotFoundException;
}
