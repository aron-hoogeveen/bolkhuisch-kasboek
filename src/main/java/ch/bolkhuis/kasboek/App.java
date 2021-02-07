/*
 * Copyright (C) 2020 Aron Hoogeveen
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package ch.bolkhuis.kasboek;

import ch.bolkhuis.kasboek.components.RecentLedgerFile;
import ch.bolkhuis.kasboek.gson.CustomizedGson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.prefs.Preferences;


/**
 * JavaFX App
 *
 * TODO add an option to import entities from another HuischLedgerFile
 */
public class App extends Application {
    /*
     * Constants for file saving
     */
    private static final Path PROGRAM_PATH = Path.of(System.getProperty("user.home"), ".kasboek/");
    private static final Path RECENT_LEDGERS_PATH = Path.of(PROGRAM_PATH.toString(), "recent_ledgers.json");

    /*
     * Constants for widths and heights
     */
    public static final double INITIAL_WIDTH = 1280;
    public static final double INITIAL_HEIGHT = 720;
    public static final double MIN_WIDTH = 848;
    public static final double MIN_HEIGHT = 480;

    /*
     * Constants for stylesheets
     */
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

    private Preferences preferences;
    private ObservableList<RecentLedgerFile> recentLedgerFiles = FXCollections.observableArrayList();
    Type listType = new TypeToken<List<RecentLedgerFile>>() {}.getType();

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
        // Check if the program directory exists, otherwise create it
        if (!Files.isDirectory(PROGRAM_PATH)) {
            try {
                Files.createDirectories(PROGRAM_PATH);
            } catch (IOException e) {
                System.err.println("ERROR. Program directory does not exist, however" +
                        " an exception is thrown while trying to create it. Program " +
                        "will exit.");
                e.printStackTrace(); // TODO remove stackTrace
//                System.exit(1);
            }
        }

        // load the recent ledgers
        try {
            loadRecentLedgers();
        } catch (IOException e) {
            System.err.println("ERROR. Could not load the recent ledgers. Program will exit.");
//            System.exit(1);
        }

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
        changeToApplicationScenePrivate(root, "Bolkhuisch Kasboek");
    }

    public void changeToApplicationScene(ApplicationSceneRoot root, String ledgerName) {
        changeToApplicationScenePrivate(root, "Bolkhuisch Kasboek - " + ledgerName);
    }

    private void changeToApplicationScenePrivate(ApplicationSceneRoot root, String title) {
        primaryStage.hide();
        primaryStage.setTitle(title);
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

    public void addRecentLedgerFile(RecentLedgerFile recentLedgerFile) {
        recentLedgerFiles.add(recentLedgerFile);
        // save the recent files
        saveRecentLedgersAsync();
    }

    public void removeRecentLedgerFile(RecentLedgerFile recentLedgerFile) {
        recentLedgerFiles.remove(recentLedgerFile);
        saveRecentLedgersAsync();
    }

    /**
     * Loads the recent ledgers if they exist
     */
    private void loadRecentLedgers() throws IOException {
        // create the file if it does not yet exist
        if (!Files.exists(RECENT_LEDGERS_PATH)) {
            Files.createFile(RECENT_LEDGERS_PATH);
            return;
        }

        try (BufferedReader br = Files.newBufferedReader(RECENT_LEDGERS_PATH)) {
            recentLedgerFiles = FXCollections.observableList(CustomizedGson.gson.fromJson(br, listType));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveRecentLedgersAsync() {
        new Thread(() -> {
            try {
                String jsonString = CustomizedGson.gson.toJson(recentLedgerFiles, listType);
                try (BufferedWriter bw = Files.newBufferedWriter(RECENT_LEDGERS_PATH)) {
                    bw.write(jsonString);
                }
            } catch (Exception e) {
                System.err.println("Error while saving recentLedgerFiles on different Thread");
                e.printStackTrace();
            }
        }).start();
    }

    public ObservableList<RecentLedgerFile> getRecentLedgerFiles() { return recentLedgerFiles; }

}