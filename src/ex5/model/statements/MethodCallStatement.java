package ex5.model.statements;

import java.util.List;

public class MethodCallStatement extends Statement {
    private final String methodName;
    private final List<String> arguments;

    public MethodCallStatement(String rawLine, String methodName, List<String> arguments) {
        super(rawLine);
        this.methodName = methodName;
        this.arguments = arguments;
    }

    @Override
    public void validate() throws Exception {
        // Check that the method exists
        // Ensure correct number and types of arguments
        throw new UnsupportedOperationException("validate not yet implemented");
    }

    public String getMethodName() {
        return methodName;
    }

    public List<String> getArguments() {
        return arguments;
    }
}
