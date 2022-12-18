package com.example.javafxtemplate;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController extends BaseController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}