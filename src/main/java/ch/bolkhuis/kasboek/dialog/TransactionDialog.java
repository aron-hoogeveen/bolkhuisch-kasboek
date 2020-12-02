package ch.bolkhuis.kasboek.dialog;

import ch.bolkhuis.kasboek.core.AccountingEntity;
import ch.bolkhuis.kasboek.core.Receipt;
import ch.bolkhuis.kasboek.core.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionDialog extends AbstractDialog<Transaction> {

    private final int newId;
    private final ObservableMap<Integer, AccountingEntity> accountingEntityObservableMap;
    private final ObservableMap<Integer, Receipt> receiptObservableMap;

    private final Label dateLabel = new Label();
    private final DatePicker datePicker = new DatePicker();
    private final Label debtorLabel = new Label();
    private final ComboBox<AccountingEntity> debtorComboBox = new ComboBox<>();
    private final Label creditorLabel = new Label();
    private final ComboBox<AccountingEntity> creditorComboBox = new ComboBox<>();
    private final Label receiptLabel = new Label();
    private final ComboBox<Receipt> receiptComboBox = new ComboBox<>();
    private final Label amountLabel = new Label();
    private final TextField amountTextField = new TextField();
    private final Label descriptionLabel = new Label();
    private final TextField descriptionTextField = new TextField();

    private final Button submitButton = new Button();

    /**
     * Creates a new AbstractDialog and initialises its owner.
     *
     * @param owner owner to be used for the stage
     * @param id the id used for the new Transaction
     */
    public TransactionDialog(
            @NotNull Window owner,
            @NotNull ObservableMap<Integer, AccountingEntity> accountingEntities,
            @NotNull ObservableMap<Integer, Receipt> receipts,
            int id
    ) {
        super(owner);

        this.accountingEntityObservableMap = accountingEntities;
        this.receiptObservableMap = receipts;
        this.newId = id;

        initAppearance();
        initBehaviour();
    }

    /**
     * Creates a new AbstractDialog and initialises its owner and the old T to load.
     *
     * @param owner owner to be used for the stage
     * @param old   T to be edited
     */
    public TransactionDialog(
            @NotNull Window owner,
            @NotNull ObservableMap<Integer, AccountingEntity> accountingEntities,
            @NotNull ObservableMap<Integer, Receipt> receipts,
            @NotNull Transaction old
    ) {
        super(owner, old);

        this.accountingEntityObservableMap = accountingEntities;
        this.receiptObservableMap = receipts;
        this.newId = -1; // default id for when the id of old is used

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

        // Set the labels' text
        dateLabel.setText("Datum:");
        debtorLabel.setText("Debtor:");
        creditorLabel.setText("Creditor:");
        receiptLabel.setText("Receipt:");
        amountLabel.setText("Bedrag:");
        descriptionLabel.setText("Beschrijving:");
        submitButton.setText("Opslaan");

        // Set the prompts for the TextFields
        amountTextField.setPromptText("Het bedrag in euros");
        descriptionTextField.setPromptText("Beschrijving van deze transactie");

        // Set the possible values for the ComboBoxes
        List<AccountingEntity> accountingEntities = new ArrayList<>(accountingEntityObservableMap.values());
        ObservableList<AccountingEntity> accountingEntityObservableList = FXCollections.observableList(accountingEntities);
        debtorComboBox.setItems(accountingEntityObservableList);
        creditorComboBox.setItems(accountingEntityObservableList);
        List<Receipt> receipts = new ArrayList<>(receiptObservableMap.values());
        ObservableList<Receipt> receiptObservableList = FXCollections.observableList(receipts);
//        receiptObservableList.add(null); // It is totally valid for a transaction to not be attached to a Receipt
        // TODO add a null item to the BEGIN of the items list so the user can undo a faulty selection
        receiptComboBox.setItems(receiptObservableList);

        // Set the initial values if old is not null
        if (old != null) {
            datePicker.setValue(old.getDate());
            debtorComboBox.getSelectionModel().select(accountingEntityObservableMap.get(old.getDebtorId()));
            creditorComboBox.getSelectionModel().select(accountingEntityObservableMap.get(old.getCreditorId()));
            if (old.getReceiptId() != null) {
                receiptComboBox.getSelectionModel().select(receiptObservableMap.get(old.getReceiptId()));
            }
        } else {
            // Set the date for the DatePicker to today
            datePicker.setValue(LocalDate.now());
        }

        // Set the maximum widths of all inputs to Double.MAX_VALUE for equal widths
        datePicker.setMaxWidth(Double.MAX_VALUE);
        debtorComboBox.setMaxWidth(Double.MAX_VALUE);
        creditorComboBox.setMaxWidth(Double.MAX_VALUE);
        receiptComboBox.setMaxWidth(Double.MAX_VALUE);
        amountTextField.setMaxWidth(Double.MAX_VALUE);
        descriptionTextField.setMaxWidth(Double.MAX_VALUE);
        submitButton.setMaxWidth(Double.MAX_VALUE);

        // Add all components to the rootGridPane
        rootGridPane.add(dateLabel, 0, 0);
        rootGridPane.add(datePicker, 1, 0);
        rootGridPane.add(receiptLabel, 2, 0);
        rootGridPane.add(receiptComboBox, 3, 0);
        rootGridPane.add(debtorLabel, 0, 1);
        rootGridPane.add(debtorComboBox, 1, 1);
        rootGridPane.add(creditorLabel, 2, 1);
        rootGridPane.add(creditorComboBox, 3, 1);
        rootGridPane.add(amountLabel, 0, 2);
        rootGridPane.add(amountTextField, 1, 2);
        rootGridPane.add(descriptionLabel, 0, 3);
        rootGridPane.add(descriptionTextField, 1, 3, 3, 1);
        rootGridPane.add(submitButton, 2, 5, 2, 1);

        stage.setScene(new Scene(rootGridPane));
        stage.sizeToScene();
    }

    @Override
    public void show() {
        setTitle();
        stage.show();
    }

    @Override
    public void showAndWait() {
        setTitle();
        stage.showAndWait();
    }

    /**
     * Sets the title of the scene based on whether an old Transaction is provided.
     */
    private void setTitle() {
        if (old == null)
            stage.setTitle("Transactie toevoegen");
        else
            stage.setTitle("Transactie bewerken");
    }

    /**
     * Sets behaviours of components that need them. E.g. sets action handlers.
     */
    @Override
    protected void initBehaviour() {
        System.err.println("The initBehaviour method of TransactionDialog is currently empty");
    }
}
