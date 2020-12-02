package ch.bolkhuis.kasboek.dialog;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.jetbrains.annotations.NotNull;

/**
 * AbstractDialog is the abstract class for implementing a simple construction or edit dialog for class {@code T}.
 *
 * @param <T> the type that this AbstractDialog returns
 */
public abstract class AbstractDialog<T> {
    protected Stage stage;

    private final static BorderStroke errorBorderStroke = new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1));
    private final static BorderStroke correctBorderStroke = new BorderStroke(Color.LIGHTGREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1));

    protected final static Border errorBorder = new Border(errorBorderStroke);
    protected final static Border correctBorder = new Border(correctBorderStroke);

    /**
     * The old T to edit if any, otherwise create a new T.
     */
    protected final T old;

    /**
     * Holds the newly created object of type T if any.
     */
    protected T result;
    /**
     * Indicates whether or not the result is set.
     */
    protected boolean resultAvailable = false;

    /**
     * Creates a new AbstractDialog and initialises its owner.
     *
     * @param owner owner to be used for the stage
     */
    public AbstractDialog(@NotNull Window owner) {
        old = null;

        // Initialise the stage
        stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
    }

    /**
     * Creates a new AbstractDialog and initialises its owner and the old T to load.
     *
     * @param owner owner to be used for the stage
     * @param old T to be edited
     */
    public AbstractDialog(@NotNull Window owner, @NotNull T old) {
        this.old = old;

        // Initialise the stage
        stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
    }

    /**
     * Initialises and sets a root to a new Scene and sets the scene to be used by the stage.
     */
    protected abstract void initAppearance();

    /**
     * Sets behaviours of components that need them. E.g. sets action handlers.
     */
    protected abstract void initBehaviour();

    /**
     * Shows the stage and returns immediately.
     */
    public void show() {
        stage.show();
    }

    /**
     * Shows the stage and blocks until it is closed.
     */
    public void showAndWait() {
        stage.showAndWait();
    }

    public T getResult() { return result; }

    public boolean isResultAvailable() { return  resultAvailable; }
}