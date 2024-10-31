module org.example.courseproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.courseproject to javafx.fxml;
    exports org.example.courseproject;
    exports org.example.courseproject.controllers;
    opens org.example.courseproject.controllers to javafx.fxml;
}