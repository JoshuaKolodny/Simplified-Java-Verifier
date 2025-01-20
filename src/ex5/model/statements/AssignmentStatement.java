package ex5.model.statements;

public class AssignmentStatement extends Statement {
    private final String variableName;
    private final String assignedValue;

    public AssignmentStatement(String rawLine, String variableName, String assignedValue) {
        super(rawLine);
        this.variableName = variableName;
        this.assignedValue = assignedValue;
    }

    @Override
    public void validate() throws Exception {
        // Ensure variable exists and was initialized before assignment
        // Ensure assignedValue is of correct type

        throw new UnsupportedOperationException("validate not yet implemented");
    }

    public String getVariableName() {
        return variableName;
    }

    public String getAssignedValue() {
        return assignedValue;
    }
}

