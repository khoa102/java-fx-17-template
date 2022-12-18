module com.example.javafxtemplate {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.javafxtemplate to javafx.fxml;
    exports com.example.javafxtemplate;
}