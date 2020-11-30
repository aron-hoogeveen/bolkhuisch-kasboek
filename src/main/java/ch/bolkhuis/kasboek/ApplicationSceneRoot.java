package ch.bolkhuis.kasboek;

import javafx.scene.layout.BorderPane;

/**
 * ApplicationSceneRoot is the root for the main Scene of the Application class. The root presents the user with an
 * overview of a single HuischLedger.
 *
 * The HuischLedger is loaded from a File.
 *
 * @author Aron Hoogeveen
 */
public class ApplicationSceneRoot extends BorderPane {
    /**
     * Default constructor.
     */
    public ApplicationSceneRoot() {
        initAppearance();
    }

    private void initAppearance() {
        setMinSize(App.MIN_WIDTH, App.MIN_HEIGHT);
        setPrefSize(App.INITIAL_WIDTH, App.INITIAL_HEIGHT);
    }
}
