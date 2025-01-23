package ex5.model;

/**
 * Represents a variable in SJava.
 * A variable has a name, type, and may be final.
 * It can also store an optional assigned value type.
 *
 * @author Joshua Kolodny, Itamar Lev Ari
 */
public class Variable {
    private final String name; // Variable name
    private final VariableType type; // Declared variable type
    private final boolean isFinal; // Whether the variable is final
    private VariableType valueType; // The type of the assigned value (optional)

    /**
     * Constructs a Variable with a name, type, and optional value type.
     *
     * @param name      The name of the variable.
     * @param type      The declared type of the variable.
     * @param isFinal   Whether the variable is final.
     * @param valueType The assigned value type, if initialized.
     */
    public Variable(String name, VariableType type, boolean isFinal, VariableType valueType) {
        this.name = name;
        this.type = type;
        this.isFinal = isFinal;
        this.valueType = valueType;
    }

    /**
     * Retrieves the name of the variable.
     *
     * @return The variable name as a string.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the declared type of the variable.
     *
     * @return The declared VariableType.
     */
    public VariableType getType() {
        return type;
    }

    /**
     * Checks if the variable is final.
     *
     * @return True if the variable is final, otherwise false.
     */
    public boolean isFinal() {
        return isFinal;
    }

    /**
     * Retrieves the assigned value type of the variable.
     *
     * @return The assigned VariableType, or null if not initialized.
     */
    public VariableType getValueType() {
        return valueType;
    }
}
