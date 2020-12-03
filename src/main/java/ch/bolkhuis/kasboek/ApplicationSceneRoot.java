package ch.bolkhuis.kasboek;

import ch.bolkhuis.kasboek.components.AccountingEntityTreeTableView;
import ch.bolkhuis.kasboek.components.ReceiptTableView;
import ch.bolkhuis.kasboek.components.TransactionTableView;
import ch.bolkhuis.kasboek.core.AccountingEntity;
import ch.bolkhuis.kasboek.core.HuischLedger;
import ch.bolkhuis.kasboek.core.InmateEntity;
import ch.bolkhuis.kasboek.core.Transaction;
import ch.bolkhuis.kasboek.dialog.AccountingEntityDialog;
import ch.bolkhuis.kasboek.dialog.InmateEntityDialog;
import ch.bolkhuis.kasboek.dialog.TransactionDialog;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.events.Event;

import java.io.File;

/**
 * ApplicationSceneRoot is the root for the main Scene of the Application class. The root presents the user with an
 * overview of a single HuischLedger.
 *
 * The HuischLedger is loaded from a File.
 *
 * @author Aron Hoogeveen
 */
public class ApplicationSceneRoot extends BorderPane {
    private final HuischLedger huischLedger;
    private final File huischLedgerFile;
    private final App app;

    /**
     * Default constructor.
     */
    public ApplicationSceneRoot(@NotNull App app, @NotNull HuischLedger huischLedger, File huischLedgerFile) {
        if (app == null) { throw new NullPointerException(); }
        if (huischLedger == null) { throw new NullPointerException(); }

        this.app = app;
        this.huischLedger = huischLedger;
        this.huischLedgerFile = huischLedgerFile;

        initAppearance();
        createAndSetChildren();
    }

    /**
     * Set all settings for this BorderPane that have to do with its appearance.
     */
    private void initAppearance() {
        setMinSize(App.MIN_WIDTH, App.MIN_HEIGHT);
        setPrefSize(App.INITIAL_WIDTH, App.INITIAL_HEIGHT);
    }

    /**
     * Creates and sets the children of this BorderPane. This method is called in all constructors.
     */
    private void createAndSetChildren() {
        createAndSetTopMenuBar(); // setTop()
        createAndSetCenterTabPane(); // setCenter()
    }

    /**
     * Creates and sets the MenuBar in the top of the window.
     */
    private void createAndSetTopMenuBar() {
        // File menu
        Menu fileMenu = new Menu("Bestand");
        MenuItem newFile = new MenuItem("Nieuwe maken");
//        newFile.setOnAction(new NewFileEventHandler());
        MenuItem openFile = new MenuItem("Openen");
//        openFile.setOnAction(new OpenFileEventHandler());
        MenuItem closeFile = new MenuItem("Sluiten");
//        closeFile.setOnAction(new CloseFileEventHandler());
        MenuItem saveFile = new MenuItem("Opslaan");
//        saveFile.setOnAction(new SaveFileEventHandler());
        MenuItem saveAsFile = new MenuItem("Opslaan als");
//        saveAsFile.setOnAction(new SaveAsFileEventHandler());
        MenuItem preferences = new MenuItem("Instellingen");
        fileMenu.getItems().addAll(
                newFile,
                openFile,
                closeFile,
                new SeparatorMenuItem(),
                saveFile,
                saveAsFile,
                new SeparatorMenuItem(),
                preferences
        );
        // Edit menu
        Menu editMenu = new Menu("Bewerken");
        MenuItem addAccountingEntity = new MenuItem("Entiteit toevoegen");
        addAccountingEntity.setOnAction(new AddAccountingEntityEventHandler());
        MenuItem addInmateEntity = new MenuItem("Huischgenoot toevoegen");
    addInmateEntity.setOnAction(new AddInmateEntityEventHandler());
        MenuItem addReceipt = new MenuItem("Bonnetje toevoegen");
//        addAccountingEntity.setOnAction(new AddAccountingEntityEventHandler());
        MenuItem addTransaction = new MenuItem("Transactie toevoegen");
        addTransaction.setOnAction(new AddTransactionEventHandler());
        editMenu.getItems().addAll(
                addAccountingEntity,
                addInmateEntity,
                addTransaction,
                addReceipt
        );
        // Print menu
        Menu toolsMenu = new Menu("Gereedschappen");
        Menu printSubMenu = new Menu("Factuur Printen");
        MenuItem printInvoicesForAllInmates = new MenuItem("Alle Huischgenoten");
        MenuItem printInvoicesForSelectedInmates = new MenuItem("Selecteer Huischgenoten");
        printSubMenu.getItems().addAll(
                printInvoicesForAllInmates,
                printInvoicesForSelectedInmates
        );
        toolsMenu.getItems().addAll(
                printSubMenu
        );

        // Add the File Menus to the MenuBar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(
                fileMenu,
                editMenu,
                toolsMenu
        );

        setTop(menuBar);
    }

    /**
     * Creates and sets the tab pane showing the AccountingEntities, Receipts and Transactions.
     */
    private void createAndSetCenterTabPane() {
        // The tabpane with the AccountingEntities, Receipts and Transactions
        TabPane tabPane = new TabPane();
        tabPane.setId("main-tab-pane");
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Tab entitiesTab = new Tab("Entiteiten");
        Tab receiptsTab = new Tab("Bonnetjes");
        Tab transactionsTab = new Tab("Transacties");

        // Entities Tab
        AccountingEntityTreeTableView entityTreeTableView = new AccountingEntityTreeTableView(
                this,
                huischLedger.getAccountingEntities()
        );
        entitiesTab.setContent(new BorderPane(entityTreeTableView)); // this should make everything full sized

        // Receipts Tab
        ReceiptTableView receiptTableView = new ReceiptTableView(huischLedger.getReceipts(), huischLedger.getAccountingEntities());
        receiptsTab.setContent(receiptTableView);

        // Transactions Tab
        TransactionTableView transactionTableView = new TransactionTableView(
                huischLedger.getTransactions(),
                huischLedger.getAccountingEntities(),
                huischLedger.getReceipts()
        );
        transactionsTab.setContent(transactionTableView);

        // Add the tabs and the pane to this BorderPane
        tabPane.getTabs().addAll(
                entitiesTab,
                receiptsTab,
                transactionsTab
        );
        setCenter(tabPane);
    }

    /**
     * Shows an EntityDialog in which the user can create a new entity that is then added to the {@code huischLedger}.
     */
    public void showAccountingEntityDialog() {
        AccountingEntityDialog accountingEntityDialog = new AccountingEntityDialog(
                app.getPrimaryStage(),
                huischLedger.getAndIncrementNextAccountingEntityId());
        accountingEntityDialog.showAndWait();
        // isResultAvailable return false if no AccountingEntity was created
        if (accountingEntityDialog.isResultAvailable()) {
            AccountingEntity accountingEntity = accountingEntityDialog.getResult();
            try {
                huischLedger.addAccountingEntity(accountingEntity);
            } catch (Exception e) {
                System.err.println("Could not add to AccountingEntity returned from the AccountingEntityDialog");
                e.printStackTrace();
            }
        }
    }

    public void showInmateEntityDialog() {
        InmateEntityDialog inmateEntityDialog = new InmateEntityDialog(
                app.getPrimaryStage(),
                huischLedger.getAndIncrementNextAccountingEntityId());
        inmateEntityDialog.showAndWait();
        // isResultAvailable return false if no AccountingEntity was created
        if (inmateEntityDialog.isResultAvailable()) {
            InmateEntity inmateEntity = inmateEntityDialog.getResult();
            try {
                huischLedger.addAccountingEntity(inmateEntity);
            } catch (Exception e) {
                System.err.println("Could not add the InmateEntity returned from the InmateEntityDialog");
                e.printStackTrace();
            }
        }
    }

    public void showTransactionDialog() {
        TransactionDialog transactionDialog = new TransactionDialog(
                app.getPrimaryStage(),
                huischLedger.getAccountingEntities(),
                huischLedger.getReceipts(),
                huischLedger.getAndIncrementNextTransactionId()
        );
        transactionDialog.showAndWait();

        if (transactionDialog.isResultAvailable()) {
            Transaction transaction = transactionDialog.getResult();
            try {
                huischLedger.addTransaction(transaction);
            } catch (Exception e) {
                System.err.println("Could not add the Transaction returned from the TransactionDialog");
                e.printStackTrace();
            }
        }
    }

    public App getApp() { return app; }

    public HuischLedger getHuischLedger() { return huischLedger; }

    // *****************************************************************************************************************
    // * Click Event Handlers
    // *****************************************************************************************************************
    private class AddAccountingEntityEventHandler implements EventHandler<ActionEvent> {

        /**
         * Invoked when a specific event of the type for which this handler is
         * registered happens.
         *
         * @param event the event which occurred
         */
        @Override
        public void handle(ActionEvent event) {
            try {
                showAccountingEntityDialog();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class AddInmateEntityEventHandler implements EventHandler<ActionEvent> {

        /**
         * Invoked when a specific event of the type for which this handler is
         * registered happens.
         *
         * @param event the event which occurred
         */
        @Override
        public void handle(ActionEvent event) {
            try {
                showInmateEntityDialog();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class AddTransactionEventHandler implements EventHandler<ActionEvent> {

        /**
         * Invoked when a specific event of the type for which this handler is
         * registered happens.
         *
         * @param event the event which occurred
         */
        @Override
        public void handle(ActionEvent event) {
            try {
                showTransactionDialog();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
