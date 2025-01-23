package ex5.parser;

/**
 * Exception thrown when syntax errors are encountered in an SJava file.
 * This exception is used to indicate that a line of code does not match
 * the expected syntax patterns.
 *
 * @author Joshua Kolodny, Itamar Lev Ari
 */
public class SyntaxException extends Exception {

    private static final String ERROR_MESSAGE = "The line \"%s\" is illegal";

    /**
     * Constructs a new SyntaxException with a formatted error message.
     *
     * @param line The line of code that caused the syntax error.
     */
    public SyntaxException(String line) {
        super(String.format(ERROR_MESSAGE, line));
    }
}
