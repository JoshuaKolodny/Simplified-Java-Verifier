package ex5.Constants;

/**
 * Defines constant values used throughout the SJava parser and validator.
 * These constants represent various types of statements, variable types, and error messages.
 *
 * This class serves as a central repository for shared string literals to ensure consistency
 * across different components of the system.
 *
 * @author Joshua Kolodny, Itamar Lev Ari
 */
public class Constants {
    public static final String COMMENT = "comment";
    public static final String ASSIGNMENT = "assignment";
    public static final String VAR_DECLARATION = "varDec";
    public static final String METHOD_DECLARATION = "methodDec";
    public static final String METHOD_CALL = "methodCall";
    public static final String IF_WHILE_STATEMENT = "ifWhile";
    public static final String RETURN_STATEMENT = "return";
    public static final String INT_VAR = "int";
    public static final String DOUBLE_VAR = "double";
    public static final String BOOL_VAR = "boolean";
    public static final String CHAR_VAR = "char";
    public static final String STRING_VAR = "String";
    public static final String IDENTIFIER = "identifier";
    public static final String INCOMPATIBLE_ASSIGNMENT_MESSAGE =
            "Incompatible assignment or unknown variable %s";


}
