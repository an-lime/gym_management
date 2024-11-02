module org.example.courseproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;


    opens org.example.courseproject to javafx.fxml;
    exports org.example.courseproject;
    exports org.example.courseproject.controllers;
    exports org.example.courseproject.models;
    opens org.example.courseproject.controllers to javafx.fxml;
}