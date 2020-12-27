/*
 * Copyright (C) 2020 Aron Hoogeveen
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package ch.bolkhuis.kasboek.dialog;

import ch.bolkhuis.kasboek.App;
import ch.bolkhuis.kasboek.core.AccountingEntity;
import ch.bolkhuis.kasboek.core.HuischLedger;
import ch.bolkhuis.kasboek.core.ResidentEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.controlsfx.control.ListSelectionView;

import java.util.prefs.Preferences;

/**
 * InvoicingDialog is the class used for showing a dialog in which the user can supply all information needed for
 * generating one or more invoices.
 */
public class InvoicingDialog {
    private final Stage stage;

    /*
     * Scene components
     */
    private final Label introTextLabel = new Label();
    private final TextArea introTextTextArea = new TextArea();
    private final Label informationLabel = new Label();
    private final Label targetDirectoryLabel = new Label();
    private final ListSelectionView<ResidentEntity> listSelectionView = new ListSelectionView<>();
    private final Button submitButton = new Button();

    /**
     * Constructor.
     */
    public InvoicingDialog(final Preferences preferences, final HuischLedger huischLedger,
                           final Window owner) {
        stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Facturen Maken");

        initComponents(huischLedger);
    }

    /**
     * Creates and sets the components to a new Scene and calls {@code setScene} on the Stage with that scene.
     */
    private void initComponents(HuischLedger huischLedger) {
        GridPane root = new GridPane();
        root.setMaxWidth(1280);
        root.setMaxHeight(720);

        informationLabel.setText("Schuif alle Huischgenoten naar rechts voor welke je een factuur wil maken. De " +
                "'introtekst' ondersteunt de parameters '${start_date}' en '${end_date}' voor als je in je factuur de " +
                "gefactureerde periode wil aangeven (Bijvoorbeeld: \"Dit is de factuur van ${start_date} tot " +
                "${end_date}\"). Let op: voordat de factuurs gegenereerd worden, wordt de 'doelmap' eerst leeg gemaakt. " +
                "Hierbij worden alle bestanden en submappen in die map verwijderd.");
        introTextLabel.setText("Introtekst:");
        targetDirectoryLabel.setText("Doelmap:");
        submitButton.setText("Factureren");
        submitButton.setMaxWidth(Double.MAX_VALUE);

        informationLabel.setWrapText(true);
        introTextTextArea.setWrapText(true);

        // Initialize the ListSelectionView
        // Get all residents
        ObservableList<ResidentEntity> residentEntities = FXCollections.observableArrayList();
        for (AccountingEntity entity : huischLedger.getAccountingEntities().values()) {
            if (entity instanceof ResidentEntity) {
                residentEntities.add((ResidentEntity) entity);
            }
        }
        listSelectionView.setSourceItems(residentEntities);

        // put the information text inside a scrollpane
        ScrollPane scrollPane = new ScrollPane(informationLabel);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPrefHeight(70);
        scrollPane.setMinHeight(70);
        scrollPane.setMaxHeight(100);

        root.add(scrollPane, 0, 0, 2, 1);
        root.add(introTextLabel, 0, 1);
        root.add(introTextTextArea, 0, 2, 2, 1);
        root.add(targetDirectoryLabel, 0, 3);
        // TODO root.add(targetDirectorySelector
        root.add(listSelectionView, 0, 4, 2, 1);
        root.add(submitButton, 0, 5);

        root.setStyle("-fx-padding: 10px;");
        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(
                App.CSS_STYLES
        );
        stage.setScene(scene);
        stage.sizeToScene();
    }

    /**
     * Calls the {@code showAndWait} method on the Stage of this InvoicingDialog.
     */
    public void showAndWait() {
        stage.showAndWait();
    }
}
