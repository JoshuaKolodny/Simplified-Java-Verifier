package ex5.model;

public class Variable {
    private final String name;
    private final VariableType type;      // "int", "double", "String", "boolean", "char"
    private final boolean isFinal;
    private boolean isInitialized;

    public Variable(String name, VariableType type, boolean isFinal, boolean isInitialized) {
        this.name = name;
        this.type = type;
        this.isFinal = isFinal;
        this.isInitialized = isInitialized;
    }

    public String getName() {
        return name;
    }

    public VariableType getType() {
        return type;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public void setInitialized(boolean initialized) {
        this.isInitialized = initialized;
    }
}

