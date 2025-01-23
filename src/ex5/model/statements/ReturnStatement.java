package ex5.model.statements;


import ex5.model.Scope;
import ex5.validator.SemanticException;

public class ReturnStatement implements Statement {

    @Override
    public void validate(Scope scope) throws SemanticException {
        // For s-Java, check that we're inside a method scope (not in global).
        // If we want to ensure it's truly inside a method, the simplest approach might be:
        if (!isInsideMethod(scope)) {
            throw new SemanticException("return statement outside a method!");
        }
    }

    private boolean isInsideMethod(Scope scope) {
        // You could store a boolean "isMethodScope" in Scope, or track it differently
        return true;
    }
}
