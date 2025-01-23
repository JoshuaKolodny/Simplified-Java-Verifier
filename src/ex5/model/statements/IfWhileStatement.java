package ex5.model.statements;

import ex5.Constants.Constants;
import ex5.model.Scope;
import ex5.model.Variable;
import ex5.model.VariableType;
import ex5.parser.RegexPatterns;
import ex5.validator.SemanticException;

/**
 * Represents an if or while statement in the SJava language.
 * This statement ensures the conditions are valid and checks for semantic correctness
 * within the associated inner scope.
 *
 * @author Joshua Kolodny, Itamar Lev Ari
 */
public class IfWhileStatement implements Statement {

    private static final String INVALID_CONDITION_TYPE_MESSAGE =
            "Argument '%s' is not a valid condition type";

    private final Scope innerScope; // Scope for statements within the block
    private final String[] conditions; // Conditions controlling the if/while block

    /**
     * Constructs an IfWhileStatement with an inner scope and conditions.
     *
     * @param innerScope The scope containing statements within the block.
     * @param conditions The conditions that control execution.
     */
    public IfWhileStatement(Scope innerScope, String[] conditions) {
        this.innerScope = innerScope;
        this.conditions = conditions;
    }

    /**
     * Validates the if/while statement by ensuring valid conditions
     * and checking the inner scope's statements.
     *
     * @param scope The current scope in which the if/while statement occurs.
     * @throws SemanticException If the statement violates SJava's semantic rules.
     */
    @Override
    public void validate(Scope scope) throws SemanticException {
        // Validate the conditions controlling the if/while block
        validateConditions(scope);

        // Validate statements inside the block's scope
        validateInnerScopeStatements();
    }

    /**
     * Validates the conditions used in the if/while statement.
     *
     * @param scope The current scope in which conditions are checked.
     * @throws SemanticException If any condition is invalid.
     */
    private void validateConditions(Scope scope) throws SemanticException {
        for (String condition : conditions) {
            VariableType conditionType = determineConditionType(scope, condition);
            validateConditionType(condition, conditionType);
        }
    }

    /**
     * Determines the type of a given condition.
     *
     * @param scope The current scope in which the condition exists.
     * @param condition The condition to evaluate.
     * @return The determined variable type of the condition.
     * @throws SemanticException If the condition is not declared.
     */
    private VariableType determineConditionType(Scope scope, String condition) throws SemanticException {
        String conditionTypeString = RegexPatterns.findValueTypePattern(condition);
        if (conditionTypeString.equals(Constants.IDENTIFIER)) {
            return getVariableType(scope, condition);
        }
        return VariableType.fromString(conditionTypeString);
    }

    /**
     * Retrieves the type of a condition variable from the scope.
     *
     * @param scope The current scope.
     * @param condition The variable representing the condition.
     * @return The type of the variable.
     * @throws SemanticException If the variable is not declared or has no known type.
     */
    private VariableType getVariableType(Scope scope, String condition) throws SemanticException {
        Variable variable = scope.findVariable(condition);
        if (variable == null || variable.getValueType() == null) {
            throw new SemanticException(String.format(Constants.INCOMPATIBLE_ASSIGNMENT_MESSAGE, condition));
        }
        return variable.getValueType();
    }

    /**
     * Ensures the condition's type is valid (int, double, or boolean).
     *
     * @param condition The condition being evaluated.
     * @param conditionType The type of the condition.
     * @throws SemanticException If the condition type is invalid.
     */
    private void validateConditionType(String condition, VariableType conditionType) throws SemanticException {
        if (conditionType != VariableType.INT && conditionType != VariableType.DOUBLE && conditionType != VariableType.BOOLEAN) {
            throw new SemanticException(String.format(INVALID_CONDITION_TYPE_MESSAGE, condition));
        }
    }

    /**
     * Validates the statements inside the inner scope of the if/while block.
     *
     * @throws SemanticException If any statement within the block is invalid.
     */
    private void validateInnerScopeStatements() throws SemanticException {
        for (Statement stmt : innerScope.getStatements()) {
            stmt.validate(innerScope);
        }
    }
}