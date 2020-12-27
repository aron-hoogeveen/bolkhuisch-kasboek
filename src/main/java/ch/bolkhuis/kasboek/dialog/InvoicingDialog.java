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
import ch.bolkhuis.kasboek.ApplicationSceneRoot;
import ch.bolkhuis.kasboek.core.AccountingEntity;
import ch.bolkhuis.kasboek.core.HuischLedger;
import ch.bolkhuis.kasboek.core.ResidentEntity;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.*;
import org.controlsfx.control.ListSelectionView;
import org.controlsfx.control.textfield.CustomTextField;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Vector;
import java.util.prefs.Preferences;

/**
 * InvoicingDialog is the class used for showing a dialog in which the user can supply all information needed for
 * generating one or more invoices.
 */
public class InvoicingDialog {
    private final Stage stage;
    private final Preferences preferences;
    
    private final FileProperty fileProperty = new FileProperty();

    /*
     * Scene components
     */
    private final Label introTextLabel = new Label();
    private final TextArea introTextTextArea = new TextArea();
    private final Label informationLabel = new Label();
    private final Label targetDirectoryLabel = new Label();
    private final CustomTextField targetDirectoryTextField = new CustomTextField();
    private final ListSelectionView<ResidentEntity> listSelectionView = new ListSelectionView<>();
    private final Button submitButton = new Button();

    /**
     * Constructor.
     */
    public InvoicingDialog(@NotNull final Preferences preferences,
                           @NotNull final HuischLedger huischLedger,
                           final Window owner) {
        this.preferences = preferences;

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
                "${end_date}\"). Let op: voordat de facturen gegenereerd worden, wordt de 'doelmap' eerst leeg gemaakt. " +
                "Hierbij worden alle bestanden en submappen in die map verwijderd.");
        introTextLabel.setText("Introtekst:");
        targetDirectoryLabel.setText("Uitvoermap:");
        submitButton.setText("Factureren");
        submitButton.setMaxWidth(Double.MAX_VALUE);

        informationLabel.setWrapText(true);
        introTextTextArea.setWrapText(true);

        // targetDirectory
        targetDirectoryTextField.setPromptText("Selecteer een uitvoer locatie...");
        Image image = new Image("icons8-folder-24.png");
        ImageView imageView = new ImageView(image);
        imageView.setCursor(Cursor.HAND);
        imageView.setOnMouseClicked(new TargetDirectorySelectionEventHandler());
        targetDirectoryTextField.setRight(imageView);
        targetDirectoryTextField.setEditable(false);
        targetDirectoryTextField.setTooltip(new Tooltip("Tekstinput wordt momenteel niet ondersteunt voor dit component"));
        fileProperty.addListener((observable, oldValue, newValue) -> {
            targetDirectoryTextField.setText(newValue.getAbsolutePath());
        });


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
        root.add(targetDirectoryTextField, 1, 3);
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

    private class TargetDirectorySelectionEventHandler implements EventHandler<MouseEvent> {
        /**
         * Invoked when a specific event of the type for which this handler is
         * registered happens.
         *
         * @param event the event which occurred
         */
        @Override
        public void handle(MouseEvent event) {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Uitvoermap");
            if (fileProperty.getValue() == null) {
                directoryChooser.setInitialDirectory(new File(preferences.get(
                        ApplicationSceneRoot.PREF_FILE_CHOOSER_DIRECTORY,
                        ApplicationSceneRoot.PREF_DEFAULT_FILE_CHOOSER_DIRECTORY
                )));
            } else {
                directoryChooser.setInitialDirectory(fileProperty.getValue());
            }
            File newTargetDir = directoryChooser.showDialog(stage);

            if (newTargetDir != null) {
                fileProperty.set(newTargetDir);
            }
        }
    }

    private static class FileProperty implements ObservableObjectValue<File> {
        private File item = null;
        Vector<ChangeListener<File>> changeListeners = new Vector<>();
        Vector<InvalidationListener> invalidationListeners = new Vector<>();

        /**
         * Returns the current value of this {@code ObservableObjectValue<T>}.
         *
         * @return The current value
         */
        @Override
        public File get() {
            return item;
        }

        public void set(File file) {
            File old = this.item;
            this.item = file;

            fireChange(old, this.item);
        }

        /**
         * Adds a {@link ChangeListener} which will be notified whenever the value
         * of the {@code ObservableValue} changes. If the same listener is added
         * more than once, then it will be notified more than once. That is, no
         * check is made to ensure uniqueness.
         * <p>
         * Note that the same actual {@code ChangeListener} instance may be safely
         * registered for different {@code ObservableValues}.
         * <p>
         * The {@code ObservableValue} stores a strong reference to the listener
         * which will prevent the listener from being garbage collected and may
         * result in a memory leak. It is recommended to either unregister a
         * listener by calling {@link #removeListener(ChangeListener)
         * removeListener} after use.
         *
         * @param listener The listener to register
         * @throws NullPointerException if the listener is null
         * @see #removeListener(ChangeListener)
         */
        @Override
        public void addListener(ChangeListener<? super File> listener) {
            changeListeners.add((ChangeListener<File>) listener);
        }

        /**
         * Removes the given listener from the list of listeners that are notified
         * whenever the value of the {@code ObservableValue} changes.
         * <p>
         * If the given listener has not been previously registered (i.e. it was
         * never added) then this method call is a no-op. If it had been previously
         * added then it will be removed. If it had been added more than once, then
         * only the first occurrence will be removed.
         *
         * @param listener The listener to remove
         * @throws NullPointerException if the listener is null
         * @see #addListener(ChangeListener)
         */
        @Override
        public void removeListener(ChangeListener<? super File> listener) {
            changeListeners.remove(listener);
        }

        /**
         * Returns the current value of this {@code ObservableValue}
         *
         * @return The current value
         */
        @Override
        public File getValue() {
            return get();
        }

        /**
         * Adds an {@link InvalidationListener} which will be notified whenever the
         * {@code Observable} becomes invalid. If the same
         * listener is added more than once, then it will be notified more than
         * once. That is, no check is made to ensure uniqueness.
         * <p>
         * Note that the same actual {@code InvalidationListener} instance may be
         * safely registered for different {@code Observables}.
         * <p>
         * The {@code Observable} stores a strong reference to the listener
         * which will prevent the listener from being garbage collected and may
         * result in a memory leak. It is recommended to either unregister a
         * listener by calling {@link #removeListener(InvalidationListener)
         * removeListener} after use.
         *
         * @param listener The listener to register
         * @throws NullPointerException if the listener is null
         * @see #removeListener(InvalidationListener)
         */
        @Override
        public void addListener(InvalidationListener listener) {
            invalidationListeners.add(listener);
        }

        /**
         * Removes the given listener from the list of listeners, that are notified
         * whenever the value of the {@code Observable} becomes invalid.
         * <p>
         * If the given listener has not been previously registered (i.e. it was
         * never added) then this method call is a no-op. If it had been previously
         * added then it will be removed. If it had been added more than once, then
         * only the first occurrence will be removed.
         *
         * @param listener The listener to remove
         * @throws NullPointerException if the listener is null
         * @see #addListener(InvalidationListener)
         */
        @Override
        public void removeListener(InvalidationListener listener) {
            invalidationListeners.remove(listener);
        }

        private void fireChange(File oldValue, File newValue) {
            for (ChangeListener<File> cl : changeListeners) {
                cl.changed(this, oldValue, newValue);
            }
        }

        private void fireInvalidation() {
            for (InvalidationListener il : invalidationListeners) {
                il.invalidated(this);
            }
        }

    }
}
