package com.example.javafxtemplate;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The ViewManager class is responsible for handling View. It handles opening, switching, and closing of View. View can be opened
 * with a new Stage or in the same stage. Only one ViewManager is needed for the whole application so this is a Singleton.
 *
 * The loading of fxml files takes the most time in the process of opening View. So, to optimize that, we cache all the Views
 * inside a HashMap when the view is opened for the first time. This will make the application to store more data so this is a
 * tradeoff between space and time. This step can be further optimized by using a more sophisticated caching algorithm or caching
 * important view only.
 */
public class ViewManager {

    // Cache View to optimize performance
    private final Map<View, Parent> cachedView = new HashMap<>();
    private final List<Stage> activeStages;

    // Needed for Singleton
    private static ViewManager instance;

    private ViewManager() {
        activeStages = new ArrayList<>();
    }

    public static ViewManager getInstance() {
        if (instance == null) {
            instance = new ViewManager();
        }
        return instance;
    }

    public void showHelloWindow() {
        System.out.println("Show Login Window Called");
        initializeStage(View.HELLO);
        Stage latestStage = activeStages.get(activeStages.size() - 1);
        latestStage.setTitle("Hello!");
    }

    public void initializeStage(View view) {
        Parent parent = getView(view);

        if (parent != null) {
            // Create a new Pane to serve as the root node because a Node can only be set as a root of one scene.
            // If the Parent that is loaded from FXML is set as the root node, we cannot reopen that window after the first time. This is because we create a new scene
            // every time and reuse the Parent as root multiple times.
            // Use anchorPane and anchor the parent node to the four side of the root node to make sure the parent node is stretch with the root node
            AnchorPane root = new AnchorPane();
            root.getChildren().add(parent);
            AnchorPane.setTopAnchor(parent, 0d);
            AnchorPane.setBottomAnchor(parent, 0d);
            AnchorPane.setLeftAnchor(parent, 0d);
            AnchorPane.setRightAnchor(parent, 0d);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            activeStages.add(stage);
        }
    }

    public void closeStage(Stage stageToClose) {
        stageToClose.close();
        activeStages.remove(stageToClose);
    }

    private Parent getView(View view) {
        Parent parent;
        try {
            long start = System.currentTimeMillis();
            if (cachedView.containsKey(view)) {
                System.out.println("Loading from cache");
                parent = cachedView.get(view);
            } else {
                System.out.println("Loading from FXML");
                parent = loadViewFromFXML(view);
                cachedView.put(view, parent);
            }
            System.out.println("fmxlLoader load: " + (System.currentTimeMillis() - start));
            return parent;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Parent loadViewFromFXML(View view) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(view.getFxmlPath()));

        // This is used to customize the creation of controller injected by javaFX when defining them with fx:controller attribute inside FXML files
        // Using the controller factory instead of setting the controller directly with fxmlLoader.setController() allows us to keep the fx:controller
        // attribute inside FXML files. This makes it easier for IDE to link fxml with controllers and check for errors
        Callback<Class<?>, Object> controllerFactory = type -> {
            // Any controller that needs custom constructor behavior needs to be defined above this check
            if (BaseController.class.isAssignableFrom(type)) {
                // A default behavior for controllerFactory for all classes extends from base controller.
                try {
//                    return type.getDeclaredConstructor(EmailManager.class, ViewManager.class, String.class).newInstance(emailManager, this, view.getFxmlPath());
                    return type.getDeclaredConstructor().newInstance();
                } catch (Exception exc) {
                    exc.printStackTrace();
                    throw new RuntimeException(exc); // fatal, just bail...
                }
            } else {
                // default behavior for controllerFactory:
                try {
                    return type.getDeclaredConstructor().newInstance();
                } catch (Exception exc) {
                    exc.printStackTrace();
                    throw new RuntimeException(exc); // fatal, just bail...
                }
            }
        };

        fxmlLoader.setControllerFactory(controllerFactory);
        return fxmlLoader.load();
    }
}
