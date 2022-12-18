package com.example.javafxtemplate;

import javafx.application.Application;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        ViewManager.getInstance().showHelloWindow();
    }

    public static void main(String[] args) {
        launch();
    }
}