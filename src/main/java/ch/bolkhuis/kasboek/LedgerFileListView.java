package ch.bolkhuis.kasboek;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class LedgerFileListView extends ListView<RecentLedgerFile> {
    /**
     * Creates a default ListView which will display contents stacked vertically.
     * As no {@link ObservableList} is provided in this constructor, an empty
     * ObservableList is created, meaning that it is legal to directly call
     * {@link #getItems()} if so desired. However, as noted elsewhere, this
     * is not the recommended approach
     * (instead call {@link #setItems(ObservableList)}).
     *
     * <p>Refer to the {@link ListView} class documentation for details on the
     * default state of other properties.
     */
    public LedgerFileListView() {
    }

    /**
     * Creates a default ListView which will stack the contents retrieved from the
     * provided {@link ObservableList} vertically.
     *
     * <p>Attempts to add a listener to the {@link ObservableList}, such that all
     * subsequent changes inside the list will be shown to the user.
     *
     * <p>Refer to the {@link ListView} class documentation for details on the
     * default state of other properties.
     *
     * @param items the list of items
     */
    public LedgerFileListView(ObservableList<RecentLedgerFile> items) {
        super(items);
    }
}
