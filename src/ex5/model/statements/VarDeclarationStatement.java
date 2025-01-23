package ex5.model.statements;

import ex5.Constants.Constants;
import ex5.model.Scope;
import ex5.model.Variable;
import ex5.model.VariableType;
import ex5.parser.RegexPatterns;
import ex5.validator.SemanticException;

/**
 * Represents a variable declaration statement in SJava.
 * This class handles the declaration of variables, including initialization
 * and final-ness constraints.
 *
 * @author Joshua Kolodny, Itamar Lev Ari
 */
public class VarDeclarationStatement implements Statement {
    private static final String INCOMPATIBLE_VARIABLE_DECLARATION_MESSAGE =
            "Incompatible variable types in declaration: %s with %s";
    private static final String VARIABLE_ALREADY_DECLARED_MESSAGE =
            "Variable %s already declared in this scope";
    private static final String FINAL_VAR_NOT_INITIALIZED_MESSAGE =
            "Final variable %s not initialized";

    private final boolean isFinal; // Whether the variable is final
    private final VariableType type; // The declared type of the variable
    private final String[] declarations; // Array of variable declarations

    /**
     * Constructs a VarDeclarationStatement with specified properties.
     *
     * @param isFinal      Whether the variable is final.
     * @param type         The declared type of the variable.
     * @param declarations The array of variable declarations.
     */
    public VarDeclarationStatement(boolean isFinal, VariableType type, String[] declarations) {
        this.isFinal = isFinal;
        this.type = type;
        this.declarations = declarations;
    }

    /**
     * Validates the variable declaration by processing each declaration.
     *
     * @param scope The scope in which the variables are being declared.
     * @throws SemanticException If there are semantic errors in the declaration.
     */
    @Override
    public void validate(Scope scope) throws SemanticException {
        for (String decl : declarations) {
            processDeclaration(scope, decl);
        }
    }

    /**
     * Processes a single variable declaration, checking type compatibility,
     * ensuring it is not already defined, and enforcing final variable initialization.
     *
     * @param scope The current scope.
     * @param decl  The variable declaration string.
     * @throws SemanticException If there are errors in the declaration.
     */
    private void processDeclaration(Scope scope, String decl) throws SemanticException {
        String[] eqSplit = decl.split("=", 2);
        String varName = eqSplit[0].strip();
        VariableType assignedVariableType = null;

        // If the variable is initialized, determine its assigned type
        if (eqSplit.length == 2) {
            assignedVariableType = getVariableType(scope, eqSplit[1].strip());
        }

        validateTypeCompatibility(assignedVariableType);
        ensureVariableNotRedefined(scope, varName);
        ensureFinalVariableIsInitialized(varName, assignedVariableType);

        // Add the new variable to the scope
        Variable var = new Variable(varName, type, isFinal, assignedVariableType);
        scope.addVariable(var);
    }

    /**
     * Ensures that the assigned value's type is compatible with the declared type.
     *
     * @param assignedVariableType The type of the assigned value.
     * @throws SemanticException If the types are incompatible.
     */
    private void validateTypeCompatibility(VariableType assignedVariableType) throws SemanticException {
        if (assignedVariableType != null && VariableType.isTypeIncompatible(type, assignedVariableType)) {
            throw new SemanticException(String.format(INCOMPATIBLE_VARIABLE_DECLARATION_MESSAGE, type,
                    assignedVariableType));
        }
    }

    /**
     * Ensures the variable is not already declared in the current scope.
     *
     * @param scope   The current scope.
     * @param varName The variable name.
     * @throws SemanticException If the variable is already declared.
     */
    private void ensureVariableNotRedefined(Scope scope, String varName) throws SemanticException {
        if (scope.findVariableInCurrentScope(varName) != null) {
            throw new SemanticException(String.format(VARIABLE_ALREADY_DECLARED_MESSAGE, varName));
        }
    }

    /**
     * Ensures that final variables are initialized upon declaration.
     *
     * @param varName             The variable name.
     * @param assignedVariableType The assigned variable type.
     * @throws SemanticException If a final variable is not initialized.
     */
    private void ensureFinalVariableIsInitialized(String varName, VariableType assignedVariableType)
            throws SemanticException {
        if (isFinal && assignedVariableType == null) {
            throw new SemanticException(String.format(FINAL_VAR_NOT_INITIALIZED_MESSAGE, varName));
        }
    }

    /**
     * Retrieves the type of variable or literal value.
     *
     * @param currentScope The current scope.
     * @param varValue The value being assigned.
     * @return The determined variable type.
     * @throws SemanticException If the value is not recognized.
     */
    private VariableType getVariableType(Scope currentScope, String varValue) throws SemanticException {
        VariableType variableType = null;
        String variableString = RegexPatterns.findValueTypePattern(varValue);

        // If the value is a variable identifier, retrieve its type from scope
        if (variableString != null && variableString.equals(Constants.IDENTIFIER)) {
            Variable var = currentScope.findVariable(varValue);
            if (var == null || var.getValueType() == null) {
                throw new SemanticException(String.format(Constants.INCOMPATIBLE_ASSIGNMENT_MESSAGE,
                        varValue));
            }
            variableType = var.getType();
        } else if (variableString != null) {
            variableType = VariableType.fromString(variableString);
        }
        return variableType;
    }
}
