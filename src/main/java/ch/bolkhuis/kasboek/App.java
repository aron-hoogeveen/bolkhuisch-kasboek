package ch.bolkhuis.kasboek;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {
    public static final double INITIAL_WIDTH = 1280;
    public static final double INITIAL_HEIGHT = 720;
    public static final double MIN_WIDTH = 848;
    public static final double MIN_HEIGHT = 480;

    private Stage primaryStage;
    private Scene splashScene;
    private Scene applicationScene;

    /**
     * The application initialization method. This method is called immediately
     * after the Application class is loaded and constructed. An application may
     * override this method to perform initialization prior to the actual starting
     * of the application.
     *
     * <p>
     * The implementation of this method provided by the Application class does nothing.
     * </p>
     *
     * <p>
     * NOTE: This method is not called on the JavaFX Application Thread. An
     * application must not construct a Scene or a Stage in this
     * method.
     * An application may construct other JavaFX objects in this method.
     * </p>
     *
     * @throws Exception if something goes wrong
     */
    @Override
    public void init() throws Exception {
        // Load the splash screen image already and pass it to the SplashSceneRoot constructor
        // The width is determined from the
        Image splashLogo = new Image("BolkhuischLogo.png", 240, 240, true, true);

        // Create the scenes as soon as the Application Thread starts up.
        Platform.runLater(() -> {
            splashScene = new Scene(new SplashSceneRoot(this, splashLogo)); // Splash screen should as small as possible
            applicationScene = new Scene(new Group(), INITIAL_WIDTH, INITIAL_HEIGHT); // placeholder

            // Set the stylesheets
            splashScene.getStylesheets().addAll(
                    "ch.bolkhuis.kasboek.Styles.css",
                    "ch.bolkhuis.kasboek.splash.css");
            applicationScene.getStylesheets().add("ch.bolkhuis.kasboek.Styles.css");
        });
    }

    @Override
    public void start(Stage stage) {
        // Do not close the Application when the last showing window is hidden
        // However, Platform.exit() has to be called EXPLICITLY for the application to exit
        Platform.setImplicitExit(false);

        primaryStage = stage;
        stage.setOnCloseRequest(event -> {
            Platform.exit(); // FIXME change this when the applicationScene is set
        });
        stage.setScene(splashScene);
        stage.setTitle("Huisch Kasboek");
        stage.sizeToScene();
        // Splash screen should not be resizable.
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public Scene getSplashScene() {
        return splashScene;
    }

    public void setApplicationScene(Scene scene) { applicationScene = scene; }
    public Scene getApplicationScene() {
        return applicationScene;
    }
}