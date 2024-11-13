module org.example.courseproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;


    opens org.example.gymmanagement to javafx.fxml;
    exports org.example.gymmanagement;
    exports org.example.gymmanagement.controllers;
    exports org.example.gymmanagement.models;
    opens org.example.gymmanagement.controllers to javafx.fxml;
    exports org.example.gymmanagement.controllers.forCoachControllers;
    opens org.example.gymmanagement.controllers.forCoachControllers to javafx.fxml;
    opens org.example.gymmanagement.controllers.forClientControllers to javafx.fxml;
}