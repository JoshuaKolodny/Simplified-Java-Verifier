package ex5.validator;

import ex5.model.*;
import ex5.model.statements.Statement;

public class SJavaValidator {

    /**
     * Validate the entire Program's semantics.
     */
    public static void validate(GlobalScope globalScope) throws SemanticException {
        // 1) Validate global scope statements/variables if you want
        validateGlobalScope(globalScope);

        // 2) Validate each method
        for (Method m : globalScope.getMethods()) {
            validateMethod(m);
        }
    }

    private static void validateGlobalScope(Scope scope) throws SemanticException {
        // For each statement, call statement.validate(scope).
        for (Statement stmt : scope.getStatements()) {
            stmt.validate(scope);
        }
    }

    private static void validateMethod(Method method) throws SemanticException {
        // If you want to allow access to global vars, you'd set
        // method.getBodyScope().setParent(program.getGlobalScope());
        // or done so in the parser.
        // Then validate the method body
        for (Statement stmt : method.getBodyScope().getStatements()) {
            stmt.validate(method.getBodyScope());
        }
    }
}
