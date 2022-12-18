package com.example.javafxtemplate;

/**
 *  The View class holds the window of the application and the fxmlPath for the window. This makes it more typesafe when open View and
 *  prevents error with fxml path. We may add additional flag for different view here. For example: isCacheable
 */
public enum View {
    HELLO("hello-view.fxml");

    private final String fxmlPath;

    View(String fxmlPath) {
        this.fxmlPath = fxmlPath;
    }

    public String getFxmlPath() {
        return fxmlPath;
    }
}
