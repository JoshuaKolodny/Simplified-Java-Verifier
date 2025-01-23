package ex5.model.statements;

import ex5.Constants.Constants;
import ex5.model.Scope;
import ex5.model.Variable;
import ex5.model.VariableType;
import ex5.parser.RegexPatterns;
import ex5.validator.SemanticException;

public class AssignmentStatement implements Statement {

    private final String variableName;
    private final String assignedValue; // e.g. "5", "anotherVar", "true"

    public AssignmentStatement(String variableName, String assignedValue) {
        this.variableName = variableName;
        this.assignedValue = assignedValue;
    }

    @Override
    public void validate(Scope scope) throws SemanticException {
        // 1) find the variable
        Variable assignedToVar = scope.findVariable(variableName);
        if (assignedToVar == null) {
            throw new SemanticException("Variable " + variableName + " not declared in scope");
        }
        // 2) check final
        if (assignedToVar.isFinal()) {
            // final variable can't be re-assigned
            throw new SemanticException("Final variable " + variableName + " cannot be reassigned");
        }
        // 3) Check that assignedValue is valid
        //    For example, if assignedValue is another variable's name => check existence, type
        //    Or if assignedValue is a literal => check it's type-compatible.
        //    We'll keep it super simple here:
        String assignedVarTypeString = RegexPatterns.findValueTypePattern(assignedValue);
        VariableType assignedVariableType = null;
        if (assignedVarTypeString.equals(Constants.IDENTIFIER)) {
            Variable variable = scope.findVariable(assignedValue);
            if (variable == null || variable.getValueType() == null) {
                throw new SemanticException("Incompatible assignment or unknown variable " + assignedValue);
            } else {
                assignedVariableType = variable.getType();
            }
        } else {
            assignedVariableType = VariableType.fromString(assignedVarTypeString);
        }

        if (!VariableType.isTypeCompatible(assignedToVar.getType(), assignedVariableType)) {
            throw new SemanticException("incompatible variable types");
        }
        scope.addVariable(new Variable(assignedToVar.getName(), assignedToVar.getType(),
                false, assignedVariableType));
    }

}
