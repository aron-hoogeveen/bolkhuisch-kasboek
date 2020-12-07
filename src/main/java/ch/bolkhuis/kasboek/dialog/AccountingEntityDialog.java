/**
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
import ch.bolkhuis.kasboek.core.AccountType;
import ch.bolkhuis.kasboek.core.AccountingEntity;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Calling {@code showDialog} on an instance of this class will present the user with a (blocking) dialog in which the
 * user can create a new AccountingEntity.
 *
 * TODO extends AbstractDialog
 */
public class AccountingEntityDialog {
    private final Stage stage;

    private final static BorderStroke errorBorderStroke = new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1));
    private final static BorderStroke correctBorderStroke = new BorderStroke(Color.LIGHTGREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1));

    private final static Border errorBorder = new Border(errorBorderStroke);
    private final static Border correctBorder = new Border(correctBorderStroke);

    private final static String numberRegex = "^[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)$";

    private final Tooltip tooltip = new Tooltip();

    private final Label nameLabel = new Label();
    private final TextField nameTextField = new TextField();
    private final Label accountTypeLabel = new Label();
    private final ComboBox<AccountType> accountTypeComboBox = new ComboBox<>();
    private final Label balanceLabel = new Label();
    private final TextField balanceTextField = new TextField();
    private final Button submitButton = new Button();

    private final AccountingEntity old;
    private final int newId;
    private final AccountType fixedAccountType;

    /**
     * The result of this "Dialog" will be saved in this field
     */
    private AccountingEntity result;
    private boolean resultAvailable = false;

    public AccountingEntityDialog(@NotNull Window owner, @NotNull AccountingEntity old) {
        if (owner == null) { throw new NullPointerException(); }
        if (old == null) { throw new NullPointerException(); }

        stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);

        this.old = old;
        this.newId = -1;
        this.fixedAccountType = null;

        initAppearance();
        initBehaviour();
    }

    /**
     * Creates an AccountingEntityDialog with initial id.
     *
     * @param owner the owner window
     * @param id the id to be used for the created AccountingEntity
     */
    public AccountingEntityDialog(@NotNull Window owner, int id) {
        if (owner == null) { throw new NullPointerException(); }

        stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);

        this.old = null;
        this.newId = id;
        this.fixedAccountType = null;

        initAppearance();
        initBehaviour();
    }

    /**
     * Creates an AccountingEntityDialog with initial id and AccountType.
     *
     * @param owner the owner window
     * @param id the id to be used for the created AccountingEntity
     * @param accountType the AccountType to be used for the created AccountingEntity
     */
    public AccountingEntityDialog(@NotNull Window owner, int id, AccountType accountType) {
        if (owner == null) { throw new NullPointerException(); }

        stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);

        this.old = null;
        this.newId = id;
        this.fixedAccountType = accountType;

        initAppearance();
        initBehaviour();

        // Disable the accountTypeComboBox and set it's value
        accountTypeComboBox.setDisable(true);
        accountTypeComboBox.getSelectionModel().select(accountType);
    }

    /**
     * Initialises the appearance of this "Dialog".
     */
    private void initAppearance() {
        GridPane rootGridPane = new GridPane();
        rootGridPane.setPadding(new Insets(10));
        rootGridPane.setVgap(10);
        rootGridPane.setHgap(5);

        nameLabel.setText("Naam:");
        accountTypeLabel.setText("Type account:");
        balanceLabel.setText("Balans:");

        nameTextField.setPromptText("Naam van de entiteit");
        accountTypeComboBox.setItems(FXCollections.observableList(List.of(AccountType.values())));
        if (accountTypeComboBox.getItems().size() > 0)
            accountTypeComboBox.getSelectionModel().select(AccountType.ASSET);
        balanceTextField.setPromptText("De huidige balans");

        submitButton.setText("Opslaan");
        submitButton.setMaxWidth(Double.MAX_VALUE);

        nameTextField.setBorder(errorBorder); // TODO change this border's color to the color of the background
        balanceTextField.setBorder(errorBorder);

        rootGridPane.add(nameLabel, 0, 0);
        rootGridPane.add(nameTextField, 1, 0);
        rootGridPane.add(accountTypeLabel, 0, 1);
        rootGridPane.add(accountTypeComboBox, 1, 1);
        rootGridPane.add(balanceLabel, 0, 2);
        rootGridPane.add(balanceTextField, 1, 2);
        rootGridPane.add(submitButton, 0, 4, 2, 1); // leave one extra row between content and submit button for prettier appearance

        Scene scene = new Scene(rootGridPane);
        scene.getStylesheets().add(App.CSS_STYLES);
        stage.setScene(scene);
        stage.sizeToScene();
    }

    /**
     * Initialises the behaviour of the components of this "Dialog".
     */
    private void initBehaviour() {
        // Provide realtime error checking for all properties that support it
        nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!AccountingEntity.isCorrectName(newValue)) {
                nameTextField.setBorder(errorBorder);
            } else {
                nameTextField.setBorder(correctBorder);
            }
        });
        balanceTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(numberRegex)) {
                balanceTextField.setBorder(errorBorder);
                // TODO show a tooltip for a short amount of time
            } else {
                balanceTextField.setBorder(correctBorder);
            }
        });

        submitButton.setOnAction(new processInputEventHandler());
    }

    /**
     * Shows the dialog and returns immediately.
     */
    public void show() {
        // if field old is not null we are updating otherwise creating
        if (old == null)
            stage.setTitle("Entiteit toevoegen");
        else
            stage.setTitle("Entiteit bewerken");
        stage.show();
    }

    /**
     * Shows the dialog and waits before returning.
     */
    public void showAndWait() {
        // if field old is not null we are updating otherwise creating
        if (old == null)
            stage.setTitle("Entiteit toevoegen");
        else
            stage.setTitle("Entiteit bewerken");
        stage.showAndWait();
    }

    public AccountingEntity getResult() {
        return result;
    }

    public boolean isResultAvailable() {
        return resultAvailable;
    }

    private class processInputEventHandler implements EventHandler<ActionEvent> {

        /**
         * Invoked when a specific event of the type for which this handler is
         * registered happens.
         *
         * @param event the event which occurred
         */
        @Override
        public void handle(ActionEvent event) {
            final String name = nameTextField.getText();
            final AccountType accountType = accountTypeComboBox.getValue();
            final String balanceString = balanceTextField.getText();

            // Validate the input
            if (!AccountingEntity.isCorrectName(name) || !balanceString.matches(numberRegex)) {
                Dialog<ButtonType> errorDialog = new Dialog<>();
                ButtonType buttonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                errorDialog.getDialogPane().getButtonTypes().add(buttonType);
                errorDialog.setTitle("Error");
                errorDialog.setContentText("Een of meerdere inputs hebben illegale waarden. Vul alsjeblieft legale " +
                        "waarden in.");
                errorDialog.showAndWait();
                return;
            }

            try {
                if (old == null) {
                    if (fixedAccountType == null)
                        result = new AccountingEntity(newId, name, accountType, Double.parseDouble(balanceString));
                    else
                        result = new AccountingEntity(newId, name, fixedAccountType, Double.parseDouble(balanceString));
                } else {
                    result = new AccountingEntity(old.getId(), name, old.getAccountType(), Double.parseDouble(balanceString));
                }
            } catch (Exception e) {
                result = null;
                System.err.println("There was an error while constructing a new AccountingEntity in " + AccountingEntityDialog.this.getClass());
                e.printStackTrace();
            }
            resultAvailable = true;

            // close the dialog
            stage.close();
        }
    }
}
