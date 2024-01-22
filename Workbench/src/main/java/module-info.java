module org.example.workbench {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.workbench to javafx.fxml;
    exports org.example.workbench;
}