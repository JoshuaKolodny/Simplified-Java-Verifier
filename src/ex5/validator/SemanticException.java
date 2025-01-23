package ex5.validator;

/**
 * Exception thrown when semantic errors are detected in an SJava file.
 * Semantic errors occur when the code is syntactically correct but
 * violates the rules of the language (e.g., using an undeclared variable,
 * type mismatches, or reassigning a final variable).
 *
 * @author Joshua Kolodny, Itamar Lev Ari
 */
public class SemanticException extends Exception {

    /**
     * Constructs a new SemanticException with a specified error message.
     *
     * @param message The error message describing the semantic issue.
     */
    public SemanticException(String message) {
        super(message);
    }
}
