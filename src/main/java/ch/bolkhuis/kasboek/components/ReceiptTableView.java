package ch.bolkhuis.kasboek.components;

import ch.bolkhuis.kasboek.core.Receipt;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.jetbrains.annotations.NotNull;

public class ReceiptTableView extends TableView<Receipt> implements MapChangeListener<Integer, Receipt> {
    private final ObservableMap<Integer, Receipt> m_items;

    /**
     * Creates a default TableView control with no content.
     *
     * <p>Refer to the {@link TableView} class documentation for details on the
     * default state of other properties.
     */
    public ReceiptTableView() {
        m_items = FXCollections.observableHashMap();

        setEditable(false); // disable editing in this table. Transactions are edited in a specific dialog presented to the user
        m_items.addListener(this);

        initColumns();
        initChildren();
    }

    /**
     * Creates a default TableView control with content.
     *
     * <p>Refer to the {@link TableView} class documentation for details on the
     * default state of other properties.
     *
     * @param items initial ObservableMap to be used as backing map
     */
    public ReceiptTableView(@NotNull ObservableMap<Integer, Receipt> items) {
        if (items == null) { throw new NullPointerException(); }

        m_items = items;

        setEditable(false); // disable editing in this table. Transactions are edited in a specific dialog presented to the user
        m_items.addListener(this);

        initColumns();
        initChildren();
    }

    /**
     * Create and set the columns for this TableView.
     */
    private void initColumns() {
        TableColumn<Receipt, String> dateColumn = new TableColumn<>("Datum");
        TableColumn<Receipt, String> payerColumn = new TableColumn<>("Betaald Door");
        TableColumn<Receipt, String> nameColumn = new TableColumn<>("Beschrijving");

        dateColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getDate().toString()));
        payerColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getPayer()))); // TODO show actual name
        nameColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getName()));

        getColumns().setAll(
                dateColumn,
                payerColumn,
                nameColumn
        );
    }

    /**
     * Initialises the backing {@code items} ObservableList from the values of the backing ObservableMap {@code m_items}.
     * This method is called by all constructors.
     */
    private void initChildren() {
        getItems().setAll(m_items.values()); // clears the ObservableList and then adds all Receipts if any
    }

    /**
     * Called after a change has been made to an ObservableMap.
     * This method is called on every elementary change (put/remove) once.
     * This means, complex changes like keySet().removeAll(Collection) or clear()
     * may result in more than one call of onChanged method.
     *
     * @param change the change that was made
     */
    @Override
    public void onChanged(Change<? extends Integer, ? extends Receipt> change) {
        // update the backing ObservableList items
        if (change.wasAdded()) {
            getItems().add(change.getValueAdded());
        }
        if (change.wasRemoved()){
            getItems().remove(change.getValueRemoved());
        }
    }
}
