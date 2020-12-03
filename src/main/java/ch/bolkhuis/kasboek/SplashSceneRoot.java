package ch.bolkhuis.kasboek;

import ch.bolkhuis.kasboek.core.*;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.controlsfx.control.*;
import java.awt.Desktop;

import java.io.File;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SplashSceneRoot extends BorderPane {
    private static final double WIDTH = 840;
    private static final double HEIGHT = 484;

    private final Image logo;
    private final App app;

    /**
     * Creates a new Splash Scene Root with the supplied Image as logo.
     */
    public SplashSceneRoot(App app, Image logo) {
        if (app == null) { throw new NullPointerException(); }
        if (logo == null) { throw new NullPointerException(); }

        this.app = app;
        this.logo = logo;

        setId("SplashSceneRoot");
        initAppearance();
        createAndSetChildren();
    }

    /**
     * Sets all properties for the visual appearance of this SplashSceneRoot.
     */
    private void initAppearance() {
        // Use a fixed size
        setMinSize(WIDTH, HEIGHT);
        setPrefSize(WIDTH, HEIGHT);
        setMaxSize(WIDTH, HEIGHT);
    }

    /**
     * Creates and sets all children for this SplashSceneRoot.
     */
    private void createAndSetChildren() {
        // Recent opened Ledgers
        List<RecentLedgerFile> recentLedgerFiles = loadRecentLedgers();
        // FIXME implement the actual loading of recent Ledger files
        recentLedgerFiles = new ArrayList<>(List.of(
                new RecentLedgerFile(new File("~/nonexistant"), "Placeholder for Ledger"),
                new RecentLedgerFile(new File("~/some/file.hlf"), "Official Ledger ofc")
        ));

        // create and set the main menu
        GridPane centerGrid = new GridPane();
        centerGrid.setId("centerGrid");
//        centerGrid.setGridLinesVisible(true); // Use this for debugging the layout of the GridPane
        centerGrid.setAlignment(Pos.CENTER);

        // imageView for the Huisch logo. Use a BorderPane for centering the ImageView
        BorderPane imageBorderPane = new BorderPane();
        ImageView imageView = new ImageView(logo);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(180);
        imageBorderPane.setCenter(imageView);
        centerGrid.add(imageBorderPane, 0, 0);
        // title
        Label title = new Label("Huisch Kasboek");
        title.setId("splashTitle");
        centerGrid.add(title, 0, 1);

        // Add buttons for creating of importing a Ledger or for getting help
        Button newLedgerButton = new Button("Nieuw Kasboek", new ImageView("plus-sign-16.png"));
        newLedgerButton.setOnAction(event -> {
            // FIXME this is a temporary action until the loading is implemented
            HuischLedger huischLedger = createTemporaryHuischLedger();

            app.changeToApplicationScene(new ApplicationSceneRoot(app, huischLedger, null));
        });
        Button importLedgerButton = new Button("Importeer Kasboek", new ImageView("import-16.png"));
        Button getHelpButton = new Button("Krijg hulp", new ImageView("question-mark-16.png"));
        centerGrid.add(newLedgerButton, 0, 2);
        centerGrid.add(importLedgerButton, 0, 3);
        centerGrid.add(getHelpButton, 0, 4);

        setCenter(centerGrid);

        // Initial width for the centerGrid
        centerGrid.setMinSize(WIDTH, HEIGHT);
        centerGrid.setPrefSize(WIDTH, HEIGHT);
        centerGrid.setMaxSize(WIDTH, HEIGHT);
        if (recentLedgerFiles != null) {
            // Set a list of RecentLedgerFiles on the Left side of this BorderPane
            LedgerFileListView ledgerFileListView = new LedgerFileListView(FXCollections.observableList(recentLedgerFiles));
            ledgerFileListView.setFocusTraversable(false); // We only want TAB to be used for the menu in the center

            // Set a fixed width of a third of the total width
            ledgerFileListView.setMinSize(WIDTH / 3.0, HEIGHT);
            ledgerFileListView.setPrefSize(WIDTH / 3.0, HEIGHT);
            ledgerFileListView.setMaxSize(WIDTH / 3.0, HEIGHT);

            setLeft(ledgerFileListView);

            // Adjust the centerGrid's width
            double newWidth = WIDTH / 3.0 * 2;
            centerGrid.setMinWidth(newWidth);
            centerGrid.setPrefWidth(newWidth);
            centerGrid.setMaxWidth(newWidth);
        }

        // Add a creator notice. Please do not edit this notice
        HyperlinkLabel creatorNotice = new HyperlinkLabel("Gemaakt door [Aron Hoogeveen]");
        creatorNotice.setOnMouseClicked(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/aron-hoogeveen"));
            } catch (Exception ignored) {
                System.err.println("Java.AWT.Desktop is not available.");
            }
        });
        HBox creatorNoticeHBox = new HBox(creatorNotice);
        creatorNoticeHBox.setAlignment(Pos.CENTER);

        setBottom(creatorNoticeHBox);
    }

    /**
     * Loads the file with all the recent Ledgers and returns the corresponding RecentLedgerFiles.
     *
     * @return List of RecentLedgerFiles
     */
    private List<RecentLedgerFile> loadRecentLedgers() {
        System.err.println("SplashSceneRoot#loadRecentLedgers() is not implemented.");

        return null;
    }

    private HuischLedger createTemporaryHuischLedger() {
        HuischLedger huischLedger = new HuischLedger();
        InmateEntity inmate = new InmateEntity(
                0,
                "Gerrit",
                0,
                0
        );
        AccountingEntity bank = new AccountingEntity(
                1,
                "ING",
                AccountType.ASSET,
                0
        );
        huischLedger.addAccountingEntity(inmate);
        huischLedger.addAccountingEntity(bank);

        Transaction t1 = new Transaction(
                0,
                0,
                1,
                25,
                null,
                LocalDate.parse("2020-01-01"),
                "Some Transaction"
        );
        Transaction t2 = new Transaction(
                1,
                1,
                0,
                25,
                null,
                LocalDate.parse("2020-01-02"),
                "Another transaction"
        );
        // Receipt transactions
        Receipt r1 = new Receipt(
                0,
                "Makrorun",
                Set.of(2, 3),
                LocalDate.parse("2020-01-15"),
                0
        );
        Transaction r1_t3 = new Transaction(
                2,
                1,
                0,
                25,
                0,
                LocalDate.parse("2020-01-15"),
                "Trans 1 bij Receipt 1"
        );
        Transaction r1_t4 = new Transaction(
                3,
                1,
                0,
                25,
                0,
                LocalDate.parse("2020-01-15"),
                "Trans 2 bij Receipt 1"
        );

        huischLedger.addTransaction(t1);
        huischLedger.addTransaction(t2);
        huischLedger.addTransaction(r1_t4);
        huischLedger.addTransaction(r1_t3);
        huischLedger.addReceipt(r1);

        huischLedger.setNextAccountingEntityId(2);

        return huischLedger;
    }
}
