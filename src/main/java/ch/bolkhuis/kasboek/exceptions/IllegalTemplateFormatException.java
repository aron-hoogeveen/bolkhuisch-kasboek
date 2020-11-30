package ch.bolkhuis.kasboek.exceptions;

public class IllegalTemplateFormatException extends RuntimeException {
    public IllegalTemplateFormatException() {
    }

    public IllegalTemplateFormatException(String message) {
        super(message);
    }

    public IllegalTemplateFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalTemplateFormatException(Throwable cause) {
        super(cause);
    }

    public IllegalTemplateFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
