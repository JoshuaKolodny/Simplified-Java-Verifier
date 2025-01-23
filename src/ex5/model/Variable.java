package ex5.model;

public class Variable {
    private final String name;
    private final VariableType type;
    private final boolean isFinal;
    private VariableType valueType;

    public Variable(String name, VariableType type, boolean isFinal, VariableType valueType) {
        this.name = name;
        this.type = type;
        this.isFinal = isFinal;
        this.valueType = valueType;
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

    public VariableType getValueType() {
        return valueType;
    }

    public void setValueType(VariableType valueType) {
        this.valueType = valueType;
    }
}
