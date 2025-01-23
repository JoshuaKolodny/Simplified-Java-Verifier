package ex5.model.statements;

import ex5.Constants.Constants;
import ex5.model.Scope;
import ex5.model.Variable;
import ex5.model.VariableType;
import ex5.parser.RegexPatterns;
import ex5.validator.SemanticException;

/**
 * Represents an assignment statement in the SJava language.
 * This statement assigns a value to a variable while ensuring type compatibility and scope validity.
 *
 * @author Joshua Kolodny, Itamar Lev Ari
 */
public class AssignmentStatement implements Statement {

    private static final String INCOMPATIBLE_VARIABLE_TYPES = "incompatible variable types: %s and %s";
    private static final String VARIABLE_NOT_DECLARED_MESSAGE = "Variable %s not declared in scope";
    private static final String FINAL_VAR_REASSIGNMENT_MESSAGE = "Final variable %s cannot be reassigned";

    private final String variableName;
    private final String assignedValue; // e.g., "5", "anotherVar", "true"

    /**
     * Constructs an AssignmentStatement with a variable name and an assigned value.
     *
     * @param variableName  The name of the variable being assigned a value.
     * @param assignedValue The value being assigned to the variable.
     */
    public AssignmentStatement(String variableName, String assignedValue) {
        this.variableName = variableName;
        this.assignedValue = assignedValue;
    }

    /**
     * Validates the assignment statement within the given scope.
     * Ensures the variable exists, is not final, and that the types are compatible.
     *
     * @param scope The current scope in which the assignment occurs.
     * @throws SemanticException If the assignment violates SJava's semantic rules.
     */
    @Override
    public void validate(Scope scope) throws SemanticException {
        // Find and validate the variable being assigned
        Variable assignedToVar = findAndValidateVariable(scope);

        // Ensure that the variable is not final before assignment
        validateFinalVariable(assignedToVar);

        // Determine the type of the assigned value
        VariableType assignedVariableType = determineAssignedVariableType(scope);

        // Validate that the assigned value is compatible with the variable's type
        validateTypeCompatibility(assignedToVar, assignedVariableType);

        // Add the updated variable to the scope
        scope.addVariable(new Variable(assignedToVar.getName(), assignedToVar.getType(),
                false, assignedVariableType));
    }

    /**
     * Finds the variable in the given scope and ensures it exists.
     *
     * @param scope The current scope.
     * @return The found variable.
     * @throws SemanticException If the variable is not declared in scope.
     */
    private Variable findAndValidateVariable(Scope scope) throws SemanticException {
        Variable assignedToVar = scope.findVariable(variableName);
        if (assignedToVar == null) {
            throw new SemanticException(String.format(VARIABLE_NOT_DECLARED_MESSAGE, variableName));
        }
        return assignedToVar;
    }

    /**
     * Ensures that the variable being assigned is not final.
     *
     * @param assignedToVar The variable being assigned a new value.
     * @throws SemanticException If attempting to reassign a final variable.
     */
    private void validateFinalVariable(Variable assignedToVar) throws SemanticException {
        if (assignedToVar.isFinal()) {
            throw new SemanticException(String.format(FINAL_VAR_REASSIGNMENT_MESSAGE, variableName));
        }
    }

    /**
     * Determines the type of the assigned value.
     * If the assigned value is another variable, its type is retrieved from the scope.
     *
     * @param scope The current scope.
     * @return The type of the assigned value.
     * @throws SemanticException If the assigned value is an undeclared variable.
     */
    private VariableType determineAssignedVariableType(Scope scope) throws SemanticException {
        // Check if the assigned value is a known primitive type or an identifier
        String assignedVarTypeString = RegexPatterns.findValueTypePattern(assignedValue);
        if (assignedVarTypeString.equals(Constants.IDENTIFIER)) {
            // Retrieve the type of the variable from the scope
            return getVariableTypeFromScope(scope);
        } else {
            return VariableType.fromString(assignedVarTypeString);
        }
    }

    /**
     * Retrieves the type of an assigned variable from the scope.
     *
     * @param scope The current scope.
     * @return The type of the assigned variable.
     * @throws SemanticException If the variable is not declared or has no known type.
     */
    private VariableType getVariableTypeFromScope(Scope scope) throws SemanticException {
        Variable variable = scope.findVariable(assignedValue);
        if (variable == null || variable.getValueType() == null) {
            throw new SemanticException(String.format(Constants.INCOMPATIBLE_ASSIGNMENT_MESSAGE, assignedValue));
        }
        return variable.getType();
    }

    /**
     * Ensures the assigned value's type is compatible with the variable's type.
     *
     * @param assignedToVar      The variable being assigned a value.
     * @param assignedVariableType The type of the assigned value.
     * @throws SemanticException If the types are incompatible.
     */
    private void validateTypeCompatibility(Variable assignedToVar, VariableType assignedVariableType) throws SemanticException {
        // Check if the assigned value's type is compatible with the variable's type
        if (VariableType.isTypeIncompatible(assignedToVar.getType(), assignedVariableType)) {
            throw new SemanticException(String.format(INCOMPATIBLE_VARIABLE_TYPES, assignedToVar.getType(), assignedVariableType));
        }
    }
}
