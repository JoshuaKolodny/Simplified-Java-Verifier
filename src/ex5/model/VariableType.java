package ex5.model;

/**
 * Enum representing the possible variable types in SJava.
 * Supports integer, double, boolean, character, and string types.
 * Provides methods for type conversion and compatibility checks.
 *
 * @author Joshua Kolodny, Itamar Lev Ari
 */
public enum VariableType {
    INT, DOUBLE, BOOLEAN, CHAR, STRING;

    /**
     * Converts a string representation to a VariableType.
     *
     * @param s The string representation of the type.
     * @return The corresponding VariableType.
     * @throws IllegalArgumentException If the type is unknown.
     */
    public static VariableType fromString(String s) {
        return switch (s) {
            case "int" -> INT;
            case "double" -> DOUBLE;
            case "boolean" -> BOOLEAN;
            case "char" -> CHAR;
            case "String" -> STRING;
            default -> throw new IllegalArgumentException("Unknown type: " + s);
        };
    }

    /**
     * Checks whether an assignment between two types is incompatible.
     * This method enforces SJava's type conversion rules.
     *
     * @param assignedToType   The declared type of the variable.
     * @param assignedValueType The type of the assigned value.
     * @return True if the types are incompatible, otherwise false.
     */
    public static boolean isTypeIncompatible(VariableType assignedToType, VariableType assignedValueType) {
        if (assignedValueType == assignedToType) {
            return false;
        }
        if (assignedToType == VariableType.DOUBLE && assignedValueType == VariableType.INT) {
            return false; // Implicit conversion from int to double is allowed
        }
        if (assignedToType == VariableType.BOOLEAN) {
            // Booleans can accept int, double, or another boolean
            return assignedValueType != VariableType.INT && assignedValueType != VariableType.DOUBLE;
        }
        return true;
    }
}
