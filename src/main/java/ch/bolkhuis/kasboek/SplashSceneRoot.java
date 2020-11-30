package ch.bolkhuis.kasboek;

import ch.bolkhuis.kasboek.core.AccountType;
import ch.bolkhuis.kasboek.core.AccountingEntity;
import ch.bolkhuis.kasboek.core.HuischLedger;
import ch.bolkhuis.kasboek.core.InmateEntity;
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
import java.util.ArrayList;
import java.util.List;

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
        // FIXME DEBUG
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
            HuischLedger huischLedger = new HuischLedger();
            InmateEntity inmate = new InmateEntity(
                    huischLedger.getAndIncrementNextAccountingEntityId(),
                    "Gerrit",
                    0,
                    0
            );
            AccountingEntity bank = new AccountingEntity(
                    huischLedger.getAndIncrementNextAccountingEntityId(),
                    "ING",
                    AccountType.ASSET,
                    0
            );
            huischLedger.addAccountingEntity(inmate);
            huischLedger.addAccountingEntity(bank);

            Stage stage = app.getPrimaryStage();
            stage.hide();
            stage.setResizable(true);
            stage.setMinWidth(App.MIN_WIDTH);
            stage.setMinHeight(App.MIN_HEIGHT);
            app.setApplicationScene(new Scene(
                    new ApplicationSceneRoot(app, huischLedger, null),
                    App.INITIAL_WIDTH,
                    App.INITIAL_HEIGHT));
            stage.setScene(app.getApplicationScene());
            stage.show();
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
}
