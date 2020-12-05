package ch.bolkhuis.kasboek.dialog;

import ch.bolkhuis.kasboek.components.TransactionTableView;
import ch.bolkhuis.kasboek.core.AccountingEntity;
import ch.bolkhuis.kasboek.core.HuischLedger;
import ch.bolkhuis.kasboek.core.Receipt;
import ch.bolkhuis.kasboek.core.Transaction;
import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;
import javafx.util.StringConverter;
import org.controlsfx.control.SearchableComboBox;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ReceiptDialog is a "Dialog" for viewing and editing a {@link Receipt}.
 *
 * @author Aron Hoogeveen
 */
public class ReceiptDialog extends AbstractDialog<Receipt> implements SetChangeListener<Integer> {
    private final ObservableMap<Integer, Transaction> transactionObservableMap = FXCollections.observableHashMap();
    private final HuischLedger huischLedger;

    // Nodes
    private final Label nameLabel = new Label();
    private final TextField nameTextField = new TextField();
    private final Label dateLabel = new Label();
    private final DatePicker datePicker = new DatePicker();
    private final Label payerLabel = new Label();
    private final SearchableComboBox<AccountingEntity> payerComboBox = new SearchableComboBox<>();
    private final Label transactionsLabel = new Label();
    private final TransactionTableView transactionTableView;

    /**
     * Creates a new AbstractDialog and initialises its owner and the old T to load.
     *
     * @param owner owner to be used for the stage
     * @param huischLedger the HuischLedger with which to populate the TransactionTableView
     * @param old   T to be edited
     */
    public ReceiptDialog(@NotNull Window owner, @NotNull HuischLedger huischLedger, @NotNull Receipt old) {
        super(owner, old);

        this.huischLedger = huischLedger;

        // populate the observable map with all transactions that belong to the receipt
        for (Map.Entry<Integer, Transaction> entry : huischLedger.getTransactions().entrySet()) {
            if (old.getTransactionIdSet().contains(entry.getKey())) {
                transactionObservableMap.put(entry.getKey(), entry.getValue());
            }
        }

        transactionTableView = new TransactionTableView(
                transactionObservableMap,
                huischLedger.getAccountingEntities(),
                huischLedger.getReceipts(),
                true // hide the column with value receipt, because we are only viewing transactions of one receipt
        );

        // Listen for changes in the accompanied transactions
        old.getTransactionIdSet().addListener(this);

        initAppearance();
        initBehaviour();
    }

    /**
     * Initialises and sets a root to a new Scene and sets the scene to be used by the stage.
     */
    @Override
    protected void initAppearance() {
        GridPane rootGridPane = new GridPane();
        rootGridPane.setPadding(new Insets(10));
        rootGridPane.setVgap(10);
        rootGridPane.setHgap(5);

        // Set the text of the labels
        nameLabel.setText("Naam:");
        dateLabel.setText("Datum:");
        payerLabel.setText("Betaler:");
        transactionsLabel.setText("Transacties:");

        // Set simple fields
        nameTextField.setText(old.getName());
        datePicker.setValue(old.getDate());

        // set searchable combo boxes
        List<AccountingEntity> accountingEntitiesList = new ArrayList<>(huischLedger.getAccountingEntities().values());
        ObservableList<AccountingEntity> accountingEntityObservableList = FXCollections.observableList(accountingEntitiesList);
        payerComboBox.setItems(accountingEntityObservableList);
        payerComboBox.getSelectionModel().select(huischLedger.getAccountingEntities().get(old.getPayer()));
        payerComboBox.setDisable(true); // The Receipt already contains transactions connected to this payer, and therefore this value must not change.

        // the transactionTableView is set in the constructor because of the way of constructing it.

        // row 0
        rootGridPane.add(nameLabel, 0, 0);
        rootGridPane.add(nameTextField, 1, 0);
        // empty column
        rootGridPane.add(dateLabel, 3, 0);
        rootGridPane.add(datePicker, 4, 0);
        // row 1
        rootGridPane.add(payerLabel, 0, 1);
        rootGridPane.add(payerComboBox, 1, 1);
        // row 2
        rootGridPane.add(transactionsLabel, 0, 2, 5, 1);
        rootGridPane.add(transactionTableView, 0, 3, 5, 1);

        stage.setScene(new Scene(rootGridPane));
        stage.setTitle("Bewerken van bonnetje: " + old.getName());
        stage.titleProperty().bindBidirectional(nameTextField.textProperty(), new StringConverter<String>() {
            @Override
            public String toString(String object) {
                return "Bewerken van bonnetje: " + object;
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        });
        stage.sizeToScene();
    }

    /**
     * Sets behaviours of components that need them. E.g. sets action handlers.
     */
    @Override
    protected void initBehaviour() {

    }

    /**
     * Called after a change has been made to an ObservableSet.
     * This method is called on every elementary change (add/remove) once.
     * This means, complex changes like removeAll(Collection) or clear()
     * may result in more than one call of onChanged method.
     *
     * @param change the change that was made
     */
    @Override
    public void onChanged(SetChangeListener.Change<? extends Integer> change) {
        // whenever a Transaction is removed from the receipt, remove it from the transactionObservableMap
        // whenever a Transaction is added to the receipt, add it to the transactionObservableMap
        if (change.wasAdded()) {
            if (change.getElementAdded() == null)
                return;
            int index = change.getElementAdded();
            Transaction transaction = huischLedger.getTransactions().get(index);
            // do not add null valued transactions
            if (transaction == null)
                return;
            transactionObservableMap.put(index, transaction);
        }
        if (change.wasRemoved()) {
            if (change.getElementRemoved() == null)
                return;
            int index = change.getElementRemoved();
            transactionObservableMap.remove(index); // remove the Transaction with id index
        }
    }
}
