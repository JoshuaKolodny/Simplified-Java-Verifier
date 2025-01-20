package ex5.model.statements;

public class ReturnStatement extends Statement {
    public ReturnStatement(String rawLine) {
        super(rawLine);
    }

    @Override
    public void validate() throws Exception {
        // Ensure it is the last statement in a method
        throw new UnsupportedOperationException("validate not yet implemented");
    }
}
