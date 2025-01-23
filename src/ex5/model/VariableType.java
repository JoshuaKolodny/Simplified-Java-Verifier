package ex5.model;

public enum VariableType {
    INT, DOUBLE, BOOLEAN, CHAR, STRING;

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

    public static boolean isTypeCompatible(VariableType assignedToType, VariableType assignedValueType) {
        // e.g. int -> double is valid? boolean -> int is valid?
        // Implement your own rules. We'll do a tiny example:
        if (assignedValueType == assignedToType) {
            return true;
        }
        if (assignedToType == VariableType.DOUBLE && assignedValueType == VariableType.INT) {
            return true;
        }
        if (assignedToType == VariableType.BOOLEAN) {
            // booleans can accept int or double or boolean
            return assignedValueType == VariableType.INT
                    || assignedValueType == VariableType.DOUBLE;
        }
        return false;
    }
}
