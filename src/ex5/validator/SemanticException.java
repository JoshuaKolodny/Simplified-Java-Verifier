package ex5.validator;

/**
 * Exception thrown when semantic errors (e.g., variable misuses) are detected.
 */
public class SemanticException extends Exception {
    public SemanticException(String message) {
        super(message);
    }
}
