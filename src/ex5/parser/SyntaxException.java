package ex5.parser;

/**
 * Exception thrown when syntax errors are encountered.
 */
public class SyntaxException extends Exception {
    public SyntaxException(String message) {
        super(message);
    }
}
