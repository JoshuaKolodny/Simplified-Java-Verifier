package ex5.model.statements;

import ex5.model.VariableType;

public class VariableDeclarationStatement extends Statement {
    private final String type;
    private final VariableType variableName;
    private final String initialValue; // Can be null if no initialization

    public VariableDeclarationStatement(String rawLine, String type, VariableType variableName, String initialValue) {
        super(rawLine);
        this.type = type;
        this.variableName = variableName;
        this.initialValue = initialValue;
    }

    @Override
    public void validate() throws Exception {
        // Check if type is valid (int, double, boolean, etc.)
        // Check if variable name follows naming rules
        // If initialized, ensure value matches type
        throw new UnsupportedOperationException("validate not yet implemented");
    }

    public String getType() {
        return type;
    }

    public VariableType getVariableName() {
        return variableName;
    }

    public String getInitialValue() {
        return initialValue;
    }
}
