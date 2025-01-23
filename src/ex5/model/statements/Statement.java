package ex5.model.statements;

import ex5.model.Scope;
import ex5.validator.SemanticException;

/**
 * Base interface for all statements in SJava.
 * Each statement must implement its own validation logic within the provided scope.
 *
 * @author Joshua Kolodny, Itamar Lev Ari
 */
public interface Statement {
    /**
     * Validates the statement within the given scope.
     * Implementations check for semantic correctness based on SJava rules.
     *
     * @param scope The current scope in which the statement resides.
     * @throws SemanticException If the statement is semantically invalid.
     */
    void validate(Scope scope) throws SemanticException;
}
