package ex5.model.statements;


import ex5.Constants.Constants;
import ex5.model.Scope;
import ex5.model.Variable;
import ex5.model.VariableType;
import ex5.parser.RegexPatterns;
import ex5.validator.SemanticException;

import java.util.List;

public class IfWhileStatement implements Statement {

    private final Scope innerScope;       // scope for statements in the block
    private final String[] conditions;

    public IfWhileStatement(Scope innerScope, String[] conditions) {
        this.innerScope = innerScope;
        this.conditions = conditions;
    }

    public Scope getInnerScope() {
        return innerScope;
    }

    @Override
    public void validate(Scope scope) throws SemanticException {
        // 1) Check the condition is valid (any variables used exist, etc.)
        // 2) Validate child scope statements
        for (String condition : conditions) {
            String conditionTypeString = RegexPatterns.findValueTypePattern(condition);
            VariableType conditionType;
            if (conditionTypeString.equals(Constants.IDENTIFIER)) {
                Variable variable = scope.findVariable(condition);
                if (variable == null || variable.getValueType() == null) {
                    throw new SemanticException("Incompatible assignment or unknown variable " + condition);
                }
                conditionType = variable.getValueType();
            } else {
                conditionType = VariableType.fromString(conditionTypeString);
            }
            // It's presumably a literal => check if it's type-compatible
            // We'll do a naive approach:
            if (conditionType != VariableType.INT && conditionType != VariableType.DOUBLE
                    && conditionType != VariableType.BOOLEAN ) {
                throw new SemanticException("argument '" + condition + "' is not a valid condition type");
            }
        }
        for (Statement stmt : innerScope.getStatements()) {
            stmt.validate(innerScope);
        }
    }
}
