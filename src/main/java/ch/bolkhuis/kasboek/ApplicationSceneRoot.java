package ch.bolkhuis.kasboek;

import ch.bolkhuis.kasboek.components.AccountingEntityTreeTableView;
import ch.bolkhuis.kasboek.core.HuischLedger;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import org.jetbrains.annotations.NotNull;

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

    private void initAppearance() {
        setMinSize(App.MIN_WIDTH, App.MIN_HEIGHT);
        setPrefSize(App.INITIAL_WIDTH, App.INITIAL_HEIGHT);
    }

    private void createAndSetChildren() {
        // The tabpane with the AccountingEntities, Receipts and Transactions
        TabPane tabPane = new TabPane();
        tabPane.setId("main-tab-pane");
        Tab entitiesTab = new Tab("Entiteiten");
        Tab receiptsTab = new Tab("Bonnetjes");
        Tab transactionsTab = new Tab("Transacties");

        // Entities Tab
        AccountingEntityTreeTableView entityTreeTableView = new AccountingEntityTreeTableView(huischLedger.getAccountingEntities());
        entitiesTab.setContent(new BorderPane(entityTreeTableView)); // this should make everything full sized

        // Receipts Tab

        // Transactions Tab

        // Add the tabs and the pane to this BorderPane
        tabPane.getTabs().addAll(
                entitiesTab,
                receiptsTab,
                transactionsTab
        );
        setCenter(tabPane);
    }
}
