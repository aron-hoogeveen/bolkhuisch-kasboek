package ch.bolkhuis.kasboek.components;

import ch.bolkhuis.kasboek.core.Transaction;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;

/**
 * TransactionTableView is an implementation of the TableView class for Transactions.
 *
 * TODO add option to show only transactions from a specific date span
 * @author Aron Hoogeveen
 */
public class TransactionTableView extends TableView<Transaction> implements MapChangeListener<Integer, Transaction> {
    private final ObservableMap<Integer, Transaction> m_items;

    /**
     * Creates a default TableView control with no content.
     *
     * <p>Refer to the {@link TableView} class documentation for details on the
     * default state of other properties.
     */
    public TransactionTableView() {
        m_items = FXCollections.observableHashMap();

        setEditable(false); // disable editing in this table. Transactions are edited in a specific dialog presented to the user
        m_items.addListener(this);

        initColumns();
        initChildren();
    }

    /**
     * Creates a TableView with the content provided in the items ObservableList.
     * This also sets up an observer such that any changes to the items list
     * will be immediately reflected in the TableView itself.
     *
     * <p>Refer to the {@link TableView} class documentation for details on the
     * default state of other properties.
     *
     * @param m_items The items to insert into the TableView, and the list to watch
     *              for changes (to automatically show in the TableView).
     */
    public TransactionTableView(@NotNull ObservableMap<Integer, Transaction> m_items) {
        if (m_items == null) { throw new NullPointerException(); }
        this.m_items = m_items;

        setEditable(false); // disable editing in this table. Transactions are edited in a specific dialog presented to the user
        m_items.addListener(this);

        initColumns();
        initChildren();
    }

    /**
     * Create and set the columns for this TableView.
     */
    private void initColumns() {
        TableColumn<Transaction, String> dateColumn = new TableColumn<>("Datum");
        TableColumn<Transaction, String> receiptColumn = new TableColumn<>("Bonnetje");
        TableColumn<Transaction, String> debtorColumn = new TableColumn<>("Debtor");
        TableColumn<Transaction, String> creditorColumn = new TableColumn<>("Creditor");
        TableColumn<Transaction, String> amountColumn = new TableColumn<>("Bedrag");
        TableColumn<Transaction, String> descriptionColumn = new TableColumn<>("Beschrijving");

        dateColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getDate().toString()));
        receiptColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getReceiptId())));
        debtorColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getDebtorId()))); // FIXME get actual name
        creditorColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getCreditorId()))); // FIXME get actual name
        amountColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(NumberFormat.getCurrencyInstance().format(
                param.getValue().getAmount()
        )));
        descriptionColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getDescription()));

        getColumns().setAll(
                dateColumn,
                receiptColumn,
                debtorColumn,
                creditorColumn,
                amountColumn,
                descriptionColumn
        );
    }

    /**
     * Initialises the backing {@code items} ObservableList from the values of the backing ObservableMap {@code m_items}.
     * This method is called by all constructors.
     */
    private void initChildren() {
        getItems().setAll(m_items.values()); // clears the ObservableList and then adds all Transactions if any
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
    public void onChanged(Change<? extends Integer, ? extends Transaction> change) {
        // update the backing ObservableList items
        if (change.wasAdded()) {
            getItems().add(change.getValueAdded());
        }
        if (change.wasRemoved()){
            getItems().remove(change.getValueRemoved());
        }
    }
}