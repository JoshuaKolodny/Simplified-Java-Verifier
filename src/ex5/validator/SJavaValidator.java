package ex5.validator;

import ex5.model.*;
import ex5.model.statements.Statement;

/**
 * Validates the semantic correctness of an SJava program.
 * This class ensures that variables, method declarations, and statements
 * conform to the language's semantic rules, such as type compatibility,
 * variable scope, and method body validation.
 *
 * @author Joshua Kolodny, Itamar Lev Ari
 */
public class SJavaValidator {

    /**
     * Validates the entire SJava program's semantics.
     * This includes validating global scope variables and method declarations.
     *
     * @param globalScope The global scope of the program.
     * @throws SemanticException If any semantic errors are detected.
     */
    public static void validate(GlobalScope globalScope) throws SemanticException {
        // Validate global scope variables and statements
        validateGlobalScope(globalScope);

        // Validate each method within the global scope
        for (Method m : globalScope.getMethods()) {
            validateMethod(m);
        }
    }

    /**
     * Validates statements in the global scope.
     * Ensures that all global variables and statements conform to semantic rules.
     *
     * @param scope The global scope containing global statements.
     * @throws SemanticException If semantic errors occur in global statements.
     */
    private static void validateGlobalScope(Scope scope) throws SemanticException {
        // Validate each statement in the global scope
        for (Statement stmt : scope.getStatements()) {
            stmt.validate(scope);
        }
    }

    /**
     * Validates the body of a method.
     * Ensures that statements inside the method conform to semantic rules,
     * including variable usage, type compatibility, and return statements.
     *
     * @param method The method to validate.
     * @throws SemanticException If any semantic errors occur in the method body.
     */
    private static void validateMethod(Method method) throws SemanticException {
        // Validate each statement in the method's body scope
        for (Statement stmt : method.getBodyScope().getStatements()) {
            stmt.validate(method.getBodyScope());
        }
    }
}
