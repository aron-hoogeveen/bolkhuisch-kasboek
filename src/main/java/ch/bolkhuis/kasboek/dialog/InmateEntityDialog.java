package ch.bolkhuis.kasboek.dialog;

import ch.bolkhuis.kasboek.core.AccountType;
import ch.bolkhuis.kasboek.core.AccountingEntity;
import ch.bolkhuis.kasboek.core.InmateEntity;
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
 * user can create a new InmateEntity.
 *
 * TODO extends AbstractDialog
 */
public class InmateEntityDialog {
    private final Stage stage;

    private final static BorderStroke errorBorderStroke = new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1));
    private final static BorderStroke correctBorderStroke = new BorderStroke(Color.LIGHTGREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1));

    private final static Border errorBorder = new Border(errorBorderStroke);
    private final static Border correctBorder = new Border(correctBorderStroke);

    private final static String numberRegex = "^[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)$";

    private final Tooltip tooltip = new Tooltip();

    private final Label nameLabel = new Label();
    private final TextField nameTextField = new TextField();
    private final Label previousBalanceLabel = new Label();
    private final TextField previousBalanceTextField = new TextField();
    private final Label balanceLabel = new Label();
    private final TextField balanceTextField = new TextField();
    private final Button submitButton = new Button();

    private final InmateEntity old;
    private final int newId;

    /**
     * The result of this "Dialog" will be saved in this field
     */
    private InmateEntity result;
    private boolean resultAvailable = false;

    public InmateEntityDialog(@NotNull Window owner, @NotNull InmateEntity old) {
        if (owner == null) { throw new NullPointerException(); }
        if (old == null) { throw new NullPointerException(); }

        stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);

        this.old = old;
        this.newId = -1;

        initAppearance();
        initBehaviour();
    }

    public InmateEntityDialog(@NotNull Window owner, int id) {
        if (owner == null) { throw new NullPointerException(); }

        stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);

        this.old = null;
        this.newId = id;

        initAppearance();
        initBehaviour();
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
        previousBalanceLabel.setText("Vorige balans:");
        balanceLabel.setText("Balans:");

        nameTextField.setPromptText("Naam van de entiteit");
        previousBalanceTextField.setPromptText("De vorige balans");
        balanceTextField.setPromptText("De huidige balans");

        submitButton.setText("Opslaan");
        submitButton.setMaxWidth(Double.MAX_VALUE);

        nameTextField.setBorder(errorBorder); // TODO check via InmateEntity.isCorrectXXX() if a initial border needs to be set
        previousBalanceTextField.setBorder(errorBorder);
        balanceTextField.setBorder(errorBorder);

        rootGridPane.add(nameLabel, 0, 0);
        rootGridPane.add(nameTextField, 1, 0);
        rootGridPane.add(previousBalanceLabel, 0, 1);
        rootGridPane.add(previousBalanceTextField, 1, 1);
        rootGridPane.add(balanceLabel, 0, 2);
        rootGridPane.add(balanceTextField, 1, 2);
        rootGridPane.add(submitButton, 0, 4, 2, 1); // leave one extra row between content and submit button for prettier appearance

        stage.setScene(new Scene(rootGridPane));
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
        previousBalanceTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(numberRegex)) {
                previousBalanceTextField.setBorder(errorBorder);
                // TODO show a tooltip for a short amount of time
            } else {
                previousBalanceTextField.setBorder(correctBorder);
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

        submitButton.setOnAction(new InmateEntityDialog.processInputEventHandler());
    }

    /**
     * Shows the dialog and returns immediately.
     */
    public void show() {
        // if field old is not null we are updating otherwise creating
        if (old == null)
            stage.setTitle("Huischgenoot toevoegen");
        else
            stage.setTitle("Huischgenoot bewerken");
        stage.show();
    }

    /**
     * Shows the dialog and waits before returning.
     */
    public void showAndWait() {
        // if field old is not null we are updating otherwise creating
        if (old == null)
            stage.setTitle("Huischgenoot toevoegen");
        else
            stage.setTitle("Huischgenoot bewerken");
        stage.showAndWait();
    }

    public InmateEntity getResult() {
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
            final String previousBalanceString = previousBalanceTextField.getText();
            final String balanceString = balanceTextField.getText();

            // Validate the input
            if (!AccountingEntity.isCorrectName(name) || !balanceString.matches(numberRegex) ||
                        !previousBalanceString.matches(numberRegex)) {
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
                    result = new InmateEntity(newId, name, Double.parseDouble(previousBalanceString), Double.parseDouble(balanceString));
                } else {
                    result = new InmateEntity(old.getId(), name, Double.parseDouble(previousBalanceString), Double.parseDouble(balanceString));
                }
            } catch (Exception e) {
                result = null;
                System.err.println("There was an error while constructing a new AccountingEntity in " + InmateEntityDialog.this.getClass());
                e.printStackTrace();
            }
            resultAvailable = true;

            // close the dialog
            stage.close();
        }
    }

}
