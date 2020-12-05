package ch.bolkhuis.kasboek;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.util.List;
import java.util.Vector;


/**
 * JavaFX App
 *
 * TODO add an option to import entities from another HuischLedgerFile
 */
public class App extends Application {
    public static final double INITIAL_WIDTH = 1280;
    public static final double INITIAL_HEIGHT = 720;
    public static final double MIN_WIDTH = 848;
    public static final double MIN_HEIGHT = 480;

    public static final String CSS_STYLES = "ch.bolkhuis.kasboek.Styles.css";
    public static final String CSS_SPLASH = "ch.bolkhuis.kasboek.splash.css";

    /**
     * ExtensionFilters to be used for saving and opening HuischLedger files.
     */
    public static final Vector<FileChooser.ExtensionFilter> extensionFilters = new Vector<>(List.of(
            new FileChooser.ExtensionFilter("Huischkasboek Bestanden", "*.hlf"),
            new FileChooser.ExtensionFilter("Alle Bestanden", "*.*")
    ));

    private Stage primaryStage;
    private Image splashLogo;

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
        splashLogo = new Image("BolkhuischLogo.png", 240, 240, true, true);
    }

    @Override
    public void start(Stage stage) {
        // Do not close the Application when the last showing window is hidden
        // However, Platform.exit() has to be called EXPLICITLY for the application to exit
        Platform.setImplicitExit(false);

        primaryStage = stage;

        changeToSplashScene();
    }

    public void changeToSplashScene() {
        primaryStage.hide();
        primaryStage.setTitle("Huisch Kasboek");
        primaryStage.setScene(new Scene(new SplashSceneRoot(this, splashLogo)));
        primaryStage.getScene().getStylesheets().addAll(
                CSS_STYLES,
                CSS_SPLASH
        );
        primaryStage.sizeToScene();
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
        });
        primaryStage.show();
    }

    public void changeToApplicationScene(ApplicationSceneRoot root) {
        primaryStage.hide();
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(App.MIN_WIDTH);
        primaryStage.setMinHeight(App.MIN_HEIGHT);
        primaryStage.setScene(new Scene(root, INITIAL_WIDTH, INITIAL_HEIGHT));
        primaryStage.getScene().getStylesheets().add(App.CSS_STYLES);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

}