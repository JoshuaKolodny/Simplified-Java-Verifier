package ex5.parser;

/**
 * Exception thrown when syntax errors are encountered.
 */
public class SyntaxException extends Exception {
    private static final String ERROR_MESSAGE = "The line \"%s\" is illegal";
    public SyntaxException(String line) {
        super(String.format(ERROR_MESSAGE, line));
    }
}
