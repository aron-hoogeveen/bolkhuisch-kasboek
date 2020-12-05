package ch.bolkhuis.kasboek.dialog;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class ErrorDialog extends Dialog<ButtonType> {
    public ErrorDialog(String contentText) {
        setTitle("Foutmelding");
        setContentText(contentText);

        // Do not set the Modality to APPLICATION_MODAL. If an error happens in an infinite loop, this would cause the program to become unusable
//        initModality(Modality.APPLICATION_MODAL);

        // Add a single ButtonType to the dialogpane
        getDialogPane().getButtonTypes().add(
                new ButtonType("OK", ButtonBar.ButtonData.OK_DONE)
        );
    }

}
