package org.example.gymmanagement.interfaces;

import org.example.gymmanagement.models.ModelUsers;

import java.sql.SQLException;

// метод, который вызывается сразу после инициализации кнтроллера
// и передаёт текущего пользователя из предыдущего
// контроллера в новый
public interface StartController {
    void startPage(ModelUsers currentUser) throws SQLException, ClassNotFoundException;
}
