package ch.bolkhuis.kasboek.components;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;

/**
 * TreeTableRootItem is a subclass of TreeItem that always returns falls for isLeaf().
 *
 * @param <T> the type of the TreeItem
 */
public class TreeTableRootItem<T> extends TreeItem<T> {
    /**
     * Creates an empty TreeItem.
     */
    public TreeTableRootItem() {
    }

    /**
     * Creates a TreeItem with the value property set to the provided object.
     *
     * @param value The object to be stored as the value of this TreeItem.
     */
    public TreeTableRootItem(T value) {
        super(value);
    }

    /**
     * Creates a TreeItem with the value property set to the provided object, and
     * the graphic set to the provided Node.
     *
     * @param value   The object to be stored as the value of this TreeItem.
     * @param graphic The Node to show in the TreeView next to this TreeItem.
     */
    public TreeTableRootItem(T value, Node graphic) {
        super(value, graphic);
    }

    /**
     * A TreeItem is a leaf if it has no children. The isLeaf method may of
     * course be overridden by subclasses to support alternate means of defining
     * how a TreeItem may be a leaf, but the general premise is the same: a
     * leaf can not be expanded by the user, and as such will not show a
     * disclosure node or respond to expansion requests.
     *
     * @return true if this TreeItem has no children
     */
    @Override
    public boolean isLeaf() {
        return false;
    }
}
