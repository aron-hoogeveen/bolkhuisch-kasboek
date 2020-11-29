package ch.bolkhuis.kasboek;

import javafx.scene.layout.BorderPane;

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
