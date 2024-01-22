module org.example.workbench {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.workbench to javafx.fxml;
    exports org.example.workbench;
}