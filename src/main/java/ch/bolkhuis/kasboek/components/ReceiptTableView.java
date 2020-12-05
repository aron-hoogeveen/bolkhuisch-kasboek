package ch.bolkhuis.kasboek.components;

import ch.bolkhuis.kasboek.ApplicationSceneRoot;
import ch.bolkhuis.kasboek.core.AccountingEntity;
import ch.bolkhuis.kasboek.core.HuischLedger;
import ch.bolkhuis.kasboek.core.Receipt;
import ch.bolkhuis.kasboek.dialog.ReceiptDialog;
import ch.bolkhuis.kasboek.dialog.TransactionDialog;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import org.jetbrains.annotations.NotNull;

public class ReceiptTableView extends TableView<Receipt> implements MapChangeListener<Integer, Receipt> {
    private final ObservableMap<Integer, Receipt> m_items;
    private final ObservableMap<Integer, AccountingEntity> m_entities;
    private final HuischLedger huischLedger;

    /**
     * Creates a default TableView control with no content.
     *
     * <p>Refer to the {@link TableView} class documentation for details on the
     * default state of other properties.
     */
    public ReceiptTableView(
            @NotNull HuischLedger huischLedger,
            @NotNull ObservableMap<Integer, AccountingEntity> m_entities
    ) {
        if (huischLedger == null) { throw new NullPointerException(); }
        if (m_entities == null) { throw new NullPointerException(); }
        m_items = FXCollections.observableHashMap();
        this.m_entities = m_entities;
        this.huischLedger = huischLedger;

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
     * @param m_items initial ObservableMap to be used as backing map
     */
    public ReceiptTableView(
            @NotNull HuischLedger huischLedger,
            @NotNull ObservableMap<Integer, Receipt> m_items,
            @NotNull ObservableMap<Integer, AccountingEntity> m_entities) {
        if (huischLedger == null) { throw new NullPointerException(); }
        if (m_items == null) { throw new NullPointerException(); }
        if (m_entities == null) { throw new NullPointerException(); }

        this.huischLedger = huischLedger;
        this.m_items = m_items;
        this.m_entities = m_entities;

        setEditable(false); // disable editing in this table. Transactions are edited in a specific dialog presented to the user
        this.m_items.addListener(this);

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
        TableColumn<Receipt, HBox> actionColumn = new TableColumn<>("Acties");

        dateColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getDate().toString())); // FIXME change to property in Receipt class
        payerColumn.setCellValueFactory(param -> m_entities.get(param.getValue().getPayer()).nameProperty());
        nameColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getName()));
        actionColumn.setCellValueFactory(param -> {
            HBox hBox = new HBox();
            Button editButton = new Button("Bewerken");
            editButton.setOnAction(event -> {
                ReceiptDialog receiptDialog = new ReceiptDialog(
                        null,
                        huischLedger,
                        param.getValue()
                );
                receiptDialog.showAndWait();
            });
            hBox.getChildren().add(
                    editButton
            );
            return new ReadOnlyObjectWrapper<>(hBox);
        });

        getColumns().setAll(
                dateColumn,
                payerColumn,
                nameColumn,
                actionColumn
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
