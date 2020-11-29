package ch.bolkhuis.kasboek;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class LedgerFileCell extends ListCell<RecentLedgerFile> {
    /**
     * The updateItem method should not be called by developers, but it is the
     * best method for developers to override to allow for them to customise the
     * visuals of the cell. To clarify, developers should never call this method
     * in their code (they should leave it up to the UI control, such as the
     * {@link ListView} control) to call this method. However,
     * the purpose of having the updateItem method is so that developers, when
     * specifying custom cell factories (again, like the ListView
     * {@link ListView#cellFactoryProperty() cell factory}),
     * the updateItem method can be overridden to allow for complete customisation
     * of the cell.
     *
     * <p>It is <strong>very important</strong> that subclasses
     * of Cell override the updateItem method properly, as failure to do so will
     * lead to issues such as blank cells or cells with unexpected content
     * appearing within them. Here is an example of how to properly override the
     * updateItem method:
     *
     * <pre>
     * protected void updateItem(T item, boolean empty) {
     *     super.updateItem(item, empty);
     *
     *     if (empty || item == null) {
     *         setText(null);
     *         setGraphic(null);
     *     } else {
     *         setText(item.toString());
     *     }
     * }
     * </pre>
     *
     * <p>Note in this code sample two important points:
     * <ol>
     *     <li>We call the super.updateItem(T, boolean) method. If this is not
     *     done, the item and empty properties are not correctly set, and you are
     *     likely to end up with graphical issues.</li>
     *     <li>We test for the <code>empty</code> condition, and if true, we
     *     set the text and graphic properties to null. If we do not do this,
     *     it is almost guaranteed that end users will see graphical artifacts
     *     in cells unexpectedly.</li>
     * </ol>
     *  @param item The new item for the cell.
     *
     * @param empty whether or not this cell represents data from the list. If it
     *              is empty, then it does not represent any domain data, but is a cell
     */
    @Override
    protected void updateItem(RecentLedgerFile item, boolean empty) {
        super.updateItem(item, empty);

        // use one color as background for all cells
        setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        if (item == null || empty) {
            setGraphic(null);
            setText(null);
        } else {
            // Set the graphic of the ListCell
            VBox root = new VBox();
            Label name = new Label(item.getName());
            Label location = new Label(item.getFile().getPath());
            location.setTextFill(Color.GRAY);
            // TODO set a little graphic of a cross on the top right corner. When clicked the item should be removed from the list
            root.getChildren().addAll(
                    name,
                    location
            );

            setGraphic(root);
        }
    }

    /**
     * Updates whether this cell is in a selected state or not.
     *
     * @param selected whether or not to select this cell.
     */
    @Override
    public void updateSelected(boolean selected) {
    }
}
