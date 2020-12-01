package ch.bolkhuis.kasboek;

import ch.bolkhuis.kasboek.components.AccountingEntityTreeTableView;
import ch.bolkhuis.kasboek.components.TransactionTableView;
import ch.bolkhuis.kasboek.core.HuischLedger;
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
        fileMenu.getItems().addAll(
                newFile,
                openFile,
                closeFile,
                new SeparatorMenuItem(),
                saveFile,
                saveAsFile
        );
        // Edit menu
        Menu editMenu = new Menu("Bewerken");
        MenuItem addAccountingEntity = new MenuItem("Entiteit toevoegen");
//        addAccountingEntity.setOnAction(new AddAccountingEntityEventHandler());
        MenuItem addInmateEntity = new MenuItem("Huischgenoot toevoegen");
//        addInmateEntity.setOnAction(new AddInmateEntityEventHandler());
//        addAccountingEntity.setOnAction(new AddAccountingEntityEventHandler());
        editMenu.getItems().addAll(
                addAccountingEntity,
                addInmateEntity
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
        AccountingEntityTreeTableView entityTreeTableView = new AccountingEntityTreeTableView(huischLedger.getAccountingEntities());
        entitiesTab.setContent(new BorderPane(entityTreeTableView)); // this should make everything full sized

        // Receipts Tab

        // Transactions Tab
        TransactionTableView transactionTableView = new TransactionTableView(huischLedger.getTransactions());
        System.out.println("Size of transactions: " + huischLedger.getTransactions().values().size());
        transactionsTab.setContent(transactionTableView);

        // Add the tabs and the pane to this BorderPane
        tabPane.getTabs().addAll(
                entitiesTab,
                receiptsTab,
                transactionsTab
        );
        setCenter(tabPane);
    }
}
