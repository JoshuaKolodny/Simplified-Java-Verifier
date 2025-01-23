package ex5.model.statements;


import ex5.model.Scope;
import ex5.validator.SemanticException;

/**
 * Base interface for all statements in s-Java.
 */
public interface Statement {
    /**
     * Semantic validation method. Each statement implements its own logic
     * for verifying correctness within the provided scope.
     */
    void validate(Scope scope) throws SemanticException;
}
